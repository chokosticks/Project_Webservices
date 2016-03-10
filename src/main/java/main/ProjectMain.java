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
        main.WSDLParser wsdlParser = new main.WSDLParser();
    }
}
