package java;

import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by Rickard on 2016-02-05.
 */
public class Parsers
{

    public Parsers( )
    {

    }


//    public ApplicantProfile parseJAXB(String path, ApplicantProfile profile) throws FileNotFoundException, JAXBException
//    {
//        // create JAXB context and instantiate marshaller
//        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
//        Marshaller m = context.createMarshaller();
//        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        //Parse the company info document with jaxb
////        System.out.println(new File(".").getAbsolutePath());
//        InputStream inputStream = getClass().getResourceAsStream(path);
////        InputStream inputStream = getClass().getResourceAsStream("/companyinfo1.xml");
////        InputStream inputStream = new FileInputStream(path);
//
//        Unmarshaller um = context.createUnmarshaller();
//        Source source = new StreamSource(inputStream);
//        JAXBElement<CompanyinfoType> root = um.unmarshal(source, CompanyinfoType.class);
//        CompanyinfoType companyinfoType = root.getValue();
//        profile.setCompanyinfo(companyinfoType);
//        System.out.println("Company info: " + companyinfoType.getCompanyname() + " " + companyinfoType.getDescription());
//        return profile;
//    }

}
