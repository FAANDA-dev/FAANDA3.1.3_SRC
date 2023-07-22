package com.harris.cinnato.outputs;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StdoutOutput extends Output {
    private static final Logger logger = LoggerFactory.getLogger("stdout");

    private static final String COPYRIGHT_NOTICE = "/*\n" +
            " * Copyright (c) 2021 L3Harris Technologies\n" +
            " */";

    public StdoutOutput(Config config) {
        super(config);
    }

    @Override
    public void output(String message, String header) {
        if (this.config.getBoolean("headers")) {
            logger.info(this.convert(header) + this.convert(message));
        } else {
            logger.info(this.convert(message));
        }
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
}
