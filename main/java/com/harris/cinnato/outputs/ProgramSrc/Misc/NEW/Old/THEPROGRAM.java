
/*
 * Copyright (c) 2021 L3Harris Technologies
 */
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

public class Car {
    public static void main(String[] args) {
        String inputFile = "./log/file.log";
        String outputFile = "./log/fileout.log";
        String matchingWord = "fdm:fltdMessage";

        try {
            String modifiedString = findAndAppendCarriageReturn(inputFile, matchingWord);
            writeToFile(outputFile, modifiedString);
            System.out.println("Modified string written to file: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findAndAppendCarriageReturn(String inputFile, String matchingWord) throws IOException {
        StringBuilder modifiedString = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(matchingWord)) {
                    line = line.replace(matchingWord, matchingWord + "TTTTTs\n");
                }
                modifiedString.append(line).append("TTTTT\n");
            }
        }

        return modifiedString.toString();
    }

    private static void writeToFile(String outputFile, String content) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(content);
        }
    }
}
