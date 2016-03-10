package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antondahlin on 2016-03-10.
 */
public class WSDLLoader {

    private List<WSDLFile> wsdlFiles = new ArrayList<WSDLFile>();

    public WSDLLoader(){

        File wsdls = new File("resources/WSDLs/");

        for(File file: wsdls.listFiles()){
            WSDLFile wsdlFile = new WSDLFile("resources/WSDLs/"+file.getName());
            wsdlFiles.add(wsdlFile);
        }

    }



}
