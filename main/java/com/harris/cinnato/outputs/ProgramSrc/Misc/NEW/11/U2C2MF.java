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
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import java.io.*;
import java.util.regex.*;

import java.io.*;
import java.util.regex.*;

import java.io.*;
import java.util.regex.*;

public class U2C2MF {
    public static void main(String[] args) {
        String inputFile = "./log/fileout.log";
        String outputFile = "./log/fileout2.log";
        String[] patterns = {
            "<nxce:idNumber>(\\d+)</nxce:idNumber>",
            "<nxce:altitude>([A-Za-z0-9]+)</nxce:altitude>",
            //"<nxce:address>([A-Za-z0-9]+)</nxce:address>"
        };

        try {
            String extractedValues = extractData(inputFile, patterns);
            writeToFile(outputFile, extractedValues);
            System.out.println("Extracted values written to file: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractData(String inputFile, String[] patterns) throws IOException {
        StringBuilder extractedValues = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String pattern : patterns) {
                    Pattern regexPattern = Pattern.compile(pattern);
                    Matcher matcher = regexPattern.matcher(line);

                    while (matcher.find()) {
                        extractedValues.append(matcher.group(1)).append(",");
                    }
                }
                extractedValues.append("\n");
            }
        }

        return extractedValues.toString();
    }

    private static void writeToFile(String outputFile, String content) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(content);
        }
    }
}
