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

import org.apache.qpid.proton.amqp.messaging.Data;
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

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.Iterator;


public class FPStatsU5 extends Output {
    private final File outputFile;
    private final Logger logger;

    public FPStatsU5(Config config) {
        super(config);
        logger = LoggerFactory.getLogger(FPStatsU5.class);

        outputFile = new File("./log/FlightPlansData.csv");

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
    StringBuilder returnString = new StringBuilder();
    File directory = new File("./log/xml/");
    File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

    if (files != null) {
        int batchSize = 100; // Set the batch size as needed
        int fileCount = 0;
        int batchCount = 0;

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



//***************************************************************************************************************************************                
             // Iterate over individual messages
String row = "";
String expression = "//fdm:fltdMessage";
NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
for (int i = 0; i < nodeList.getLength(); i++) {
    Node nNode = nodeList.item(i);
    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;

        // Check if message is a flight plan message
        if (eElement.getAttribute("msgType").equals("trackInformation")) {
            row = eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();

            String expressionchildX3 = "//nxcm:qualifiedAircraftId";
            NodeList childNodesX3 = (NodeList) xPath.compile(expressionchildX3).evaluate(eElement, XPathConstants.NODESET);
            
            // Obtain the child nodes
            Node childNodeX3 = childNodesX3.item(i);
            
            // Check if the child node is an element node
            if (childNodeX3.getNodeType() == Node.ELEMENT_NODE) {
                // Access the child element here
                Element eElement2 = (Element) childNodeX3;                                                      
                row = row + "," + eElement2.getAttribute("aircraftCategory");
                row = row + "," + eElement2.getAttribute("userCategory");                                      
            }
         
            row = row + "," + eElement.getElementsByTagName("nxcm:speed").item(0).getTextContent();                
            row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();
            
        String expressionchildX31 = "//nxce:latitudeDMS";
NodeList childNodesX31 = (NodeList) xPath.compile(expressionchildX31).evaluate(eElement, XPathConstants.NODESET);

// Iterate over child nodes
   int k=1;
    Node childNodeX31 = childNodesX31.item(k);

    // Check if the child node is an element node
    if (childNodeX31.getNodeType() == Node.ELEMENT_NODE) {
        // Access the child element here
        Element latitudeElement = (Element) childNodeX31;
        row = row + "," + latitudeElement.getAttribute("degrees");
        row = row + "," + latitudeElement.getAttribute("direction");
        row = row + "," + latitudeElement.getAttribute("minutes");
        row = row + "," + latitudeElement.getAttribute("seconds");
    }

 
          
         
            // Child node start  
String expressionchildX20 = "//nxce:longitudeDMS";
NodeList childNodesX20 = (NodeList) xPath.compile(expressionchildX20).evaluate(eElement, XPathConstants.NODESET);

// Iterate over child nodes
    int j=1;
    Node childNodeX20 = childNodesX20.item(j);
 
    // Check if the child node is an element node
    if (childNodeX20.getNodeType() == Node.ELEMENT_NODE) {
        // Access the child element here
        Element longitudeElement = (Element) childNodeX20;
        row = row + "," + longitudeElement.getAttribute("degrees");
        row = row + "," + longitudeElement.getAttribute("direction");
        row = row + "," + longitudeElement.getAttribute("minutes");
        row = row + "," + longitudeElement.getAttribute("seconds");   
    }






                                
            row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
        }
    }
    
    if (!row.isEmpty()) {
        returnString.append(row);
        returnString.append("\n");
    }
}

            
                fileCount++;
                if (fileCount % batchSize == 0) {
                    // Write the batch to the CSV file
                    writeRowsToCSV(returnString.toString());
                    returnString.setLength(0);
                    batchCount++;
                }
            } catch (Exception e) {
                logger.error("Error parsing file " + file.getName(), e);
            }
        }

        // Write any remaining rows to the CSV file
        if (returnString.length() > 0) {
            writeRowsToCSV(returnString.toString());
            batchCount++;
        }

        logger.info("Processed " + fileCount + " files in " + batchCount + " batches.");
    }

    return returnString.toString();
}


   
    private void writeRowsToCSV(String rows) {
        try {
            FileWriter myWriter = new FileWriter(outputFile, true);
            myWriter.append(rows);
            myWriter.close();
        } catch (IOException e) {
            logger.error("Error writing to the CSV file", e);
        }
    }

    @Override
    public void output(String message, String header) {
        // Call the existing writeRowsToCSV() method
        writeRowsToCSV(getFlightPlanData());
    }
}
