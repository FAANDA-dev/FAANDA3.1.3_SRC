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



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FPSUtil2 extends Output {
    private static final Logger logger = LoggerFactory.getLogger(FPSUtil1.class);
    private final File fileY1;
    private final File fileY2;
    private File currentFile;

    public FPSUtil2(Config config) {
        super(config);
        // TODO: change the directory from config
        fileY1 = new File("./log/fileY1.log");
        fileY2 = new File("./log/fileY2.log");
        currentFile = fileY1;

        scheduleFileSwitching();
    }

    @Override
    public void output(String message, String header) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile, true))) {
            if (this.config.getBoolean("headers")) {
                writer.write(this.convert(header));
            }
            writer.write(this.convert(message));
        } catch (IOException e) {
            logger.error("Failed to write to file", e);
        }
    }

    private void scheduleFileSwitching() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentFile.equals(fileY1)) {
                    currentFile = fileY2;
                } else {
                    currentFile = fileY1;
                }
                // Reschedule file switching after 15 seconds
                scheduleFileSwitching();
            }
        }, 15000); // Switch files after 15 seconds
    }
}


















    

