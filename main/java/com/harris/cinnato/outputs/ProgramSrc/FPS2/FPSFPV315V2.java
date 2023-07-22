/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.lang.model.util.ElementScanner14;
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
public class FPSFPV315 extends Output {

    
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;

      public static boolean isNullOrEmpty(String row) {
        return row == null || row.isEmpty();
      }
    String row = "";  
    String sec = "";
//    boolean FIX = false;
    String FIX = "";
    String Dep = "";
    String Arr = "";
    String Check = "";
  
        public FPSFPV315(Config config) {
            super(config);
                 
                   
            
            outputFile = new File("./log/FlightPlansData35.csv");
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
                myWriter.write("acid,nxce:aircraftId,aircraftCategory,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition,lat_degrees,direction,minutes,long_derees,direction,minutes,\n");//colum headers 

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

                  String expressionchildX3 = "//nxcm:qualifiedAircraftId";
                  NodeList childNodesX3 = (NodeList) xPath.compile(expressionchildX3).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                     Node childNodeX3 = childNodesX3.item(i);
                      // Check if the child node is an element node
                      if (childNodeX3.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element eElement2 = (Element) childNodeX3;
                          row = row + "," + eElement2.getAttribute("aircraftCategory");
                          
                      }
                  
                    row = row + "," + eElement.getElementsByTagName("nxcm:speed").item(0).getTextContent();

 
                    row = row + "," + eElement.getElementsByTagName("nxce:simpleAltitude").item(0).getTextContent();

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
                          //row = row + "," + latitudeElement.getAttribute("seconds");
                           sec = latitudeElement.getAttribute("seconds");
                            if(sec.isEmpty())
                            {
                                sec = "00 ";
                                row = row + "," + sec;
                            }
                            else
                            {
                                row = row + "," + latitudeElement.getAttribute("seconds");
                            }

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
                  //row = row + ", " + longitudeElement.getAttribute("seconds");   
                   sec = longitudeElement.getAttribute("seconds");
                            if(sec.isEmpty())
                            {
                                sec = "00 ";
                                row = row + "," + sec;
                            }
                            else
                            {
                                row = row + "," + longitudeElement.getAttribute("seconds");
                            }
 
                 }


                

                 
                row = row + "," + eElement.getElementsByTagName("nxcm:timeAtPosition").item(0).getTextContent();
//******************************************************************************************************************************************

//departure point airport *****************************************
  // Process child node 'qualifiedAircraftId'
   String expressionChildX7 = ".//nxce:departurePoint";
   NodeList childNodesX7 = (NodeList) xPath.compile(expressionChildX7).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix1 = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX7.getLength(); j++) {
    Node nNodeX7 = childNodesX7.item(j);
    if (nNodeX7.getNodeType() == Node.ELEMENT_NODE) {
        Element trackZ = (Element) nNodeX7;
        
        row = row + "," + trackZ.getElementsByTagName("nxce:airport").item(0).getTextContent();
       row = row + "," + "Dep";
System.out.println("Dep");

        
    }
}




//arrival point airport *****************************************
  // Process child node 'qualifiedAircraftId'
   String expressionChildX30 = ".//nxce:arrivalPoint";
   NodeList childNodesX30 = (NodeList) xPath.compile(expressionChildX30).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix3 = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX30.getLength(); j++) {
    Node nNodeX30 = childNodesX30.item(j);
    if (nNodeX30.getNodeType() == Node.ELEMENT_NODE) {
        Element track3 = (Element) nNodeX30;
        row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();

        row = row + "," + "Arr";
         System.out.println("Arr");

    }
}

//********************************************************************************************************************
// Process child node 'qualifiedAircraftId'
   String expressionChildX10 = ".//nxcm:qualifiedAircraftId";
   NodeList childNodesX10 = (NodeList) xPath.compile(expressionChildX10).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX10.getLength(); j++) {
    Node nNodeX10 = childNodesX10.item(j);
    if (nNodeX10.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX10;
      //  row = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();
         Check = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();
         row = row + "," + "FixA";

         System.out.println("FIX A TRUE******************************");



       //  row = row + "," + "Departure";
          //System.out.println("Dep = true");             
        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
           // note fix is true under this code block
            hasFix = true;

            row = row + "," + "FixBBBBB";
        System.out.println("FIX BBBBB TRUE******************************");




        }
    }

}
 




/*
//Test!!!!!!!!
 // Process child node 'qualifiedAircraftId'
   String expressionChildX11 = ".//nxce:departurePoint";
   NodeList childNodesX11 = (NodeList) xPath.compile(expressionChildX11).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix2 = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX11.getLength(); j++) {
    Node nNodeX11 = childNodesX11.item(j);
    if (nNodeX11.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX11;
      //  row = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();
         Check = row + "," + track.getElementsByTagName("nxce:airport").item(0).getTextContent();
         row = row + "," + "Departure";
          //System.out.println("Dep = true");             
        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
           // note fix is true under this code block
            hasFix = true;
            
             // row = row + "," + track.getElementsByTagName("nxce:airport").item(0).getTextContent();
                   
             
            // Iterate over descendants
            for (int k = 0; k < descendants.getLength(); k++) {
                Node descendantNode = descendants.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;
                    descendantElement.getElementsByTagName("nxce:latitidue").item(0).getTextContent();




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


             


                }
            }
        }
    }
}




*/







 
   
 



                   
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
    





