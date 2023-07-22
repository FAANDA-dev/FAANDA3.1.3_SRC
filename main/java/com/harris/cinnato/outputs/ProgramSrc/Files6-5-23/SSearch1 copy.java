package com.harris.cinnato.outputs;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class SSearch1 {

    
    public static void main(String[] args) {
        String filePath = "./log/fpd.log";
        String searchString = "acid";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
    
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
    
            String fileString = fileContent.toString();
    
            if (fileString.contains(searchString)) {
                System.out.println("String found in the file!!!!!!!!!.");
            } else {
                System.out.println("String not found in the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}        
        
    




    

