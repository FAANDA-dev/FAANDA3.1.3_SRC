package com.harris.cinnato.outputs;

import java.io.*;
import java.util.*;

import java.io.*;
import java.util.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class FSearch1 {
    public static void main(String[] args) {
        File directory = new File("./log/xml/"); // Replace with the directory path where your XML files are located

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".xml"));

        if (files != null) {
            for (File file : files) {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(file);

                    // Search for nodes matching <nxce:aircraftId>
                    NodeList nodeList = document.getElementsByTagName("nxce:aircraftId");

                    // Iterate over matching nodes and print the data between them
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        String data = nodeList.item(i).getTextContent();
                        System.out.println(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
