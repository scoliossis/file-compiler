package scoliosis;

import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Libs.RenderLib;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static scoliosis.Main.*;
import static scoliosis.mainjframe.mainframe;
import static scoliosis.mainjframe.textbox;


public class Game {

    public static ArrayList usedWords = new ArrayList();
    static {
        usedWords.add("word");
    }

    static String Word = "";

    public static void game(BufferedImage bi, BufferStrategy bs) throws IOException {
        textbox.requestFocus();

        if (bs != null) {
            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/background.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            Font font = new Font("Comic Sans MS", 1, (int) (15/480f *(float) (mainframe.getWidth())));
            g.setFont(font);
            g.setColor(new Color(0,0,0));

            Word = mainjframe.textbox.getText().replaceAll("\"", "");
            //Word = "C:\\Users\\scale\\Downloads\\testfile";

            RenderLib.drawRoundedRect(300-g.getFontMetrics().stringWidth(Word)/2  - g.getFontMetrics().stringWidth("input file path: ") - 20, 135, g.getFontMetrics().stringWidth(Word) + g.getFontMetrics().stringWidth("input file path: ") + 30 , 30, 10, 10, new Color(0, 0, 0, 66), g);
            RenderLib.drawRoundedRectOutline(300-g.getFontMetrics().stringWidth(Word)/2 - 10, 135, g.getFontMetrics().stringWidth(Word) + 20, 30, 10, 10, new Color(255, 0, 252, 66), g);
            RenderLib.drawString("input file path: ", 300-g.getFontMetrics().stringWidth(Word)/2 - g.getFontMetrics().stringWidth("input file path: ") - 15, 157, new Color(255, 255, 255), g);

            RenderLib.drawString(Word, 300-g.getFontMetrics().stringWidth(Word)/2, 157, new Color(255, 255, 255), g);

            if (KeyLib.keyPressed(KeyEvent.VK_ENTER)) {
                if (Files.exists(Paths.get(Word))) {
                    if (!Word.endsWith(".scolio")) {
                        System.out.println("zipping file path: " + Word);

                        scolioPack(Word);
                    }
                    else {
                        System.out.println("unzipping file path: " + Word);

                        scolioUnpack(Word);
                    }

                    System.out.println("done!");
                    
                }
                else {
                    RenderLib.drawString("grow up bro (file path invalid)", 300 - g.getFontMetrics().stringWidth("grow up bro (file path invalid)") / 2 - 60, 190, new Color(255, 0, 0), g);
                }
            }

            g.dispose();
            bs.show();
        }
    }

    public static void scolioPack(String filePath) {
        try {
            String zipPath = filePath.split(filePath.split("\\\\")[filePath.split("\\\\").length - 1])[0] + "\\" + filePath.split("\\\\")[filePath.split("\\\\").length - 1] + ".scolio";

            Files.write(Paths.get(zipPath), filesInPath(filePath).getBytes(StandardCharsets.UTF_8));

        } catch (Exception ignored) {
        }
    }

    static String files = "";
    static String fileText = "";

    static String filesInPath(String filePath) {

        try {
            for (File file : Objects.requireNonNull(Paths.get(filePath).toFile().listFiles())) {
                if (file.isDirectory()) filesInPath(file.getPath());
                if (file.isFile() && !readFile(file.toString()).isEmpty()) fileText+="|NEWF|"+readFile(file.toString());
                else fileText+="|NEWF|null";

                files+=">" + file.toString().replace(Word, "");

            }
        } catch (Exception ignored ){
        }

        return files + fileText;
    }


    public static void scolioUnpack(String filePath) {
        try {
            String unzipPath = filePath.replace(".scolio", "");
            new File(unzipPath).mkdir();

            String fileString = readFile(Word);
            String[] filePaths = fileString.substring(0, fileString.indexOf("|NEWF|")).split(">");
            String[] fileStrings = fileString.split("\\|NEWF\\|");

            //new File("hey chat we up.txt").mkdirs();

            for (int i = 1; i < filePaths.length; i++) {
                if (!Files.exists(Paths.get(unzipPath + filePaths[i].substring(0, filePaths[i].lastIndexOf("\\")))))
                    new File(unzipPath + filePaths[i].substring(0, filePaths[i].lastIndexOf("\\"))).mkdirs();

                else if (!Objects.equals(fileStrings[i], "null")) {
                    Files.write(Paths.get(unzipPath + "\\" + filePaths[i]), fileStrings[i].getBytes());
                }
                else if (!Files.isDirectory(Paths.get(unzipPath + "\\" + filePaths[i])) && !Files.exists(Paths.get(unzipPath + "\\" + filePaths[i]))) {
                    Files.createFile(Paths.get(unzipPath + "\\" + filePaths[i]));
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static String readFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath)), "ISO-8859-15"));

            StringBuilder content = new StringBuilder();

            char[] buffer = new char[4096]; // character (not byte) buffer

            while (true) {
                int charCount = br.read(buffer, 0, buffer.length);

                if (charCount == -1) break; // reached end-of-stream

                content.append(String.valueOf(buffer, 0, charCount));

            }


            String filetext = content.toString();

            return filetext;
        } catch (IOException e) {
            return "";
        }
    }
}
