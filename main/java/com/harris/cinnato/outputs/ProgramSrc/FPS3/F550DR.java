package com.harris.cinnato.outputs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JToolBar.Separator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


    
public class F550DR extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
    private final Set<String> uniqueRecords;
    String row = "";

    public F550DR(Config config) {
        super(config);
        outputFile = new File("./log/FlightPlansDataX.csv");
        uniqueRecords = new HashSet<>();

        try {
            if (!outputFile.createNewFile()) {
                outputFile.delete();
                outputFile.createNewFile();
            }

            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write("# Flight Plans #\n"); // first message header
            myWriter.write("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,\n");
            myWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }



private Document loadXMLFromFile() throws Exception {
    File directory = new File("./log/xml/");
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

    // Sort the files in ascending order
    Arrays.sort(files, Comparator.comparingLong(File::lastModified));

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document lastDocument = null;

    for (File file : files) {
        if (file.isFile()) {
            lastDocument = builder.parse(file);
            // Process the document as needed
        }
    }

    return lastDocument;
}



    private String getFlightPlanData(String message) {
        StringBuilder returnString = new StringBuilder();

        try {
            Document document = loadXMLFromFile();
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new NamespaceContext() {
                

  @Override
            public Iterator<String> getPrefixes(String arg0) {
                return null;
            }

            @Override
            public String getPrefix(String arg0) {
                return null;
            }

            @Override
            public String getNamespaceURI(String arg0) {
                switch (arg0) {
                    case "df":
                        return "urn:us:gov:dot:faa:atm:tfm:tfmdataservice";
                    case "fdm":
                        return "urn:us:gov:dot:faa:atm:tfm:flightdata";
                    case "nxce":
                        return "urn:us:gov:dot:faa:atm:tfm:tfmdatacoreelements";
                    case "nxcm":
                        return "urn:us:gov:dot:faa:atm:tfm:flightdatacommonmessages";
                    default:
                        return null;
                }
            }
        });

            

            String expression = "//fdm:fltdMessage";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (eElement.getAttribute("msgType").equals("trackInformation")) {
                        row = eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();


                       
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing message: ", e);
        }

        return returnString.toString();
    }

    @Override
    public void output(String message, String header) {
        String rows = getFlightPlanData(message);

        if (rows.length() > 0) {
            try {
                FileWriter myWriter = new FileWriter(outputFile, true);
                myWriter.append(rows);
                myWriter.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}

