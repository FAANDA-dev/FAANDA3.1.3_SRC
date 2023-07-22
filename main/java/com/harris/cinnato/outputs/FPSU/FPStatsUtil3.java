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

public class FPStatsUtil3 extends Output {
    private final File outputFile;

    public FPStatsUtil3(Config config) {
        super(config);

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
            e.printStackTrace();
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
        File directory = new File("./log/xml/"); // Specify the directory path here
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (files != null) {
            for (File file : files) {
                try {
                    Document document = loadXMLFromFile(file);
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
                                row = eElement.getAttribute("acid");
                             
                     
                   row = row + "," + eElement.getAttribute("depArpt");
                   row = row + "," + eElement.getAttribute("arrArpt");
               
                    
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
          
                         row = row + "," + eElement.getElementsByTagName("nxcm:speed").item(0).getTextContent();
                         row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();
                         }

                
                        String expressionchildX31 = "//nxce:latitudeDMS";
                  NodeList childNodesX31 = (NodeList) xPath.compile(expressionchildX31).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                     Node childNodeX31 = childNodesX31.item(i);
                      // Check if the child node is an element node
                      if (childNodeX31.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element latitudeElement = (Element) childNodeX31;
                          row = row + "," + latitudeElement.getAttribute("degrees");
                          row = row + "," + latitudeElement.getAttribute("direction");
                          row = row + "," + latitudeElement.getAttribute("minutes");
                          row = row + "," + latitudeElement.getAttribute("seconds");
                    
                         }
              

          //child node start  
          String expressionchildX20 = "//nxce:longitudeDMS";
          NodeList childNodesX20 = (NodeList) xPath.compile(expressionchildX20).evaluate(eElement, XPathConstants.NODESET);
          // Obtain the child nodes
                 Node childNodeX20 = childNodesX20.item(i);
              // Check if the child node is an element node
              if (childNodeX20.getNodeType() == Node.ELEMENT_NODE) {
                  // Access the child element here
                  Element longitudeElement = (Element) childNodeX20;
                  row = row + "," + longitudeElement.getAttribute("degrees");
                  row = row + "," + longitudeElement.getAttribute("direction");
                  row = row + "," + longitudeElement.getAttribute("minutes");                                             
                  row = row + ", " + longitudeElement.getAttribute("seconds");   
                 
                 }

          
                
                row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();




                                row = row + "\n";
                                returnString = returnString + row; // append row to return string
                            }
                        }
                    }
        

                 } catch (Exception e) {
         //       logger.error("Error parsing file " + file.getName(), e);
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
           // logger.error(e.getMessage());
        }
    }
}

  
}
    





