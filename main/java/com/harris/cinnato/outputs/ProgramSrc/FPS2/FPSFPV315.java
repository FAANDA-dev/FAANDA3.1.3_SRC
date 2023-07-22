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

    
    String row = "";  
    String sec = "";
//    boolean FIX = false;
    String FIX = "";
    String Dep = "";
    String Arr = "";
    String Check = "";
  
        public FPSFPV315(Config config) {
            super(config);
                 
                   
            
            outputFile = new File("./log/FlightPlansData50.csv");
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
System.out.println(row);

 
// Process child node 'qualifiedAircraftId'
   String expressionChildX12 = ".//nxcm:qualifiedAircraftId";
   NodeList childNodesX12 = (NodeList) xPath.compile(expressionChildX12).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix5 = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX12.getLength(); j++) {
    Node nNodeX12 = childNodesX12.item(j);
    if (nNodeX12.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX12;
       // row = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();
          row = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();

   //        row = row + "," + "FIX_A1";
        
        System.out.println("FIX A1");
//


       //  row = row + "," + "Departure";
          //System.out.println("Dep = true");             
        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
           // note fix is true under this code block
            hasFix5 = true;

           // row = row + "," + "Fix_A2";
             //  System.out.println("FIX A2");


        }
    }

}
 
/* 

// Process child node 'qualifiedAircraftId'
   String expressionChildX13 = ".//nxcm:qualifiedAircraftId";
   NodeList childNodesX13 = (NodeList) xPath.compile(expressionChildX13).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFix7 = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX13.getLength(); j++) {
    Node nNodeX13 = childNodesX13.item(j);
    if (nNodeX13.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX13;
      //  row = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();
          row = row + "," + track.getElementsByTagName("nxce:fix").item(0).getTextContent();

           row = row + "," + "FIX_B1";
          System.out.println(track);
         System.out.println(row);

         System.out.println("FIX B1");



       //  row = row + "," + "Departure";
          //System.out.println("Dep = true");             
        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
           // note fix is true under this code block
            hasFix7 = true;

            row = row + "," + "Fix_B2";
               System.out.println("FIX B2");


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
    





