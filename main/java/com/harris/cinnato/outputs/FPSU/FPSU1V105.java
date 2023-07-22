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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;

public class FPSU1V105 {
    public static void main(String[] args) {
        File directory = new File("./log/xml/"); // Replace with the directory path where your XML files are located

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

        // Create an output file
        String outputFileName = "./log/FlightPlanStatsX.csv";
        File outputFile = new File(outputFileName);
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            // Write CSV header
            writer.println("Aircraft ID,Degrees");

            if (files != null) {
                for (File file : files) {
                    try {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   DocumentBuilder builder = factory.newDocumentBuilder();
     Document document = builder.parse(file);
    // Search for nodes matching <nxce:aircraftId>
    NodeList aircraftIdNodes = document.getElementsByTagName("nxce:aircraftId");

 // Ensure all required node lists have the same number of elements
    int nodeCount = aircraftIdNodes.getLength();

  // Iterate over matching nodes and write the data to the output file
     for (int i = 0; i < nodeCount; i++) {
          Node aircraftIdNode = aircraftIdNodes.item(i);

    if (aircraftIdNode != null) {
        String aircraftIdData = aircraftIdNode.getTextContent();

      String degreesData = ""; // Initialize degreesData variable

   // Search for nodes matching <nxce:latitude> within the current aircraftIdNode
   NodeList latitudeNodes = aircraftIdNode.getParentNode().getChildNodes();

   // Find the latitude node with the "degrees" attribute
      for (int j = 0; j < latitudeNodes.getLength(); j++) {
      Node latitudeNode = latitudeNodes.item(j);
      if (latitudeNode.getNodeName().equals("nxce:latitude")) {
          Element latitudeElement = (Element) latitudeNode;
          String degrees = latitudeElement.getAttribute("degrees");
          degreesData = degrees;
           break;
         }
       }

     writer.println(aircraftIdData + "," + degreesData);
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
