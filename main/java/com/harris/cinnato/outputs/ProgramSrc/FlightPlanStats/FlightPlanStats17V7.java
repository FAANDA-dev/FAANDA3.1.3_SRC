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
public class FlightPlanStats17 extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
    private String nbsp;
  
        public FlightPlanStats17(Config config) {
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


                 //child node start  
                String expressionchildcheck = "//nxcm:qualifiedAircraftId";
                NodeList childNodescheck = (NodeList) xPath.compile(expressionchildcheck).evaluate(eElement, XPathConstants.NODESET);
                // Obtain the child nodes
                     Node childNodecheck = childNodescheck.item(i);
                    // Check if the child node is an element node
                    if (childNodecheck.getNodeType() == Node.ELEMENT_NODE) {
                        // Access the child element here
                        Element trackInformationcheck = (Element) childNodecheck;
                        String  CheckCategory = "";
                        CheckCategory = trackInformationcheck.getAttribute("aircraftCategory");                                           
                      if("OTHER".equals(CheckCategory))                  
                        {
                   /*****************************************Start of record 2************************************************************************* */         
                        
                       // row = "-----";
                        row = row + "," + eElement.getAttribute("acid");
                        row = row + "," + eElement.getAttribute("airline");
                        row = row + "," + eElement.getAttribute("cdmPart");
                        row = row + "," + eElement.getAttribute("fdTrigger");
                        row = row + "," + eElement.getAttribute("flightRef");
                        row = row + "," + eElement.getAttribute("major");
                        row = row + "," + eElement.getAttribute("msgType");
                        row = row + "," + eElement.getAttribute("sensitivity");
                        row = row + "," + eElement.getAttribute("sourceFacility");
                        row = row + "," + eElement.getAttribute("sourceTimeStamp");
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

                        row = row + "," + eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();
                        row = row + "," + eElement.getElementsByTagName("nxce:facilityIdentifier").item(0).getTextContent();
                        row = row + "," + eElement.getElementsByTagName("nxce:idNumber").item(0).getTextContent();
                        row = row + "," + eElement.getElementsByTagName("nxcm:speed").item(0).getTextContent();
                        row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();

     
                        String expressionchild2B = "//nxce:latitudeDMS";
                        NodeList childNodes2B = (NodeList) xPath.compile(expressionchild2B).evaluate(eElement, XPathConstants.NODESET);
                        // Obtain the child nodes
                           Node childNode2B = childNodes2B.item(i);
                            // Check if the child node is an element node
                            if (childNode2B.getNodeType() == Node.ELEMENT_NODE) {
                                // Access the child element here
                                Element latitudeElement = (Element) childNode2B;
                                row = row + "," + latitudeElement.getAttribute("degrees");
                                row = row + "," + latitudeElement.getAttribute("direction");
                                row = row + "," + latitudeElement.getAttribute("minutes");
                               row = row + "," + latitudeElement.getAttribute("seconds");
                               
                               }
  
                   

                //child node start  
                String expressionchild4 = "//nxce:longitudeDMS";
                NodeList childNodes4 = (NodeList) xPath.compile(expressionchild4).evaluate(eElement, XPathConstants.NODESET);
                // Obtain the child nodes
                       Node childNode4 = childNodes4.item(i);
                    // Check if the child node is an element node
                    if (childNode4.getNodeType() == Node.ELEMENT_NODE) {
                        // Access the child element here
                        Element longitudeElement = (Element) childNode4;
                        row = row + "," + longitudeElement.getAttribute("degrees");
                        row = row + "," + longitudeElement.getAttribute("direction");
                        row = row + "," + longitudeElement.getAttribute("minutes");                                             
                        row = row + ", " + longitudeElement.getAttribute("seconds");   
                        
                       }
                       row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                       String expressionchild7 = "//nxcm:rvsmData";
                       NodeList childNodes7 = (NodeList) xPath.compile(expressionchild7).evaluate(eElement, XPathConstants.NODESET);
                       // Obtain the child nodes
                            Node childNode7 = childNodes7.item(i);
                           // Check if the child node is an element node
                           if (childNode7.getNodeType() == Node.ELEMENT_NODE) {
                               // Access the child element here
                               Element ncsmTrackData = (Element) childNode7;
                               row = row + "," + ncsmTrackData.getAttribute("currentCompliance");
                               row = row + "," + ncsmTrackData.getAttribute("equipped");
                               row = row + "," + ncsmTrackData.getAttribute("futureCompliance");                                                                                    
                              
                              }

                        // end of record number 2
                        row = row + ",";
                        returnString = returnString + row; // append row to return string
                        return returnString;
                        }
                    }
           
/********************************Start record 1**************************************************************************************************** */

               row = row + "," + eElement.getAttribute("acid");
               row = row + "," + eElement.getAttribute("airline");
                 
               
               
                row = row + "," + eElement.getAttribute("arrArpt");
                row = row + "," + eElement.getAttribute("cdmPart");
                row = row + "," + eElement.getAttribute("depArpt");
                row = row + "," + eElement.getAttribute("fdTrigger");
                row = row + "," + eElement.getAttribute("flightRef");
                row = row + "," + eElement.getAttribute("major");
                row = row + "," + eElement.getAttribute("msgType");
                row = row + "," + eElement.getAttribute("sensitivity");
                row = row + "," + eElement.getAttribute("sourceFacility");
                row = row + "," + eElement.getAttribute("sourceTimeStamp");
              


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
                  String expressionchild2 = "//fdm:fltdMessage";
                  NodeList childNodes2 = (NodeList) xPath.compile(expressionchild2).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                       Node childNode2 = childNodes2.item(i);
                      // Check if the child node is an element node
                      if (childNode2.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element eElement2 = (Element) childNode;
                         // row = row + "," + eElement2.getAttribute("aircraftCategory");
                          //row = row + "," + trackInformation.getAttribute("userCategory");
                          row = row + "," + eElement2.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();
                          row = row + "," + eElement2.getElementsByTagName("nxce:facilityIdentifier").item(0).getTextContent();
                          row = row + "," + eElement2.getElementsByTagName("nxce:idNumber").item(0).getTextContent();
                          row = row + "," + eElement2.getElementsByTagName("nxce:gufi").item(0).getTextContent();
                          row = row + "," + eElement2.getElementsByTagName("nxce:igtd").item(0).getTextContent();
                          //depart
                        //  row = row + "," + eElement2.getElementsByTagName("nxce:airport").item(0).getTextContent();
                         
                         }


                        //child node start  
                             String expressionchildX = "//nxce:departurePoint";
                             NodeList childNodesX = (NodeList) xPath.compile(expressionchildX).evaluate(eElement, XPathConstants.NODESET);
                             // Obtain the child nodes
                                  Node childNodeX = childNodesX.item(i);
                                 // Check if the child node is an element node
                                 if (childNodeX.getNodeType() == Node.ELEMENT_NODE) {
                                     // Access the child element here
                                     Element departurePoint = (Element) childNodeX;
                                     //row = row + "," + timeAtPosition.getAttribute("nxcm:timeAtPosition"); 
                                    // row = row + "," + position.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                                      row = row + "," + departurePoint.getElementsByTagName("nxce:airport").item(0).getTextContent();
                                 }
                    





                             //child node start  
                             String expressionchildX1 = "//nxce:arrivalPoint";
                             NodeList childNodesX1 = (NodeList) xPath.compile(expressionchildX1).evaluate(eElement, XPathConstants.NODESET);
                             // Obtain the child nodes
                                  Node childNodeX1 = childNodesX1.item(i);
                                 // Check if the child node is an element node
                                 if (childNodeX1.getNodeType() == Node.ELEMENT_NODE) {
                                     // Access the child element here
                                     Element arrivalPoint = (Element) childNodeX1;
                                     //row = row + "," + timeAtPosition.getAttribute("nxcm:timeAtPosition"); 
                                    // row = row + "," + position.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                                     row = row + "," + arrivalPoint.getElementsByTagName("nxce:airport").item(0).getTextContent();
                                 }
                    



                  //child node start  
                 String expressionchildX2 = "//fdm:fltdMessage";
                 NodeList childNodesX2 = (NodeList) xPath.compile(expressionchildX2).evaluate(eElement, XPathConstants.NODESET);
                 // Obtain the child nodes
                      Node childNodeX2 = childNodesX2.item(i);
                     // Check if the child node is an element node
                     if (childNodeX2.getNodeType() == Node.ELEMENT_NODE) {
                         // Access the child element here
                         Element position = (Element) childNodeX2;
                         //row = row + "," + timeAtPosition.getAttribute("nxcm:timeAtPosition"); 
                        // row = row + "," + position.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                         row = row + "," + position.getElementsByTagName("nxcm:speed").item(0).getTextContent();
                     }





                 //child node start  
                String expressionchild2A = "//nxcm:reportedAltitude";
                NodeList childNodes2A = (NodeList) xPath.compile(expressionchild2A).evaluate(eElement, XPathConstants.NODESET);
                // Obtain the child nodes
                     Node childNode2A = childNodes2A.item(i);
                    // Check if the child node is an element node
                    if (childNode2A.getNodeType() == Node.ELEMENT_NODE) {
                        // Access the child element here
                        Element assignedAltitude = (Element) childNode2A;
                        //row = row + "," + trackInformation.getAttribute("aircraftCategory");
                        //row = row + "," + trackInformation.getAttribute("userCategory");
                       
                          row = row + "," + assignedAltitude.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();
                       //   row = row + "," + assignedAltitude.getElementsByTagName("nxcm:speed").item(0).getTextContent();


                       }

                
                         
                     
                         String expressionchild2B = "//nxce:latitudeDMS";
                         NodeList childNodes2B = (NodeList) xPath.compile(expressionchild2B).evaluate(eElement, XPathConstants.NODESET);
                         // Obtain the child nodes
                            Node childNode2B = childNodes2B.item(i);
                             // Check if the child node is an element node
                             if (childNode2B.getNodeType() == Node.ELEMENT_NODE) {
                                 // Access the child element here
                                 Element latitudeElement = (Element) childNode2B;
                                 row = row + "," + latitudeElement.getAttribute("degrees");
                                 row = row + "," + latitudeElement.getAttribute("direction");
                                 row = row + "," + latitudeElement.getAttribute("minutes");
                                row = row + "," + latitudeElement.getAttribute("seconds");
                                
                                }
        



                      

                 //child node start  
                 String expressionchild4 = "//nxce:longitudeDMS";
                 NodeList childNodes4 = (NodeList) xPath.compile(expressionchild4).evaluate(eElement, XPathConstants.NODESET);
                 // Obtain the child nodes
                        Node childNode4 = childNodes4.item(i);
                     // Check if the child node is an element node
                     if (childNode4.getNodeType() == Node.ELEMENT_NODE) {
                         // Access the child element here
                         Element longitudeElement = (Element) childNode4;
                         row = row + "," + longitudeElement.getAttribute("degrees");
                         row = row + "," + longitudeElement.getAttribute("direction");
                         row = row + "," + longitudeElement.getAttribute("minutes");                                             
                         row = row + ", " + longitudeElement.getAttribute("seconds");   
                         
                        }



                 //child node start  
                 String expressionchild5 = "//fdm:fltdMessage";
                 NodeList childNodes5 = (NodeList) xPath.compile(expressionchild5).evaluate(eElement, XPathConstants.NODESET);
                 // Obtain the child nodes
                      Node childNode5 = childNodes5.item(i);
                     // Check if the child node is an element node
                     if (childNode5.getNodeType() == Node.ELEMENT_NODE) {
                         // Access the child element here
                         Element position = (Element) childNode5;
                         //row = row + "," + timeAtPosition.getAttribute("nxcm:timeAtPosition"); 
                         row = row + "," + position.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                        // row = row + "," + position.getElementsByTagName("nxcm:speed").item(0).getTextContent();



                         
                        }


                          //child node start  
                  String expressionchild6 = "//nxcm:eta";
                  NodeList childNodes6 = (NodeList) xPath.compile(expressionchild6).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                       Node childNode6 = childNodes6.item(i);
                      // Check if the child node is an element node
                      if (childNode6.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element ncsmTrackData = (Element) childNode6;
                          row = row + "," + ncsmTrackData.getAttribute("etaType");
                          row = row + "," + ncsmTrackData.getAttribute("timeValue");
                         // row = row + "," + ncsmTrackData.getAttribute("etaType");



                          //row = row + "," + eta.getAttribute("timeValue");
                         
                          
                         }

                         String expressionchild7 = "//nxcm:rvsmData";
                         NodeList childNodes7 = (NodeList) xPath.compile(expressionchild7).evaluate(eElement, XPathConstants.NODESET);
                         // Obtain the child nodes
                              Node childNode7 = childNodes7.item(i);
                             // Check if the child node is an element node
                             if (childNode7.getNodeType() == Node.ELEMENT_NODE) {
                                 // Access the child element here
                                 Element ncsmTrackData = (Element) childNode7;
                                 row = row + "," + ncsmTrackData.getAttribute("currentCompliance");
                                 row = row + "," + ncsmTrackData.getAttribute("equipped");
                                 row = row + "," + ncsmTrackData.getAttribute("futureCompliance");
                                                                                                 
                                 
                                }

                                String expressionchild8 = "//nxcm:arrivalFixAndTime";
                                NodeList childNodes8 = (NodeList) xPath.compile(expressionchild8).evaluate(eElement, XPathConstants.NODESET);
                                // Obtain the child nodes
                                     Node childNode8 = childNodes8.item(i);
                                    // Check if the child node is an element node
                                    if (childNode8.getNodeType() == Node.ELEMENT_NODE) {
                                        // Access the child element here
                                        Element ncsmTrackData = (Element) childNode8;
                                        row = row + "," + ncsmTrackData.getAttribute("arrTime");
                                        row = row + "," + ncsmTrackData.getAttribute("fixName");
                                                                                        
                                        
                                       }
                                String expressionchild9 = "//nxcm:departureFixAndTime";
                                NodeList childNodes9 = (NodeList) xPath.compile(expressionchild9).evaluate(eElement, XPathConstants.NODESET);
                                // Obtain the child nodes
                                     Node childNode9 = childNodes9.item(i);
                                    // Check if the child node is an element node
                                    if (childNode9.getNodeType() == Node.ELEMENT_NODE) {
                                        // Access the child element here
                                        Element ncsmTrackData = (Element) childNode9;
                                        row = row + "," + ncsmTrackData.getAttribute("arrTime");
                                        row = row + "," + ncsmTrackData.getAttribute("fixName");
                                                                                        
                                        
                                       }

  

                  //child node start  
                  String expressionchild10 = "//nxcm:nextEvent";
                  NodeList childNodes10 = (NodeList) xPath.compile(expressionchild10).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                       Node childNode10 = childNodes10.item(i);
                      // Check if the child node is an element node
                      if (childNode10.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element ncsmTrackData = (Element) childNode10;
                          row = row + "," + ncsmTrackData.getAttribute("latitudeDecimal");
                          row = row + "," + ncsmTrackData.getAttribute("longitudeDecimal");
                         


                          //row = row + "," + eta.getAttribute("timeValue");
                         
                          
                         }
  
                

                // extract data and format to row of csv
                String routeOfFlight = "";
                 
                NodeList flightRouteNodes = eElement.getElementsByTagName("nxcm:routeOfFlight");
                for (int j = 0; j < flightRouteNodes.getLength(); j++) {
                    if (flightRouteNodes.item(j).getTextContent().length() > 0) {
                        routeOfFlight = flightRouteNodes.item(j).getTextContent();
                        break;
                    }
                }
             
                row = row + "," + routeOfFlight + "\n";
                returnString = returnString + row; // append row to return string
//********************************************************End of record 1************************************************************************************ */
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
    





