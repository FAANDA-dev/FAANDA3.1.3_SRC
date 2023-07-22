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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class C2LN1 {
    public static void main(String[] args) {
        String inputFile = "./log/input.txt";
        String outputFile = "./log/output.txt";
        String pattern = "fdm:fltdMessage>";

        try {
            String modifiedContent = modifyFileContent(inputFile, pattern);
            writeToFile(outputFile, modifiedContent);
            System.out.println("Modified content written to file: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String modifyFileContent(String inputFile, String pattern) throws IOException {
        StringBuilder modifiedContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(pattern)) {
                    line += "\n"; // Append carriage return at the end
                }
                modifiedContent.append(line).append("\n");
            }
        }

        return modifiedContent.toString();
    }

    private static void writeToFile(String outputFile, String content) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(content);
        }
    }
}
