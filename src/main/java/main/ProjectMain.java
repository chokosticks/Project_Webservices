package main;

import com.predic8.schema.*;
import com.predic8.wsdl.*;
import com.predic8.wsdl.Input;
import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Output;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.Types;
import com.predic8.wsdl.WSDLParser;
import groovy.xml.QName;
import org.jdom.JDOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.List;


/**
 * Created by antondahlin on 2016-03-01.
 */
public class ProjectMain {


    public static void main(String[] args) throws JDOMException, IOException, ParserConfigurationException, SAXException {
        File wsdlFile = new File("/Users/antondahlin/IdeaProjects/Project_Webservices/src/main/resources/WSDLs/ACHWorksAPIProfile.wsdl");
        org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wsdlFile);

        NodeList list = document.getElementsByTagName("wsdl:operation");

        WSDLParser parser = new WSDLParser();

        Definitions defs = parser.parse("/Users/antondahlin/IdeaProjects/Project_Webservices/src/main/resources/WSDLs/LibertyReserveAPIProfile.wsdl");//ACHWorksAPIProfile.wsdl");


        List<Service> services = defs.getServices();


        for(Service srv: services){
            System.out.println(srv.getName());
        }

        System.out.println("\n\n");


        for(PortType pt: defs.getPortTypes()){
            System.out.println(pt.getName());
            for(Operation op: pt.getOperations()){

                System.out.println(" -- "+op.getName());
                Input input = op.getInput();
                Output output = op.getOutput();
                String inputOpName = input.getMessage().getQname().getLocalPart();
                String outputOpName = output.getMessage().getQName().getLocalPart();

                System.out.println("\t\t[Input ] "+inputOpName+"\n\t\t[Output] "+outputOpName);

                Message msg = defs.getMessage(inputOpName);
                List<Part> parts = msg.getParts();

                parts.addAll(defs.getMessage(outputOpName).getParts());

                if(parts.isEmpty())
                    continue;

                for(Part part: parts){
                    System.out.println("\t\t[NAME] "+part.getQName().getLocalPart());
                    System.out.println("\t\t[TYPE] "+part.getType());
                }

//                Types types = defs.getLocalTypes();



            }
        }
    }
}
