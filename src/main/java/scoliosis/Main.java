package scoliosis;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static scoliosis.mainjframe.DrawDisplay;


public class Main{

    /*
    todo: make todo list
     */


    public static void main(String[] args) throws IOException {
        loadresources();

        DrawDisplay();
    }

    public static String scoliosis = System.getenv("APPDATA") + "\\scoliosis";
    public static String baseName = System.getenv("APPDATA") + "\\scoliosis\\BombParty";
    public static String resourcesFile = baseName + "\\resources";

    public static void loadresources() {

        if (!Files.isDirectory(Paths.get(scoliosis))) new File(scoliosis).mkdir();
        if (!Files.isDirectory(Paths.get(baseName))) new File(baseName).mkdir();
        if (!Files.isDirectory(Paths.get(resourcesFile))) new File(resourcesFile).mkdir();

        try {

            copyFileOut("files.txt");


            String[] files = Files.readAllLines(Paths.get(resourcesFile + "\\files.txt")).toString().replace("]", "").split(",");

            for (String file : files) {
                copyFileOut(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFileOut(String filename) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (filename == "files.txt" && Files.exists(Paths.get(resourcesFile + "\\" + filename))) Files.delete(Paths.get(resourcesFile + "\\" + filename));

        if (!Files.exists(Paths.get(resourcesFile + "\\" + filename)))
        if (inputStream != null) Files.copy(inputStream, Paths.get(resourcesFile + "\\" + filename));
    }

}
