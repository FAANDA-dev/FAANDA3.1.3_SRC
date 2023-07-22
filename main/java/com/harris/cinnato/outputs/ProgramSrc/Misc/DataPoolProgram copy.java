package com.harris.cinnato.outputs;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataPoolProgram {
    private BlockingQueue<String> dataPool;
    private static final Logger logger = LoggerFactory.getLogger("stdout");

    private static final String COPYRIGHT_NOTICE = "/*\n" +
            " * Copyright (c) 2021 L3Harris Technologies\n" +
            " */";

    public DataPoolProgram() {
        dataPool = new LinkedBlockingQueue<>();
    }

    public void processFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder fileContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }

            String updatedContent = insertCopyrightNotice(fileContent.toString());
            logger.info(updatedContent);

            putDataIntoPool(updatedContent);
        } catch (IOException e) {
            logger.error("Error reading file: " + filePath, e);
        }
    }

    private String insertCopyrightNotice(String fileContent) {
        int insertionIndex = fileContent.indexOf("package");

        if (insertionIndex != -1) {
            StringBuilder updatedContent = new StringBuilder(fileContent);
            updatedContent.insert(insertionIndex, COPYRIGHT_NOTICE);
            return updatedContent.toString();
        }

        return fileContent;
    }

    private void putDataIntoPool(String data) {
        try {
            dataPool.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Handle exceptions as per your requirement
        }
    }

    public void writeDataPoolToFile(String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            while (!dataPool.isEmpty()) {
                String data = dataPool.poll();
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as per your requirement
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "path/to/input/file.txt";
        String outputFilePath = "path/to/output/file.txt";

        DataPoolProgram program = new DataPoolProgram();
        program.processFile(inputFilePath);
        program.writeDataPoolToFile(outputFilePath);
    }
}
