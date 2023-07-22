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



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;








import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;







public class FPSReadIn1 {


    public static void main(String[] args) {
        String directoryPath = "./log/xml/"; // Path to the directory containing XML files
        int startingNumber = 1; // Starting number of the XML files
        int numberOfFiles = 10; // Number of XML files to read

        try {
            readXMLFiles(directoryPath, startingNumber, numberOfFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readXMLFiles(String directoryPath, int startingNumber, int numberOfFiles) throws Exception {
        File directory = new File(directoryPath);

        for (int i = startingNumber; i < startingNumber + numberOfFiles; i++) {
            String fileName = i + ".xml";
            File file = new File(directory, fileName);

            if (file.exists()) {
                System.out.println("Reading file: " + file.getAbsolutePath());
                // Process the XML file here
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                // ... Do something with the XML document

            } else {
                System.out.println("File not found: " + file.getAbsolutePath());
            }
        }
    }
}



















    

