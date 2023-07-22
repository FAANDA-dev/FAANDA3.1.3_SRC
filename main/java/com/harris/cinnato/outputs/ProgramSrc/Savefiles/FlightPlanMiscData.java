package com.harris.cinnato.outputs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FlightPlanMiscData {
    public static void main(String[] args) {
        String filePath = "datafile1.txt";
        String searchTerm = "fltdMeessage";

        try {
            // Read the file content as one long string
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

            // Find the index of the matching word
            int wordIndex = fileContent.indexOf(searchTerm);
            if (wordIndex != -1) {
                // Insert a newline character after the matching word
                StringBuilder modifiedContent = new StringBuilder(fileContent);
                modifiedContent.insert(wordIndex + searchTerm.length(), "\r\n");

                // Save the modified content back to the file
                Files.write(Paths.get(filePath), modifiedContent.toString().getBytes(StandardCharsets.UTF_8));

                System.out.println("Modification completed.");
            } else {
                System.out.println("Matching word not found in the file.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        }
    }
}

   












    
}
