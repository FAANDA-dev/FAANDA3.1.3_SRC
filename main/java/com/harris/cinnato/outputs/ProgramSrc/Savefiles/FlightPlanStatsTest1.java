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
public class FlightPlanStats12 extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
    private String nbsp;
  
        public FlightPlanStats12(Config config) {
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
               
                myWriter.write("acid,airline,arrArpt,cdmPart,depArpt,fdTrigger,flightRef,major,msgType,sensitivity,sourceFacility,sourceTimeStamp,aircraftCategory,userCategory,nxce:aircraftId,nxce:facilityIdentifier,ncxe:idNumber,nxce:gufi,nxce:igtd,nxce:departurePoint,nxce:arrivalPoint,nxcm:speed,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition,etaType,timeValue,currentCompliance,equipped,futureCompliance,arrival_arrTime,fixName,depart_arrTime,fixName,latitudeDecimail,longitudeDecimal,routOfFlight\n"); // column headers
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
             if (eElement.getAttribute("msgType").equals("trackInformation")) {
                String row = "";
                 
              //  row = eElement.getAttribute("acid");
               // row = row + "," + eElement.getAttribute("airline");
              
           


                 //child node start  
                String expressionchild = "//nxcm:qualifiedAircraftId";
                NodeList childNodes = (NodeList) xPath.compile(expressionchild).evaluate(eElement, XPathConstants.NODESET);
                // Obtain the child nodes
                     Node childNode = childNodes.item(i);
                    // Check if the child node is an element node
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        // Access the child element here
                        Element trackInformation = (Element) childNode;
                        String  CheckCategory = "";
                        CheckCategory = trackInformation.getAttribute("aircraftCategory");                                           
                      if("OTHER".equals(CheckCategory))                  
                        {
                         System.out.println("*************************Other***************");
                         System.exit(0);
  
                        }
               
                        

                       }

                             
                
                // extract data and format to row of csv
                String routeOfFlight = "";
                 
             
                row = row + "," + routeOfFlight + "\n";
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
    





