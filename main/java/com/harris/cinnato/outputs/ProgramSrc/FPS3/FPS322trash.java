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
public class FPS322 extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
      
    String row = "";  
    String sec = "";
    boolean FIX = false;
  
    
  
        public FPS322(Config config) {
            super(config);
                 
                   
            
            outputFile = new File("./log/FlightPlansData7111.csv");
             // create the CSV file
             try {
                if(!outputFile.createNewFile()) {
                    outputFile.delete();
                    outputFile.createNewFile();
                }
           
                
                // add message headers
                FileWriter myWriter = new FileWriter(outputFile);
                myWriter.write("# Flight Plans #\n"); // first message header
                myWriter.write("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,\n");

              //  myWriter.write(",acid,airline,arrArpt,cdmPart,depArpt,fdTrigger,flightRef,major,msgType,sensitivity,sourceFacility,sourceTimeStamp,aircraftCategory,userCategory,nxce:aircraftId,nxce:facilityIdentifier,ncxe:idNumber,nxce:gufi,nxce:igtd,nxce:departurePoint,nxce:arrivalPoint,nxcm:speed,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition,etaType,timeValue,currentCompliance,equipped,futureCompliance,arrival_arrTime,fixName,depart_arrTime,fixName,latitudeDecimail,longitudeDecimal,routOfFlight\n"); // column headers
              //  myWriter.write("acid,nxce:aircraftId,aircraftCategory,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition,lat_degrees,direction,minutes,long_derees,direction,minutes,\n");//colum headers 

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

    

  //departure point airport *****************************************
  // Process child node 'qualifiedAircraftId'
   String expressionChildX7 = ".//nxce:departurePoint";
   NodeList childNodesX7 = (NodeList) xPath.compile(expressionChildX7).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean has_Dep = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX7.getLength(); j++) {
    Node nNodeX7 = childNodesX7.item(j);
    if (nNodeX7.getNodeType() == Node.ELEMENT_NODE) {
        Element trackZ = (Element) nNodeX7;

        // Check if descendants exist for the child node
        NodeList descendants = trackZ.getElementsByTagName("nxce:airport");
        if (descendants.getLength() > 0) {
            has_Dep = true;
            row = row + "," + trackZ.getElementsByTagName("nxce:airport").item(0).getTextContent();
                         
           
        }



    }



}
                  


//arrival point airport *****************************************
  // Process child node 'qualifiedAircraftId'
   String expressionChildX30 = ".//nxce:arrivalPoint";
   NodeList childNodesX30 = (NodeList) xPath.compile(expressionChildX30).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean has_Arr = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX30.getLength(); j++) {
    Node nNodeX30 = childNodesX30.item(j);
    if (nNodeX30.getNodeType() == Node.ELEMENT_NODE) {
        Element track3 = (Element) nNodeX30;
       // row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();

        // Check if descendants exist for the child node
        NodeList descendants3 = track3.getElementsByTagName("nxce:airport");
        if (descendants3.getLength() > 0) {
            has_Arr = true;
            row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();
           
                          
       
        }
       
    }

 
       





}



//**********************************************start reg*********************************************************************************
   String Check = "OTHER";
   String expressionchildX3 = "//nxcm:qualifiedAircraftId";
                  NodeList childNodesX3 = (NodeList) xPath.compile(expressionchildX3).evaluate(eElement, XPathConstants.NODESET);
                  // Obtain the child nodes
                     Node childNodeX3 = childNodesX3.item(i);
                      // Check if the child node is an element node
                      if (childNodeX3.getNodeType() == Node.ELEMENT_NODE) {
                          // Access the child element here
                          Element eElement2 = (Element) childNodeX3;
Check = eElement2.getAttribute("aircraftCategory");

if ("OTHER".equals(Check))
{
     row = row + "," + "," + "," + eElement2.getAttribute("aircraftCategory");


}
else{

row = row + "," + eElement2.getAttribute("aircraftCategory");

     }
                      
       
  }













                   
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
    





