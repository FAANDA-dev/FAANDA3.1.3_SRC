package com.harris.cinnato.outputs;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class SSearch1 {

  

    public static void main(String[] args) {
        String filePath = "./log/fpd.log";
        String startWord = "acid";
        String stopWord = "trackInformation";
        boolean printLines = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains(startWord)) {
                    printLines = true;
                }

                if (printLines) {
                    System.out.println(line);
                }

                if (line.contains(stopWord)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    
    
        
        
    




    

