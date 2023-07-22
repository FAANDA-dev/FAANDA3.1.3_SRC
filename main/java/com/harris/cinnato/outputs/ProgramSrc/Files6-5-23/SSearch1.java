package com.harris.cinnato.outputs;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SSearch1 {
    public static void printSubstringFromFile(String filename, String startWord, String endWord) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String fileContent = sb.toString();
            int startIndex = fileContent.indexOf(startWord);
            int endIndex = fileContent.indexOf(endWord);

            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                String substring = fileContent.substring(startIndex, endIndex + endWord.length());
                System.out.println(substring);
            } else {
                System.out.println("Start or end word not found, or the order is incorrect.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filename = "./log/fpd.log";
        String startWord = "fdm";
        String endWord = "fdm";

        printSubstringFromFile(filename, startWord, endWord);
    }
}
