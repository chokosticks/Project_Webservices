package main;

import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import groovy.xml.QName;

import matching.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antondahlin on 2016-03-01.
 */
@SuppressWarnings("Duplicates")
public class WSDLParser {


    private String namespace = "http://www.w3.org/2001/XMLSchema";
    private Wordnet wordnet;
    private final double LOWERBOUND = 0.2;
    private List<WSDLFile> wsdlFiles1 = new ArrayList<WSDLFile>();
    private List<WSDLFile> wsdlFiles2 = new ArrayList<WSDLFile>();
    private String wsdlPath;
    WSMatchingType wsMatchingType = new WSMatchingType();

    public WSDLParser() {
        String basePath = new File("").getAbsolutePath();
        wsdlPath = basePath.replace("\\", "/") + "/src/main/resources/WSDLs/";

        wordnet = new Wordnet();
        loadWSDLs();
    }


    public void loadWSDLs(){

        File wsdls = new File(wsdlPath);

        for(File file: wsdls.listFiles()){
            System.out.println("wsdl file path name: " + wsdlPath);
            WSDLFile wsdlFile = new WSDLFile(wsdlPath+file.getName(), true);
            wsdlFiles1.add(wsdlFile);
            wsdlFiles2.add(wsdlFile);
        }
    }

    public void match() {
        wsMatchingType = new WSMatchingType();

        for(WSDLFile wsdlFile1: wsdlFiles1) {
            for(WSDLFile wsdlFile2: wsdlFiles2) {
                if(wsdlFile1.equals(wsdlFile2)) continue;

                MatchedWebServiceType matchedWebServiceType = match(wsdlFile1, wsdlFile2);
                if(matchedWebServiceType != null)
                    wsMatchingType.getMatching().add(matchedWebServiceType);
            }
        }

    }

    public void writeFile(String task)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(WSMatchingType.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            String s = WSDLParser.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            System.out.println(new File(s + "/"+task+"Output.xml").getAbsolutePath());
            s =  s.substring(0,s.lastIndexOf("/"));
            OutputStream os = new FileOutputStream(s + "/"+task+"Output.xml" );
            marshaller.marshal( wsMatchingType, os );
            os.close();
        } catch (JAXBException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    private MatchedWebServiceType match(WSDLFile wsdlFile1, WSDLFile wsdlFile2) {
        MatchedWebServiceType matchedWebServiceType = new MatchedWebServiceType();
        matchedWebServiceType.setInputServiceName(wsdlFile1.getService().getName());
        matchedWebServiceType.setOutputServiceName(wsdlFile2.getService().getName());  

        double serviceScore = 0.0;
        int operationsCount = 0;

        // Porttypes from wsdl1
        for(PortType inPortType: wsdlFile1.getDefinitions().getPortTypes()) {
            // For each operation in portType of wsdlFile1
            for(Operation inOp: inPortType.getOperations()) {
                MatchedOperationType operation = new MatchedOperationType();

                double opScore = 0.0; int elementCount = 0;
                operation.setInputOperationName(inOp.getName());
                Message inMessage = inOp.getInput().getMessage();

                // For each part of a message from wsdlFile1
                for(Part inPart: inMessage.getParts()) {
                    if(!isSimple(inPart)) continue;
                    String inputPart = inPart.getName(); 

                    // Ouput of wsdl2
                    for(PortType outPortType: wsdlFile2.getDefinitions().getPortTypes()) {
                        // For each operation in portType of wsdl2
                        for (Operation outOp: outPortType.getOperations()) {
                            operation.setOutputOperationName(outOp.getName());
                            Message outMessage = outOp.getOutput().getMessage();
                            // For each part of a message from wsdl2
                            for(Part outPart: outMessage.getParts()) {
                                if(!isSimple(outPart)) continue;

                                String outputPart = outPart.getName();

                                double score = wordnet.calculateLevenstheinScore(inputPart, outputPart);

                                if(score >= LOWERBOUND) {
                                    opScore += score;
                                    elementCount++;

                                    MatchedElementType matchedElementType = new MatchedElementType();
                                    matchedElementType.setInputElement(inputPart);
                                    matchedElementType.setOutputElement(outputPart);
                                    matchedElementType.setScore(score);

                                    operation.getMacthedElement().add(matchedElementType);
                                }
                            }
                        }
                    }
                }
                if(!operation.getMacthedElement().isEmpty()) {
                    double opFinalScore = opScore / elementCount;
                    operation.setOpScore(opFinalScore);
                    serviceScore += opFinalScore;
                    operationsCount++;
                    matchedWebServiceType.getMacthedOperation().add(operation);
                }
            }
        }
        if(!matchedWebServiceType.getMacthedOperation().isEmpty()) {
            double serviceFinalScore = serviceScore / operationsCount;
            matchedWebServiceType.setWsScore(serviceFinalScore);


            // Print some shit
            System.out.println("MATCHES FOUND FOR SERVICE : " + matchedWebServiceType.getInputServiceName());
            System.out.println("MATCHED WITH : " + matchedWebServiceType.getOutputServiceName());
            for(MatchedOperationType op: matchedWebServiceType.getMacthedOperation()) {
                System.out.println("--Operation: " + op.getInputOperationName());
                System.out.println("++Operation: " + op.getOutputOperationName());
                for(MatchedElementType el: op.getMacthedElement())
                    System.out.println("==== " + el.getInputElement() + " <-> " + el.getOutputElement() + "(" + el.getScore() + ")");
            }
        }
        else
            return null;

        return matchedWebServiceType;
    }

    private boolean isSimple(Part part) {
        QName qname = part.getType() == null ? part.getElement().getType() :
                part.getType().getQname();
        if (qname == null) return false;
        String ns = qname.getNamespaceURI();
        String l = qname.getLocalPart();
        return ns.equals(namespace) &&
                (l.equals("int") || l.equals("double") || l.equals("date") || l.equals("string")||
                        l.equals("double"));
    }
}
