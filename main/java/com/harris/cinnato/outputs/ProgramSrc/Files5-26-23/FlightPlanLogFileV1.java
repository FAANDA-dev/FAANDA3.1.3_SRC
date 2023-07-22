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
import java.io.IOException;



/**
 * Outputs the outputFiles to a single rotating file log located in `./log/outputFiles.log`
 */
public class FlightPlanLogFile extends Output {
    private static final Logger logger = LoggerFactory.getLogger("stdout");
    private final File outputLog;
    private String nbsp;
  
        public FlightPlanLogFile(Config config) {
            super(config);
            outputLog = new File("./log/FlightPlans.log");
            
            // create the CSV file
            try {
                if(!outputLog.createNewFile()) {
                    outputLog.delete();
                    outputLog.createNewFile();
                }
                
                // add outputFile headers
                FileWriter myWriter = new FileWriter(outputLog);
              //  myWriter.write("# Flight Plans #\n"); // first outputFile header
               
                //myWriter.write(",acid,airline,arrArpt,cdmPart,depArpt,fdTrigger,flightRef,major,msgType,sensitivity,sourceFacility,sourceTimeStamp,aircraftCategory,userCategory,nxce:aircraftId,nxce:facilityIdentifier,ncxe:idNumber,nxce:gufi,nxce:igtd,nxce:departurePoint,nxce:arrivalPoint,nxcm:speed,nxce:simpleAltitude,lat_degrees,direction,minutes,seconds,long_derees,direction,minutes,seconds,nxcm:timeAtPosition,etaType,timeValue,currentCompliance,equipped,futureCompliance,arrival_arrTime,fixName,depart_arrTime,fixName,latitudeDecimail,longitudeDecimal,routOfFlight\n"); // column headers
                myWriter.close();
            } catch (IOException e) {
             //   logger.error(e.getoutputFile());
            }
        }
       



    @Override
    public void output(String outputFile, String header) {
        String rows = outputFile;
        if(rows.length() > 0){
           try{
            FileWriter myWriter = new FileWriter(outputLog, true);
            myWriter.append(rows);
            myWriter.close();
           }catch (Exception e){
           // logger.error(e.getoutputFile());
           }
                    
        }
     
    }
  

  
}
    





