package main;


import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WSDLFile {

    private WSDLParser WSDLParser;
    private String filename;
    private Definitions definitions;
    private Service service;

    public WSDLFile(String path) {
        try {
            WSDLParser = new WSDLParser();
            System.out.println("[Path] "+path);
            definitions = WSDLParser.parse(new FileInputStream(path));
            service = definitions.getLocalServices().get(0);
            filename = path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
}
