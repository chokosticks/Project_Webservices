package main;

import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import generated.MatchedElementType;
import generated.MatchedOperationType;
import generated.MatchedWebServiceType;
import generated.WSMatchingType;
import groovy.xml.QName;
import org.jdom.input.SAXBuilder;

import java.io.File;

/**
 * Created by antondahlin on 2016-03-01.
 */
public class WSDLParser {


    private String namespace = "http://www.w3.org/2001/XMLSchema";
    private WordnetMatcher wm;
    private final double LOWERBOUND = 0.53;

    public WSDLParser() {
        wm = new WordnetMatcher();
    }

    public void matchAll() {
        WSMatchingType wsMatchingType = new WSMatchingType();

        for(WSDLFile w1: WSDLManager.getInstance().getWsdlFiles()) {
            for(WSDLFile w2: WSDLManager.getInstance().getWsdlFiles()) {
                if(w1.equals(w2)) continue;

                MatchedWebServiceType matchedWebServiceType = match(w1, w2);
                if(matchedWebServiceType != null)
                    wsMatchingType.getMacthing().add(matchedWebServiceType);
            }
        }
    }

    private MatchedWebServiceType match(WSDLFile wsdlFile1, WSDLFile wsdlFile2) {
        MatchedWebServiceType matchedWebServiceType = new MatchedWebServiceType();
        matchedWebServiceType.setInputServiceName(wsdlFile.getService().getName());
        matchedWebServiceType.setOutputServiceName(wsdlFile2.getService().getName());  

        double serviceScore = 0.0; int operationsCount = 0;

        // Service > Port > Binding > Port type > Operation > Message > Part > Types
        // Get all port types from wsdlFile1
        for(PortType inPortType: wsdlFile.getDefs().getPortTypes()) {
            // For each operation in portType of wsdlFile1
            for(Operation inOp: inPortType.getOperations()) {
                MatchedOperationType operation = new MatchedOperationType();

                double opScore = 0.0; int elementCount = 0;
                operation.setInputOperationName(inOp.getName());
                Message inMessage = inOp.getInput().getMessage();

                // For each part of the message wsdlFile1
                for(Part inPart: inMessage.getParts()) {
                    if(!isSimple(inPart)) continue;
                    String inputPart = inPart.getName(); 

                    // Compare it with all output operations of wsdlFile2
                    for(PortType outPortType: wsdlFile2.getDefs().getPortTypes()) {
                        
                        for (Operation outOp: outPortType.getOperations()) {
                            operation.setOutputOperationName(outOp.getName());
                            Message outMessage = outOp.getOutput().getMessage();

                            for(Part outPart: outMessage.getParts()) {
                                if(!isSimple(outPart)) continue;

                                String outputPart = outPart.getName();

                                double score = wm.calculateScore(inputPart, outputPart);

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
                    // If we actually had matches
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
