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





public class FPSDelPart {


    public static void main(String[] args) {
        String inputFile = "./log/filex.xml"; // Path to the input file
        String outputFile = "./log/output.txt"; // Path to the output file
        String patternBegin = "<?xml"; // Starting pattern
        String patternEnd = "\"no\"?>"; // Ending pattern

        try {
            removePatternFromFile(inputFile, outputFile, patternBegin, patternEnd);
            System.out.println("Pattern removed successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void removePatternFromFile(String inputFile, String outputFile, String patternBegin, String patternEnd)
            throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            boolean insidePattern = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains(patternBegin)) {
                    insidePattern = true;
                    continue;
                }

                if (line.contains(patternEnd)) {
                    insidePattern = false;
                    continue;
                }

                if (!insidePattern) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}




























    

