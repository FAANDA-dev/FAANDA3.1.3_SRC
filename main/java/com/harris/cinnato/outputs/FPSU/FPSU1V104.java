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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;






public class FPSU1V104 {
    public static void main(String[] args) {
        File directory = new File("./log/xml/"); // Replace with the directory path where your XML files are located

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

        // Create an output file
        String outputFileName = "./log/FlightPlanStatsX.csv";
        File outputFile = new File(outputFileName);
try {
    PrintWriter writer = new PrintWriter(outputFile);
    // Write CSV header
    writer.println("Aircraft ID,Time at Position,Departure Airport,Arrival Airport,Latitude");

    if (files != null) {
        for (File file : files) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);

                // Search for nodes matching <nxce:aircraftId>
                NodeList aircraftIdNodes = document.getElementsByTagName("nxce:aircraftId");

                // Search for nodes matching <nxcm:timeAtPosition>
                NodeList timeAtPositionNodes = document.getElementsByTagName("nxcm:timeAtPosition");

                // Search for nodes matching <nxce:airport>
                NodeList airportNodes = document.getElementsByTagName("nxce:airport");

                // Search for nodes matching <nxce:arrivalPoint><nxce:airport></nxce:airport></nxce:arrivalPoint>
                NodeList arrivalPointNodes = document.getElementsByTagName("nxce:arrivalPoint");

                // Ensure all required node lists have the same number of elements
                int nodeCount = Math.min(Math.min(Math.min(aircraftIdNodes.getLength(), timeAtPositionNodes.getLength()), airportNodes.getLength()), arrivalPointNodes.getLength());

                // Iterate over matching nodes and write the data to the output file
                for (int i = 0; i < nodeCount; i++) {
                    Node aircraftIdNode = aircraftIdNodes.item(i);
                    Node timeAtPositionNode = timeAtPositionNodes.item(i);
                    Node airportNode = airportNodes.item(i);
                    Node arrivalPointNode = arrivalPointNodes.item(i);

                    if (aircraftIdNode != null && timeAtPositionNode != null && airportNode != null && arrivalPointNode != null) {
                        String aircraftIdData = aircraftIdNode.getTextContent();
                        String timeAtPositionData = timeAtPositionNode.getTextContent();
                        String airportData = airportNode.getTextContent();
                        String arrivalPointData = arrivalPointNode.getTextContent();

                        String latitudeData = ""; // Initialize latitudeData variable

                        // Search for nodes matching <nxce:latitude> within the current arrivalPointNode
                        NodeList latitudeNodes = ((Element) arrivalPointNode).getElementsByTagName("nxce:latitude");

                        // Ensure the latitude nodes exist
                        if (latitudeNodes.getLength() > 0) {
                            Element latitudeElement = (Element) latitudeNodes.item(0);
                            String degrees = latitudeElement.getAttribute("degrees");
                            String direction = latitudeElement.getAttribute("direction");
                            String minutes = latitudeElement.getAttribute("minutes");
                            String seconds = latitudeElement.getAttribute("seconds");

                            latitudeData = degrees + "° " + minutes + "' " + seconds + "\" " + direction;
                        }

                        writer.println(aircraftIdData + "," + timeAtPositionData + "," + airportData + "," + arrivalPointData + "," + latitudeData);
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
