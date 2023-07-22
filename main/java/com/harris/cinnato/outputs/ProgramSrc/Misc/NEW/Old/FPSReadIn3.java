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



import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FPSReadIn2 {

    private static final Logger logger = LoggerFactory.getLogger("file");
    private static final String DIRECTORY_PATH = "./log/xml/";
    private static final String OUTPUT_FILE_PATH = "./log/FlightPlansData8.csv";

    public static void main(String[] args) {
        try {
            readXMLFiles(DIRECTORY_PATH, OUTPUT_FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readXMLFiles(String directoryPath, String outputFilePath) throws Exception {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null) {
            System.out.println("No files found in the directory.");
            return;
        }

        // Sort the files based on their names in ascending order
        Arrays.sort(files);

        // Create the output CSV file
        try {
            FileWriter writer = new FileWriter(outputFilePath);
            writer.write("# Flight Plans #\n");
            writer.write("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error creating the output CSV file.");
            e.printStackTrace();
            return;
        }

        // Process the XML files
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("Reading file: " + file.getAbsolutePath());
                processXMLFile(file, outputFilePath);
            }
        }
    }

    private static void processXMLFile(File file, String outputFilePath) {
        try {
            Document document = loadXMLFromFile(file);
            String flightPlanData = getFlightPlanData(document);
            if (!flightPlanData.isEmpty()) {
                appendToCSVFile(flightPlanData, outputFilePath);
            }
        } catch (Exception e) {
            System.out.println("Error processing XML file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private static Document loadXMLFromFile(File file) throws ParserConfigurationException, IOException, org.xml.sax.SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    private static String getFlightPlanData(Document document) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                switch (prefix) {
                    case "df":
                        return "urn:us:gov:dot:faa:atm:tfm:tfmdataservice";
                    case "fdm":
                        return "urn:us:gov:dot:faa:atm:tfm:flightdata";
                    case
