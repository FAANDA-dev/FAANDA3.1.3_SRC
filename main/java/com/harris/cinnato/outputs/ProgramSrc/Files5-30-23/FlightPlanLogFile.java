/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Output pool of data streaming comming in 
 */
public class FlightPlanLogFile extends Output {
    private static final Logger logger = LoggerFactory.getLogger("stdout");
    private final File outputLog;
    private String nbsp;
  
        public FlightPlanLogFile(Config config) {
            super(config);
            outputLog = new File("./log/FlightPlanData.log");
            
            // create the CSV file
            try {
                if(!outputLog.createNewFile()) {
                    outputLog.delete();
                    outputLog.createNewFile();
                }
                
                // add outputFile headers
                FileWriter myWriter = new FileWriter(outputLog);
             
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
    





