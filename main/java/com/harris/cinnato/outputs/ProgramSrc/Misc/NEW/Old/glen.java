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




import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class glen extends Output {
    private static final Logger logger = LoggerFactory.getLogger(glen.class);
    private final File outputFile;

    public glen(Config config) {
        super(config);
        // TODO change the directory from config
        outputFile = new File("./log/file777.log");
    }

    @Override
    public void output(String message, String header) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            if (this.config.getBoolean("headers")) {
                writer.write(this.convert(header));
            }
            writer.write(this.convert(message));
        } catch (IOException e) {
            logger.error("Failed to write to file", e);
        }
    }
}
