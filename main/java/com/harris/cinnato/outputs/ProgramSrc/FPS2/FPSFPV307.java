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
public class FPSFPV307 extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
      
    String row = "";  
    String sec = "";
    String FIX = "";
   
    
  
        public FPSFPV307(Config config) {
            super(config);
                 
                   
            
            outputFile = new File("./log/FlightPlansData11.csv");
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
                myWriter.write("acid,nxce:aircraftId,aircraftCategory,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,,direction,minutes,seconds,nxcm:timeAtPosition\n");//colum headers 

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
      //  String row = "";
        String expression = "//fdm:fltdMessage";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                // Check if message is a flight plan message
               if (eElement.getAttribute("msgType").equals("trackInformation")) {
                    
                   
                    row  = eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();


                 //   row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();
                   

 



                   // Process child node 'qualifiedAircraftId'
                  String expressionChildX = ".//nxcm:qualifiedAircraftId";
                  NodeList childNodesX = (NodeList) xPath.compile(expressionChildX).evaluate(eElement, XPathConstants.NODESET);

                  // Check if any descendants are found
                  boolean hasDescendants = false;

                  // Iterate over child nodes
                 for (int j = 0; j < childNodesX.getLength(); j++) {
                     Node nNodeX = childNodesX.item(j);
                     if (nNodeX.getNodeType() == Node.ELEMENT_NODE) {
                         Element track = (Element) nNodeX;

                 // Start of fix node ****
                 // Check if descendants exist for the child node
                 NodeList descendants = track.getElementsByTagName("nxce:fix");
                if (descendants.getLength() > 0) {
                   hasDescendants = true;
               //    row = eElement.getAttribute("acid");
                   row = row + "," + "JJ";
         
                                      
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
                      //     row = row + "," + latitudeElement.getAttribute("minutes");
                          //row = row + "," + latitudeElement.getAttribute("seconds");
                          
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
                   //row = row + "," + longitudeElement.getAttribute("minutes");                                             
                  // row = row + ", " + longitudeElement.getAttribute("seconds");   
                   
                  }
/****************************************************************************************************************************************** */
// secound scan DMS



                  String expressionchildX3 = "//nxcm:qualifiedAircraftId";
                  NodeList childNodesX3 = (NodeList) xPath.compile(expressionchildX3).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                     Node childNodeX3 = childNodesX3.item(i);
                      // Check if the child node is an element node
                      if (childNodeX3.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element latitudeElement = (Element) childNodeX3;
                          row = row + "," + latitudeElement.getAttribute("degrees");
                          row = row + "," + latitudeElement.getAttribute("direction");
                        //  row = row + "," + latitudeElement.getAttribute("minutes");
              //           row = row + "," + latitudeElement.getAttribute("seconds");
                         
                         }
                 

          //child node start  
          String expressionchildX4 = "//nxce:longitudeDMS";
          NodeList childNodesX4 = (NodeList) xPath.compile(expressionchildX4).evaluate(eElement, XPathConstants.NODESET);
          // Obtain the child nodes
                 Node childNodeX4 = childNodesX4.item(i);
              // Check if the child node is an element node
              if (childNode4.getNodeType() == Node.ELEMENT_NODE) {
                  // Access the child element here
                  Element longitudeElement = (Element) childNodeX4;
                  row = row + "," + longitudeElement.getAttribute("degrees");
                  row = row + "," + longitudeElement.getAttribute("direction");
                 // row = row + "," + longitudeElement.getAttribute("minutes");                                             
              //    row = row + ", " + longitudeElement.getAttribute("seconds");   
                  
                 }

                  
                 // End of fix data
                   break; // Exit the loop, as we only need to find one descendant
//******************************************************************************************************************************************

        }
    }
}



if(hasDescendants)
{
   
System.out.println("Fix: " + hasDescendants);

//System.exit(0);
}
                  

               

                 
                //row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
                   
                row = row + "\n" + "";
                returnString = returnString + row; // append row to return string
           
               
                   
                }
            }
        }
    //  }//if condition end
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
    





