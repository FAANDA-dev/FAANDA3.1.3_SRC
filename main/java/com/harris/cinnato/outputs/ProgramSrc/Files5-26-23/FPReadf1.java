package com.harris.cinnato.outputs;


    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    
    public class FPReadf1 {
        public static void main(String[] args) {
            String filePath = "./log/rData1.txt"; // Specify the path to your flat file
    
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                StringBuilder fileContent = new StringBuilder();
    
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
    
                String fileData = fileContent.toString();
                System.out.println("File contents:\n" + fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    














    

