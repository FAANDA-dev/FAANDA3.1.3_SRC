package com.harris.cinnato.outputs;
import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class FSearch2 {
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

                    // Create an output file
                    String outputFileName = file.getName() + ".output.xml";
                    File outputFile = new File(directory, outputFileName);
                    PrintWriter writer = new PrintWriter(outputFile);

                    // Iterate over matching nodes and write the data to the output file
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        String data = nodeList.item(i).getTextContent();
                        writer.println(data);
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

