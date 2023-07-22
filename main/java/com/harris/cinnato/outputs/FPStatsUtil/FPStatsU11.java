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


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
// Add the missing imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class FPStatsU11 extends Output {
    private final File outputFile;
    private final Logger logger;

    public FPStatsU11(Config config) {
        super(config);
        logger = LoggerFactory.getLogger(FPStatsU11.class);

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
            int batchSize = 50; // Set the batch size as needed
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

                    // Iterate over individual messages
                    String expression = "//fdm:fltdMessage[nxce:qualifiedAircraftId]";
                    NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node nNode = nodeList.item(i);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            // Check if message is a flight plan message
                            if (eElement.getAttribute("msgType").equals("trackInformation")) {
                                String row = eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();

                                Element qualifiedAircraftId = (Element) eElement.getElementsByTagName("nxcm:qualifiedAircraftId").item(0);
                                row += "," + qualifiedAircraftId.getAttribute("aircraftCategory");
                                row += "," + qualifiedAircraftId.getAttribute("userCategory");

                                row += "," + eElement.getElementsByTagName("nxcm:speed").item(0).getTextContent();
                                row += "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();

                                // Process latitude
                                Element latitudeElement = (Element) eElement.getElementsByTagName("nxce:latitudeDMS").item(0);
                                row += "," + latitudeElement.getAttribute("degrees");
                                row += "," + latitudeElement.getAttribute("direction");
                                row += "," + latitudeElement.getAttribute("minutes");
                                row += "," + latitudeElement.getAttribute("seconds");

                                // Process longitude
                                Element longitudeElement = (Element) eElement.getElementsByTagName("nxce:longitudeDMS").item(0);
                                row += "," + longitudeElement.getAttribute("degrees");
                                row += "," + longitudeElement.getAttribute("direction");
                                row += "," + longitudeElement.getAttribute("minutes");
                                row += "," + longitudeElement.getAttribute("seconds");

                                row += "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();

                                returnString.append(row);
                                returnString.append(",");
                                returnString.append(file.getName());
                                returnString.append("\n");
                            }
                        }
                    }

                    fileCount++;
                    if (fileCount % batchSize == 0) {
                        // Write the batch to the CSV file
                        writeRowsToCSV(returnString.toString());
                        returnString.setLength(0);
                        batchCount++;
                    }

                    // Move to the processed directory
                    File processedDirectory = new File("./log/xml/processed/");
                    File newLocation = new File(processedDirectory, file.getName());
                    if (!file.renameTo(newLocation)) {
                        logger.error("Error moving file " + file.getName() + " to the processed directory");
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