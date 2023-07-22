package com.harris.cinnato.outputs;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class FSearch3 {
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
                    NodeList aircraftIdNodes = document.getElementsByTagName("nxce:aircraftId");

                    // Search for nodes matching <nxcm:timeAtPosition>
                    NodeList timeAtPositionNodes = document.getElementsByTagName("nxcm:timeAtPosition");

                    // Create an output file
                    String outputFileName = file.getName() + ".output.xml";
                    File outputFile = new File(directory, outputFileName);
                    PrintWriter writer = new PrintWriter(outputFile);

                    // Iterate over matching nodes and write the data to the output file
                    for (int i = 0; i < aircraftIdNodes.getLength(); i++) {
                        String aircraftIdData = aircraftIdNodes.item(i).getTextContent();
                        writer.println(aircraftIdData);
                    }

                    for (int i = 0; i < timeAtPositionNodes.getLength(); i++) {
                        String timeAtPositionData = timeAtPositionNodes.item(i).getTextContent();
                        writer.println(timeAtPositionData);
                    }

                    writer.close();
                    System.out.println("Output saved to: " + outputFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

