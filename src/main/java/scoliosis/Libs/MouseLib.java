package scoliosis.Libs;

import java.awt.*;
import java.awt.event.*;

import static scoliosis.mainjframe.mainframe;
import static scoliosis.mainjframe.textbox;

public class MouseLib implements AWTEventListener {

    public static boolean leftclicked = false;
    public static boolean middleclicked = false;
    public static boolean rightclicked = false;
    public static boolean otherclicked = false;

    public void eventDispatched(AWTEvent event) {
        // prob lowers fps or smth idrk, however needed for the keylistner to work
        mainframe.requestFocus();

        if (event.toString().contains("MOUSE_PRESSED")) {
            int mousenumber = Integer.parseInt(event.toString().split("button=")[1].split(",")[0]);
            if (mousenumber == 1) leftclicked = true;
            else if (mousenumber == 2) middleclicked = true;
            else if (mousenumber == 3) rightclicked = true;
            else otherclicked = true;
        }

        if (event.toString().contains("MOUSE_RELEASED")) {
            int mousenumber = Integer.parseInt(event.toString().split("button=")[1].split(",")[0]);
            if (mousenumber == 1) leftclicked = false;
            else if (mousenumber == 2) middleclicked = false;
            else if (mousenumber == 3) rightclicked = false;
            else otherclicked = false;
            
        }
    }

    public static boolean isMouseOverCoords(int x, int y, int width, int height) {
        int mx = MouseInfo.getPointerInfo().getLocation().x - mainframe.getX();
        int my = MouseInfo.getPointerInfo().getLocation().y - mainframe.getY();

        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y =  (int) (y/270f *(float) (mainframe.getHeight()));
        width = (int) (width/480f *(float) (mainframe.getWidth()));
        height =  (int) (height/270f *(float) (mainframe.getHeight()));

        return mx >= x+10 && mx <= x+10 + width && my >= y+40 && my <= y+40 + height;
    }


    public static int mousexcoord(float divnum) {
        return (int) (((MouseInfo.getPointerInfo().getLocation().x - mainframe.getX() - (10f/ (mainframe.getWidth()/480f))) / divnum) / (mainframe.getWidth()/480f));
    };

    public static int realmousexcoord() {
        return (int) (((MouseInfo.getPointerInfo().getLocation().x - mainframe.getX() - (10f/ (mainframe.getWidth()/480f)))) / (mainframe.getWidth()/480f));
    };

    public static int mouseycoord(float divnum) {
        return (int) (((MouseInfo.getPointerInfo().getLocation().y - mainframe.getY() - (36f/(mainframe.getHeight()/270f))) / divnum) / (mainframe.getHeight()/270f));
    };

    public static int realmouseycoord() {
        return (int) (((MouseInfo.getPointerInfo().getLocation().y - mainframe.getY() - (36f/(mainframe.getHeight()/270f)))) / (mainframe.getHeight()/270f));
    };
}
