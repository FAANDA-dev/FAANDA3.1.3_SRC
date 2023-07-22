/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

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
public class FlightPlanOutput extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
  
        public FlightPlanOutput(Config config) {
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
                            return "urn:us:gov:dot:faa:atm:tfm:flightdatacommonmessage";
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
                        String row;
                        // extract data and format to row of csv
                         
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



                        
//stops
            //            row = row + "," + eElement.getElementsByTagName("nxcm:nextEvent latitudeDecimal").item(0).getTextContent();


                 //       row = row + "," + eElement.getElementsByTagName("nxce:latitudeDMS").item(0).getTextContent();
                      //  row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();

                       // row = row + "," + eElement.getAttribute("direction");
                     //   row = row + "," + eElement.getAttribute("minutes");
                       // row = row + "," + eElement.getAttribute("seconds");
                   
                   //     row = row + "," + eElement.getElementsByTagName("nxce:longitudeDMS degrees").item(0).getTextContent();
                   //     row = row + "," + eElement.getAttribute("direction");
                 //       row = row + "," + eElement.getAttribute("minutes");
               //         row = row + "," + eElement.getAttribute("seconds");
                //          row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();                    
                  //      row = row + "," + eElement.getElementsByTagName("nxcm:eta etaType").item(0).getTextContent();
                     //   row = row + "," + eElement.getAttribute("timeValue");
                  //      row = row + "," + eElement.getElementsByTagName("nxcm:rvsmData currentCompliance=").item(0).getTextContent();
                       // row = row + "," + eElement.getAttribute("equipped");
                    //    row = row + "," + eElement.getAttribute("futureCompliance");
                 //       row = row + "," + eElement.getElementsByTagName("nxcm:nextEvent latitudeDecimal").item(0).getTextContent();
                      //  row = row + "," + eElement.getAttribute("longitudeDecimal");

                   
          
                            
           
                
                  
                        String routeOfFlight = "";
                /*        NodeList flightRouteNodes = eElement.getElementsByTagName("nxcm:routeOfFlight");
                        for (int j = 0; j < flightRouteNodes.getLength(); j++) {
                            if (flightRouteNodes.item(j).getTextContent().length() > 0) {
                                routeOfFlight = flightRouteNodes.item(j).getTextContent();
                                break;
                            }
                        }

                        */
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
       /*  if (this.config.getBoolean("headers")) {
            logger.info(this.convert(header) + this.convert(message));
        } else {
            logger.info(this.convert(message));
        }
        */
    }
  

  
}
    





