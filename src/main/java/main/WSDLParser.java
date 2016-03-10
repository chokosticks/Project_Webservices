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
        WSMatchingType wmt = new WSMatchingType();

        for(WSDLFile w1: WSDLManager.getInstance().getWsdlFiles()) {
            for(WSDLFile w2: WSDLManager.getInstance().getWsdlFiles()) {
                if(w1.equals(w2)) continue;

                MatchedWebServiceType bla = match(w1, w2);
                if(bla != null)
                    wmt.getMacthing().add(bla);
            }
        }
    }

    private MatchedWebServiceType match(WSDLFile w1, WSDLFile w2) {
        MatchedWebServiceType wmt = new MatchedWebServiceType();
        wmt.setInputServiceName(w1.getService().getName());
        wmt.setOutputServiceName(w2.getService().getName());

        double serviceScore = 0.0; int operationsCount = 0;

        // Service > Port > Binding > Port type > Operation > Message > Part > Types
        // Get all port types from W1
        for(PortType inPortType: w1.getDefs().getPortTypes()) {
            // For each operation in portType of W1
            for(Operation inOp: inPortType.getOperations()) {
                MatchedOperationType operation = new MatchedOperationType();

                double opScore = 0.0; int elementCount = 0;
                operation.setInputOperationName(inOp.getName());
                Message inMessage = inOp.getInput().getMessage();

                // For each part of the message W1
                for(Part inPart: inMessage.getParts()) {
                    if(!isSimple(inPart)) continue;
                    String s1 = inPart.getName();

                    // Compare it with all output operations of W2, ffs
                    for(PortType outPortType: w2.getDefs().getPortTypes()) {
                        // Deja vu
                        for (Operation outOp: outPortType.getOperations()) {
                            operation.setOutputOperationName(outOp.getName());
                            Message outMessage = outOp.getOutput().getMessage();

                            for(Part outPart: outMessage.getParts()) {
                                if(!isSimple(outPart)) continue;

                                String s2 = outPart.getName();

                                double score = wm.calculateScore(s1, s2);

                                if(score >= LOWERBOUND) {
                                    opScore += score;
                                    elementCount++;

                                    MatchedElementType met = new MatchedElementType();
                                    met.setInputElement(s1);
                                    met.setOutputElement(s2);
                                    met.setScore(score);

                                    operation.getMacthedElement().add(met);
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
                    wmt.getMacthedOperation().add(operation);
                }
            }
        }
        if(!wmt.getMacthedOperation().isEmpty()) {
            double serviceFinalScore = serviceScore / operationsCount;
            wmt.setWsScore(serviceFinalScore);

            // Print some shit
            System.out.println("MATCHES FOUND FOR SERVICE : " + wmt.getInputServiceName());
            System.out.println("MATCHED WITH : " + wmt.getOutputServiceName());
            for(MatchedOperationType op: wmt.getMacthedOperation()) {
                System.out.println("--Operation: " + op.getInputOperationName());
                System.out.println("++Operation: " + op.getOutputOperationName());
                for(MatchedElementType el: op.getMacthedElement())
                    System.out.println("==== " + el.getInputElement() + " <-> " + el.getOutputElement() + "(" + el.getScore() + ")");
            }
        }
        else
            return null;

        return wmt;
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
