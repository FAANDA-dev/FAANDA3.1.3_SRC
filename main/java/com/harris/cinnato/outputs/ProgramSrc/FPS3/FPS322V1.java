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
                 
                   
            
            outputFile = new File("./log/FlightPlansData7555.csv");
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
   boolean hasFix1 = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX7.getLength(); j++) {
    Node nNodeX7 = childNodesX7.item(j);
    if (nNodeX7.getNodeType() == Node.ELEMENT_NODE) {
        Element trackZ = (Element) nNodeX7;

        // Check if descendants exist for the child node
        NodeList descendants = trackZ.getElementsByTagName("nxce:airport");
        if (descendants.getLength() > 0) {
            hasFix1 = true;
            row = row + "," + trackZ.getElementsByTagName("nxce:airport").item(0).getTextContent();
           
                          
           
        }
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
       // row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();

        // Check if descendants exist for the child node
        NodeList descendants3 = track3.getElementsByTagName("nxce:airport");
        if (descendants3.getLength() > 0) {
            hasFix3 = true;
            row = row + "," + track3.getElementsByTagName("nxce:airport").item(0).getTextContent();
           
                          
       
        }
    }
}



//**********************************************start reg*********************************************************************************







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



//************************************************End of func************************************************************************
 

//**************fix!!!! regular
 
   // Process child node 'qualifiedAircraftId'
   String expressionChildX10 = ".//nxcm:qualifiedAircraftId";
   NodeList childNodesX10 = (NodeList) xPath.compile(expressionChildX10).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFixReg = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX10.getLength(); j++) {
    Node nNodeX10 = childNodesX10.item(j);
    if (nNodeX10.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX10;

        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
            hasFixReg = true;
             //row = row + "," + "," +"FIX";

             row = row + "," +"FIX";

             System.out.println(row);
            // Iterate over descendants
            for (int k = 0; k < descendants.getLength(); k++) {
                Node descendantNode = descendants.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

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


//**************fix!!!! departure
 
   // Process child node 'departurePoint'
   String expressionChildX17 = ".//nxce:departurePoint";
   NodeList childNodesX17 = (NodeList) xPath.compile(expressionChildX17).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFixDep = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX17.getLength(); j++) {
    Node nNodeX17 = childNodesX17.item(j);
    if (nNodeX17.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX17;

        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
            hasFixDep = true;
           
           //   row = row + "," + "," +"FIX_Dep";
             System.out.println(row);
            // Iterate over descendants
            for (int k = 0; k < descendants.getLength(); k++) {
                Node descendantNode = descendants.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

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

                    if(hasFixDep)
                    {
                     row = row + "FIX_Dep" + ","+ "," + "," + "," + "," +",";

                    }



                }
            }
        }
    }
}




//**************fix!!!! arrival
 
   // Process child node "arrivalPoint'
   String expressionChildX15 = ".//nxce:arrivalPoint";
   NodeList childNodesX15 = (NodeList) xPath.compile(expressionChildX15).evaluate(eElement, XPathConstants.NODESET);

   // Check if any descendants are found
   boolean hasFixArr = false;
 // Iterate over child nodes
for (int j = 0; j < childNodesX15.getLength(); j++) {
    Node nNodeX15 = childNodesX15.item(j);
    if (nNodeX15.getNodeType() == Node.ELEMENT_NODE) {
        Element track = (Element) nNodeX15;

        // Check if descendants exist for the child node
        NodeList descendants = track.getElementsByTagName("nxce:fix");
        if (descendants.getLength() > 0) {
            hasFixArr = true;
             row = row + "," + "FIX_Arr";
            //  row = row + "," + "," +"FIX_Arr";
             System.out.println(row);
            // Iterate over descendants
            for (int k = 0; k < descendants.getLength(); k++) {
                Node descendantNode = descendants.item(k);
                if (descendantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element descendantElement = (Element) descendantNode;

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

                    if(hasFixDep)
                    {
                     row = row + "FIX_Arr" + ","+ "," + "," + "," + "," +",";

                    }




                }
            }
        }
    }
}







//******************************************************************************************************************************************












                   
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
    





