package main;

import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import matching.MatchedElementType;
import matching.MatchedOperationType;
import matching.MatchedWebServiceType;
import matching.WSMatchingType;
import ontology.MyOntManager;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by antondahlin on 2016-03-10.
 */
@SuppressWarnings("Duplicates")
public class SAWSDLParser {

    private String namespace = "http://www.w3.org/2001/XMLSchema";
    private final double LOWERBOUND = 0.1;
    private double score = 0f;
    private MyOntManager myOntManager;
    private OWLOntologyManager owlOntologyManager;
    private OWLOntology owlOntology;
    private Reasoner reasoner;
    private HashMap<String, OWLClass> classes;
    private String sawsdlPath = "./src/main/resources/SAWSDLs/";
    private List<WSDLFile> sawsdlFiles1 = new ArrayList<WSDLFile>();
    private List<WSDLFile> sawsdlFiles2 = new ArrayList<WSDLFile>();



    public SAWSDLParser(){
        myOntManager = new MyOntManager();
        owlOntologyManager = myOntManager.initializeOntologyManager();
        owlOntology = myOntManager.initializeOntology(owlOntologyManager, "./src/main/resources/OWL/SUMO.owl");
        reasoner = myOntManager.initializeReasoner(owlOntology, owlOntologyManager);
        classes = myOntManager.loadClasses(reasoner);
        loadSAWSDLs();
    }


    public void loadSAWSDLs(){

        File wsdls = new File(sawsdlPath);

        for(File file: wsdls.listFiles()){
            WSDLFile wsdlFile = new WSDLFile(sawsdlPath+file.getName(), false);
            sawsdlFiles1.add(wsdlFile);
            sawsdlFiles2.add(wsdlFile);
        }
    }

    public void match() {
        WSMatchingType wsMatchingType = new WSMatchingType();

        for(WSDLFile wsdlFile1: sawsdlFiles1) {
            for(WSDLFile wsdlFile2: sawsdlFiles2) {
                if(wsdlFile1.equals(wsdlFile2)) continue;

                MatchedWebServiceType matchedWebServiceType = match(wsdlFile1, wsdlFile2);
                if(matchedWebServiceType != null)
                    wsMatchingType.getMacthing().add(matchedWebServiceType);
            }
        }
    }

    private MatchedWebServiceType match(WSDLFile wsdlFile1, WSDLFile wsdlFile2) {
        MatchedWebServiceType matchedWebServiceType = new MatchedWebServiceType();
        matchedWebServiceType.setInputServiceName(wsdlFile1.getService().getName());
        matchedWebServiceType.setOutputServiceName(wsdlFile2.getService().getName());



        double serviceScore = 0.0; int operationsCount = 0;

        // Get all port types from wsdlFile1
        for(PortType inPortType: wsdlFile1.getDefinitions().getPortTypes()) {
            // For each operation in portType of wsdlFile1
            for(Operation inOp: inPortType.getOperations()) {
                MatchedOperationType operation = new MatchedOperationType();

                double opScore = 0.0; int elementCount = 0;
                operation.setInputOperationName(inOp.getName());
                Message inMessage = inOp.getInput().getMessage();

                // For each part of the message wsdlFile1
                for(Part inPart: inMessage.getParts()) {
                    String inputPart = inPart.getName();

                    String typeIn = wsdlFile1.getTypes().get(inputPart);
                    String classsIn = wsdlFile1.getClasses().get(typeIn);



                    // Compare it with all output operations of wsdlFile2
                    for(PortType outPortType: wsdlFile2.getDefinitions().getPortTypes()) {

                        for (Operation outOp: outPortType.getOperations()) {
                            operation.setOutputOperationName(outOp.getName());
                            Message outMessage = outOp.getOutput().getMessage();

                            for(Part outPart: outMessage.getParts()) {

                                String outputPart = outPart.getName();

                                String typeOut = wsdlFile2.getTypes().get(outputPart);
                                String classsOut = wsdlFile2.getClasses().get(typeOut);

                                System.out.println("\t[TYPE] "+typeOut+" [CLASS] "+classsOut);

                                double score = matchingDegree(classsIn, classsOut);

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
            //TODO skriv ut filer
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

    private double matchingDegree(String class1, String class2){
        if(class1.equalsIgnoreCase(class2))
            return 1.0;
        OWLClass owlClass1 = classes.get(class1.toLowerCase());
        OWLClass owlClass2 = classes.get(class2.toLowerCase());

        if(owlClass1 == null || owlClass2 == null)
            return 0.0;

//        System.out.println("[OWL1] "+owlClass1.toString()+" [OWL2] "+owlClass2.toString());

        if(owlClass1 == null || owlClass2 == null)
            return 0.0;
        else if(reasoner.isSameAs(owlClass1.asOWLIndividual(), owlClass2.asOWLIndividual()))
            return 1.0;
        else if(reasoner.isSubClassOf(owlClass1, owlClass2))
            return 0.8;
        else if(reasoner.isSubClassOf(owlClass2,owlClass1))
            return 0.6;
        else if(myOntManager.findRelationship(owlClass1,owlClass2, reasoner).size() > 0)
            return 0.5;


        return 0.0;
    }
}
