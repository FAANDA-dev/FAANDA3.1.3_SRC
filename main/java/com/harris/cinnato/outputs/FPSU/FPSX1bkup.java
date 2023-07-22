package com.harris.cinnato.outputs;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;




public class FPSX1 {
    public static void main(String[] args) {
        File directory = new File("./log/xml/"); // Replace with the directory path where your XML files are located

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

        // Create an output file
        String outputFileName = "./log/FlightPlanStatsX.csv";
        File outputFile = new File(outputFileName);

        try {
            PrintWriter writer = new PrintWriter(outputFile);
            // Write CSV header
            writer.println("Aircraft ID,Time at Position,Departure Airport");

            if (files != null) {
                for (File file : files) {
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document document = builder.parse(file);

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

                        String row = "";
                        String expression = "//nxce:latitudeDMS";
                        NodeList latitudeNodes = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

                        for (int i = 0; i < latitudeNodes.getLength(); i++) {
                            Node latitudeNode = latitudeNodes.item(i);
                            if (latitudeNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element latitudeElement = (Element) latitudeNode;
                                row = latitudeElement.getAttribute("degrees");
                                row += "," + latitudeElement.getAttribute("direction");
                                row += "," + latitudeElement.getAttribute("minutes");
                                row += "," + latitudeElement.getAttribute("seconds");
                                // Add more code to process other elements and build the row

                                // Write the row to the output file
                                writer.println(row);
                            }
                        }

                        System.out.println("Data from " + file.getName() + " added to the output file.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            writer.close();
            System.out.println("Output saved to: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

         