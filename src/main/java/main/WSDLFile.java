package main;


import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WSDLFile {

    private String filename;
    private WSDLParser parser;
    private Definitions definitions;
    private Service service;

    public WSDLFile(String path) {
        try {
            parser = new WSDLParser();
            System.out.println("[Path] "+path);
            definitions = parser.parse(new FileInputStream(path));
            service = definitions.getLocalServices().get(0);
            filename = path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public WSDLParser getParser() {
        return parser;
    }

    public Definitions getDefinitions() {
        return definitions;
    }

    public Service getService() {
        return service;
    }

    public String getFilename() { return filename; }
}
