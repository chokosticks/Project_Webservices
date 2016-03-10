package main;


import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class WSDLFile {

    private String filename;
    private WSDLParser parser;
    private Definitions defs;
    private Service service;

    public WSDLFile(String path) {
        try {
            parser = new WSDLParser();
            defs = parser.parse(new FileInputStream(path));
            service = defs.getLocalServices().get(0);
            filename = path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public WSDLParser getParser() {
        return parser;
    }

    public Definitions getDefs() {
        return defs;
    }

    public Service getService() {
        return service;
    }

    public String getFilename() { return filename; }
}
