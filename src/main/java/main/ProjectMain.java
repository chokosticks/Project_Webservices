package main;

import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by antondahlin on 2016-03-01.
 */
public class ProjectMain {


    public static void main(String[] args) throws JDOMException, IOException, ParserConfigurationException, SAXException {
        System.setProperty("wordnet.database.dir", "./src/main/resources/dict/");

//        WSDLParser wsdlParser = new WSDLParser();
//        wsdlParser.match();
//        wsdlParser.writeFile("task1");


        SAWSDLParser sawsdlParser = new SAWSDLParser();
        sawsdlParser.match();
        sawsdlParser.writeFile("task2");

    }
}
