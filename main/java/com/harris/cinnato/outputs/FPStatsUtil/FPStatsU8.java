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



public class FPStatsU7 extends Output {
    private final File outputFile;
    private final Logger logger;

    public FPStatsU7(Config config) {
        super(config);
        logger = LoggerFactory.getLogger(FPStatsU7.class);

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
            
       //**************NEW deg!!! departure
 
   // Process child node 'departurePoint'
   String expressionChildX17 = ".//nxcm:position";
   NodeList childNodesX17 = (NodeList) xPath.compile(expressionChildX17).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFixDep = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX17.getLength(); j++) {
    Node nNodeX17 = childNodesX17.item(j);
    if (nNodeX17.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX17;

        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:latitude");
        if (descendants.getLength() > 0) {
            hasFixDep = true;
           
              row = row + "," + "lat";
             System.out.println(row);
            // Iterate over descendants
            for (int k = 0; k < descendants.getLength(); k++) {
                Node descendantNode = descendants.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

                    // Access further down nodes within the descendant element
                    NodeList furtherDescendants = descendantElement.getElementsByTagName("nxce:latitudeDMS");
                    for (int m = 0; m < furtherDescendants.getLength(); m++) {
                        Node furtherDescendantNode = furtherDescendants.item(m);
                        if (furtherDescendantNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element furtherDescendantElement = (Element) furtherDescendantNode;
                            row = row + "," + furtherDescendantElement.getAttribute("degrees");
                            row = row + "," + furtherDescendantElement.getAttribute("direction");
                            row = row + "," + furtherDescendantElement.getAttribute("minutes");   
                            System.out.println(row);

                            // Process further descendant elements as needed
                        }
                    }

                    NodeList furtherDescendants1 = descendantElement.getElementsByTagName("nxce:longitudeDMS");
                    for (int m = 0; m < furtherDescendants1.getLength(); m++) {
                        Node furtherDescendantNode1 = furtherDescendants1.item(m);
                        if (furtherDescendantNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element furtherDescendantElement1 = (Element) furtherDescendantNode1;
                            row = row + "," + furtherDescendantElement1.getAttribute("degrees");
                            row = row + "," + furtherDescendantElement1.getAttribute("direction");
                            row = row + "," + furtherDescendantElement1.getAttribute("minutes");   




                        }
                    }

                    if(hasFixDep)
                    {
                    
                    }



                }
            }
        }
    }
}



                                
            row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
        }
    }
    
    if (!row.isEmpty()) {
        returnString.append(row);
        returnString.append(",");
        returnString.append(file.getName());
        returnString.append("\n");
    }
}

            
                fileCount++;
                if (fileCount % batchSize == 0) {
                    // Write the batch to the CSV file
                    writeRowsToCSV(returnString.toString());
                    returnString.setLength(0);
                    batchCount++;
                }// Thread.sleep(500);

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
