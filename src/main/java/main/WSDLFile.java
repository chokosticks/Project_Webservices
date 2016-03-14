package main;


import com.predic8.schema.ComplexType;
import com.predic8.schema.Schema;
import com.predic8.schema.SimpleType;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by antondahlin on 2016-03-03.
 */
public class WSDLFile {

    private WSDLParser WSDLParser;
    private SAWSDLReader sawsdlReader;
    private Document doc;
    private String filename;
    private Definitions definitions;
    private Service service;
    private String namespace = "http://www.w3.org/2001/XMLSchema";

    private List<SimpleType> simpleTypes = new ArrayList<SimpleType>();
    private List<ComplexType> complexTypes = new ArrayList<ComplexType>();
    private HashMap<String, String> mapNameType = new HashMap<String, String>();
    private Schema schema;
    private HashMap<String, String> attributes = new HashMap<String, String>();
    private HashMap<String, String> classes = new HashMap<String, String>();
    private HashMap<String, String> types = new HashMap<String, String>();

    public WSDLFile(String path) {
        try {
            WSDLParser = new WSDLParser();
            System.out.println("[Path] "+path);

            definitions = WSDLParser.parse(new FileInputStream(path));
            schema = definitions.getSchemas().get(0);
            service = definitions.getLocalServices().get(0);
            filename = path;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(filename);

            NodeList simpleTypes = doc.getElementsByTagName("xsd:simpleType");
            NodeList complexTypes = doc.getElementsByTagName("xsd:complexType");


            for(int i = 0; i < simpleTypes.getLength(); i++){
                String name = simpleTypes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                String modelReference = simpleTypes.item(i).getAttributes().getNamedItem("sawsdl:modelReference").getTextContent();
                String classs = null;
                if(modelReference != null) {
                    classs = extractClass(modelReference);
                }
                System.out.println("[NAME] "+name+" [CLASS] "+classs);
                classes.put(name, classs);

            }
            for(int i = 0; i < complexTypes.getLength(); i++){
                String name = complexTypes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                String modelReference = complexTypes.item(i).getAttributes().getNamedItem("sawsdl:modelReference").getTextContent();
                String classs = null;
                if(modelReference != null) {
                    classs = extractClass(modelReference);
                }
                System.out.println("[NAME] "+name+" [CLASS] "+classs);
                classes.put(name, classs);
            }

            NodeList parts = doc.getElementsByTagName("wsdl:part");

            for(int i = 0; i < parts.getLength(); i++){
                String name = parts.item(i).getAttributes().getNamedItem("name").getNodeValue();
                String type = parts.item(i).getAttributes().getNamedItem("type").getNodeValue();

                System.out.println("\t[NAME] "+name+" [TYPE] "+type);

                types.put(name, type);
            }

            System.out.println("\n\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractClass(String modelReference){
        return modelReference.substring(modelReference.indexOf("#")+1, modelReference.length());
    }

    public Service getService()
    {
        return service;
    }

    public Definitions getDefinitions()
    {
        return definitions;
    }

    public void setWSDLParser(com.predic8.wsdl.WSDLParser WSDLParser)
    {
        this.WSDLParser = WSDLParser;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public void setDefinitions(Definitions definitions)
    {
        this.definitions = definitions;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public String getFilename()
    {
        return filename;
    }

    public WSDLParser getWSDLParser()
    {
        return WSDLParser;
    }

    public HashMap<String, String> getClasses(){
        return classes;
    }

    public HashMap<String, String> getTypes(){
        return types;
    }


}
