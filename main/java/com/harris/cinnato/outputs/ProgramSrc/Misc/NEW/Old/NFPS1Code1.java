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
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;



public class NFPS1Code1 extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");
    private final File outputFile;
    private final String inputFilePath = "./log/File.log";
    private String row = "";

    public NFPS1Code1(Config config) {
    super(config);
       // this.inputFilePath = inputFilePath;

        outputFile = new File("./log/FlightPlansData755.csv");

        try {
            if (!outputFile.createNewFile()) {
                outputFile.delete();
                outputFile.createNewFile();
            }

            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write("# Flight Plans #\n");
            myWriter.write("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,\n");
            myWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Document loadXMLFromFile(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }

    private String getFlightPlanData(String message) {
        String returnString = "";
        try {
            Document document = loadXMLFromFile(inputFilePath);
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

            String expression = "//fdm:fltdMessage";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (eElement.getAttribute("msgType").equals("trackInformation")) {
                        row = eElement.getElementsByTagName("nxce:aircraftId").item(0).getTextContent();
                        row = row + "," + eElement.getAttribute("depArpt");
                        row = row + "," + eElement.getAttribute("arrArpt");
                        row = row + "\n";
                        returnString = returnString + row;
                    }
                }
            }
        } catch (Exception e) {
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
    



 












