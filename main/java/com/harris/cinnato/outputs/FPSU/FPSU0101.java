/*
 * Copyright (c) 2021 L3Harris Technologies
 */
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.namespace.NamespaceContext;

public class FPSU0101 extends Output {
    private final File outputFile;
    private final Logger logger;


    public FPSU0101(Config config) {
        super(config);
        logger = LoggerFactory.getLogger(FPSU0101.class);



        outputFile = new File("./log/FlightPlansDataX.csv");

        // create the CSV file
        try {
            if (!outputFile.createNewFile()) {
                outputFile.delete();
                outputFile.createNewFile();
            }

            // add message headers
            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write("# Flight Plans #\n"); // first message header
            myWriter.write("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,\n");// column headers
            myWriter.close();
        } catch (IOException e) {
            // Handle the exception
                logger.error("Error creating or writing to the CSV file", e);
            
        }
    }

     private Document loadXMLFromFile(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
 private String getFlightPlanData() {
    String returnString = "";
    File directory = new File("./log/xml/");
    File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

    if (files != null) {
        for (File file : files) {
            try {
                Document document = loadXMLFromFile(file);
                XPath xPath = XPathFactory.newInstance().newXPath();
               xPath.setNamespaceContext(new NamespaceContext() {
    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        return null; // TODO: Implement logic to return prefixes for the given namespaceURI
    }

    @Override
    public String getPrefix(String namespaceURI) {
        return null; // TODO: Implement logic to return the prefix for the given namespaceURI
    }

    @Override
    public String getNamespaceURI(String prefix) {
        switch (prefix) {
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

               
                

                String row = "";
                String expression = "//fdm:fltdMessage";
                NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nNode = nodeList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        if (eElement.getAttribute("msgType").equals("trackInformation")) {
                            row = eElement.getAttribute("acid");
                            row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                            row = row + "\n";
                            returnString = returnString + row;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error parsing file " + file.getName(), e);
            }
        }
    }

    return returnString;
}


   



 @Override
public void output(String message, String header) {
    String rows = getFlightPlanData();
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
    





