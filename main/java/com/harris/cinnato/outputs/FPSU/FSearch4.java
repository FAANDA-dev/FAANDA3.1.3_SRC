package com.harris.cinnato.outputs;
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
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class FSearch4 {
    public static void main(String[] args) {
        File directory = new File("./log/xml/"); // Replace with the directory path where your XML files are located

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

        // Create an output file
        String outputFileName = "./log/logged.xml";
        File outputFile = new File(outputFileName);

        try {
            PrintWriter writer = new PrintWriter(outputFile);

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

                        // Iterate over matching nodes and write the data to the output file
                        for (int i = 0; i < aircraftIdNodes.getLength(); i++) {
                            String aircraftIdData = aircraftIdNodes.item(i).getTextContent();
                            writer.println(aircraftIdData);
                        }

                        for (int i = 0; i < timeAtPositionNodes.getLength(); i++) {
                            String timeAtPositionData = timeAtPositionNodes.item(i).getTextContent();
                            writer.println(timeAtPositionData);
                        }

                        writer.println(); // Add a blank line to separate data from different files
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
