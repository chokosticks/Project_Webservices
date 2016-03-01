package java;

import java.io.*;
import javax.xml.bind.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import applicantprofile.*;

public class main {
    public static void main(String[] args){

        try {
            //Parse the employment record with SAX

//            InputStream input = getClass().getResourceAsStream("/classpath/to/my/file");

            ApplicantProfile profile = new ApplicantProfile();
            Parsers parsers = new Parsers();

            profile = parsers.parseJAXB("/companyinfo1.xml", profile);


            //Output the application profile using jaxb
            JAXBContext jaxbCtx = JAXBContext.newInstance(profile.getClass().getPackage().getName());
            Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String s = main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            System.out.println(new File(s + "/applicationProfileOutput.xml").getAbsolutePath());
            s =  s.substring(0,s.lastIndexOf("/"));
            OutputStream os = new FileOutputStream(s + "/applicationProfileOutput.xml" );
            marshaller.marshal( profile, os );
            os.close();


//            filePath = filePath.concat("src\\main\\resources\\applicationProfileOutput.xml");
//            System.out.println(filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public <T> T unmarshal(String packageName, InputStream inputStream )
            throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance( packageName );
        Unmarshaller u = jc.createUnmarshaller();
        JAXBElement<T> doc = (JAXBElement<T>)u.unmarshal( inputStream );
        return doc.getValue();
    }
}
