package com.harris.cinnato.outputs;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;


import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FPSU1V100 {
    public static void main(String[] args) {
        File directory = new File("./log/xml/"); // Replace with the directory path where your XML files are located

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

        // Create an output file
        String outputFileName = "./log/logged.csv";
        File outputFile = new File(outputFileName);
        try {
            // Create output CSV file and writer
            FileWriter writer = new FileWriter(outputFile);

            // Write CSV header
            writer.write("Degrees\n");

            // Parse the XML files
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            for (File file : files) {
                Document document = builder.parse(file);

                // Find all <nxce:latitude> nodes
                NodeList latitudeNodes = document.getElementsByTagName("nxce:latitude");

                // Iterate over latitude nodes and extract the data
                for (int i = 0; i < latitudeNodes.getLength(); i++) {
                    Node latitudeNode = latitudeNodes.item(i);
                    if (latitudeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element latitudeElement = (Element) latitudeNode;

                        // Extract attribute values
                        String degrees = latitudeElement.getAttribute("degrees");

                        // Write data to CSV
                        System.out.print(degrees + "XX");
                        writer.write(degrees + "\n");
                    }
                }
            }

            // Close the writer
            writer.close();

            System.out.println("Data successfully extracted from XML and saved to CSV.");
            System.out.println("Output CSV file path: " + outputFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





