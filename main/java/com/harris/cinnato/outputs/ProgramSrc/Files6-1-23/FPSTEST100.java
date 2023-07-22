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



//****************************note read from file log only!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */

/**
 * Outputs the messages to a single rotating file log located in `./log/messages.log`
 */
public class FPSV201 extends Output {
    private static final String fileName = "FlightPlanData.log";
    private static final Logger logger = LoggerFactory.getLogger(fileName);
    String row = "";  
    String sec = "";
    String Failed = ""; 
   // private static final Logger logger = LoggerFactory.getLogger("stdout");
    private final File outputFile;
    //private final File fileLog;

    
  
        public FPSV201(Config config) {
            super(config);

                     
           // fileLog = new File("./log/FlightPlanData.log");
            
            
            outputFile = new File("./log/FlightPlansC1A.csv");
             // create the CSV file
             try {
                if(!outputFile.createNewFile()) {
                    outputFile.delete();
                    outputFile.createNewFile();
                }
           
                
                // add message headers
                FileWriter myWriter = new FileWriter(outputFile);
                myWriter.write("# Flight Plans #\n"); // first message header
               
              //  myWriter.write(",acid,airline,arrArpt,cdmPart,depArpt,fdTrigger,flightRef,major,msgType,sensitivity,sourceFacility,sourceTimeStamp,aircraftCategory,userCategory,nxce:aircraftId,nxce:facilityIdentifier,ncxe:idNumber,nxce:gufi,nxce:igtd,nxce:departurePoint,nxce:arrivalPoint,nxcm:speed,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition,etaType,timeValue,currentCompliance,equipped,futureCompliance,arrival_arrTime,fixName,depart_arrTime,fixName,latitudeDecimail,longitudeDecimal,routOfFlight\n"); // column headers
                myWriter.write("nxce:aircraftId,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition\n");//colum headers 

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
                    row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();
                   
                    // Process child node 'latitudeDMS'
                    String expressionChild5 = ".//nxce:latitudeDMS";
                    NodeList childNodes5 = (NodeList) xPath.compile(expressionChild5).evaluate(eElement, XPathConstants.NODESET);
                    Failed = "False";
                    // Iterate over child nodes
                    for (int j = 0; j < childNodes5.getLength(); j++) {
                        Node nNode5 = childNodes5.item(j);
                        if (nNode5.getNodeType() == Node.ELEMENT_NODE) {
                            Element latitudeElement = (Element) nNode5;
                            row = row + "," + latitudeElement.getAttribute("degrees");
                            row = row + "," + latitudeElement.getAttribute("direction");
                            row = row + "," + Failed;
                            
                            row = row + "," + latitudeElement.getAttribute("seconds");
                            sec = latitudeElement.getAttribute("seconds");
                            if(sec == null)
                            {
                                Failed = "True";
                                System.out.println(sec);

                              //  row = row + " " + Failed + "\n";
                                returnString = returnString + Failed; // append row to return string


                            }
                            
                        }
                    }


                    // Process child node 'longitudeDMS'
                    String expressionChild4 = ".//nxce:longitudeDMS";
                    NodeList childNodes4 = (NodeList) xPath.compile(expressionChild4).evaluate(eElement, XPathConstants.NODESET);

                    // Iterate over child nodes
                    for (int j = 0; j < childNodes4.getLength(); j++) {
                        Node nNode2 = childNodes4.item(j);
                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                            Element longitudeElement = (Element) nNode2;
                            row = row + "," + longitudeElement.getAttribute("degrees");
                            row = row + "," + longitudeElement.getAttribute("direction");
                            row = row + "," + longitudeElement.getAttribute("minutes");
                            row = row + "," + longitudeElement.getAttribute("seconds");
                        }
                    }


                    //row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
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
        // Handle the exception
        logger.error("Error parsing message: ", e);
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
    





