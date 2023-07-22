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

public class FPSReadIn2 {

    public static void main(String[] args) {
        String directoryPath = "./log/xml/"; // Path to the directory containing XML files

        try {
            readXMLFiles(directoryPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readXMLFiles(String directoryPath) throws Exception {
        File directory = new File(directoryPath);

        // Get a list of all files in the directory
        File[] files = directory.listFiles();

        // Sort the files based on their names in ascending order
        Arrays.sort(files);

        // Iterate over the sorted files and process them
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("Reading file: " + file.getAbsolutePath());
                // Process the XML file here
                // ...
            }
        }
    }
}
