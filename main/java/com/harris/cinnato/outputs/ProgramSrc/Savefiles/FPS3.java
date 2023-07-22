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





/**
 * Outputs the messages to a single rotating file log located in `./log/messages.log`
 */
public class FlightPlanStats3 extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
    private String nbsp;
  
        public FlightPlanStats3(Config config) {
            super(config);
            outputFile = new File("./log/FlightPlans.csv");
            
            // create the CSV file
            try {
                if(!outputFile.createNewFile()) {
                    outputFile.delete();
                    outputFile.createNewFile();
                }
                
                // add message headers
                FileWriter myWriter = new FileWriter(outputFile);
                myWriter.write("# Flight Plans #\n"); // first message header
               
                myWriter.write("acid,airline,dep,arr,igtd,route\n"); // column headers
                myWriter.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    
    
   

    private Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes()));
  
    }
   //phase 3
   

   private String getFlightPlanData(String message) {
    String returnString = "";
    try {
        Document document = loadXMLFromString(message);
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

        // iterate over individual messages
        String expression = "//fdm:fltdMessage";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                // check if message is flight plan message
             if (eElement.getAttribute("msgType").equals("flightPlanInformation")) {
                String row = "";
                 
                row = eElement.getAttribute("acid");
                row = row + "," + eElement.getAttribute("airline");
                row = row + "," + eElement.getAttribute("arrArpt");
                row = row + "," + eElement.getAttribute("depArpt");
                row = row + "," + eElement.getAttribute("fdTrigger");
                row = row + "," + eElement.getAttribute("flightRef");
                row = row + "," + eElement.getAttribute("major");
                row = row + "," + eElement.getAttribute("msgType");
                row = row + "," + eElement.getAttribute("sensitivity");
                row = row + "," + eElement.getAttribute("sourceFacility");
                row = row + "," + eElement.getAttribute("sourceTimeStamp");
                /*
                //blank next 2
                row = row + "," + eElement.getAttribute("aircraftCategory");
                row = row + "," + eElement.getAttribute("userCategory");

               
                row = row + "," + eElement.getElementsByTagName("nxce:facilityIdentifier").item(0).getTextContent();
                row = row + "," + eElement.getElementsByTagName("nxce:idNumber").item(0).getTextContent();
                row = row + "," + eElement.getElementsByTagName("nxce:gufi").item(0).getTextContent();
                row = row + "," + eElement.getElementsByTagName("nxce:igtd").item(0).getTextContent();
                row = row + "," + eElement.getElementsByTagName("nxce:airport").item(0).getTextContent();
                row = row + "," + eElement.getElementsByTagName("nxce:airport").item(0).getTextContent();
                row = row + "," + eElement.getElementsByTagName("nxcm:speed").item(0).getTextContent();
               
                row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();


/* */

                 //child node start  
                String expressionchild = "//nxcm:qualifiedAircraftId";
                NodeList childNodes = (NodeList) xPath.compile(expressionchild).evaluate(eElement, XPathConstants.NODESET);
                // Obtain the child nodes
                     Node childNode = childNodes.item(i);
                    // Check if the child node is an element node
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        // Access the child element here
                        Element trackInformation = (Element) childNode;
                        row = row + "," + trackInformation.getAttribute("aircraftCategory");
                        row = row + "," + trackInformation.getAttribute("userCategory");
                       
                        
                       }

                       

                 //child node start  
                 String expressionchild2 = "//nxce:longitudeDMS";
                 NodeList childNodes2 = (NodeList) xPath.compile(expressionchild2).evaluate(eElement, XPathConstants.NODESET);
                 // Obtain the child nodes
              /* */        Node childNode2 = childNodes2.item(i);
                     // Check if the child node is an element node
                     if (childNode2.getNodeType() == Node.ELEMENT_NODE) {
                         // Access the child element here
                         Element longitudeElement = (Element) childNode2;
                         /*
                         row = row + "," + longitudeElement.getAttribute("degrees");
                         row = row + "," + longitudeElement.getAttribute("direction");
                         row = row + "," + longitudeElement.getAttribute("minutes");  
                         */

                        // Thread.sleep(1000);
                      //   row = row + ", " + longitudeElement.getAttribute("seconds");   
                         
                        }



                 //child node start  
                 String expressionchild3 = "//nxcm:timeAtPosition";
                 NodeList childNodes3 = (NodeList) xPath.compile(expressionchild3).evaluate(eElement, XPathConstants.NODESET);
                 // Obtain the child nodes
                      Node childNode3 = childNodes3.item(i);
                     // Check if the child node is an element node
                     if (childNode3.getNodeType() == Node.ELEMENT_NODE) {
                         // Access the child element here
                         Element timeAtPosition = (Element) childNode3;
                         row = row + "," + timeAtPosition.getAttribute("nxce:timeAtPosition");                       
                        }









                

                // extract data and format to row of csv
                String routeOfFlight = "";
                /* 
                NodeList flightRouteNodes = eElement.getElementsByTagName("nxcm:routeOfFlight");
                for (int j = 0; j < flightRouteNodes.getLength(); j++) {
                    if (flightRouteNodes.item(j).getTextContent().length() > 0) {
                        routeOfFlight = flightRouteNodes.item(j).getTextContent();
                        break;
                    }
                }
             */
                row = row + " " + routeOfFlight + "\n";
                returnString = returnString + row; // append row to return string

            }
            }
        }
    } catch (Exception e) {
        logger.error("Error parsing message!!!!", e);
    }
    return returnString;
}



    @Override
    public void output(String message, String header) {
        String rows = getFlightPlanData(message);
        if(rows.length() > 0){
           try{
            FileWriter myWriter = new FileWriter(outputFile, true);
            myWriter.append(rows);
            myWriter.close();
           }catch (Exception e){
            logger.error(e.getMessage());
           }
                    
        }
     
    }
  

  
}
    





