/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.home.iae;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author yuyurch001
 */
public class FileModelDataFile {
    private final String IncFile="incItem.txt";
    private final String ExpFile="expItem.txt";
    private FileWriter writer;

    public String getIncFile() {
        return IncFile;
    }

    public String getExpFile() {
        return ExpFile;
    }

    void saveInFile(String line, boolean appandInFile, File pathToFile ) throws FileNotFoundException, IOException {
        System.out.println(pathToFile + " saveInFile!!!!!");
        writer = new FileWriter(pathToFile, appandInFile);
        writer.write(line);
        writer.write(System.lineSeparator());
        System.out.println("IncList saved!");
        writer.close();
    }

    void saveInFile(ArrayList<String> saveToFile, String pathToFile, boolean rewrite ) throws IOException {
        writer = new FileWriter(pathToFile, rewrite);
        for (String str : saveToFile) {
            writer.write(str);
            writer.write(System.lineSeparator());
        }
        writer.close();
    }
    
    
    
    
}
