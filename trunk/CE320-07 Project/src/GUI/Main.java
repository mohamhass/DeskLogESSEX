package GUI;

//This class should not be tampered with, please use other classes to build you program as this should be clean. - ka16693


import javax.swing.*;

public class  Main {
    public static void main(String[] args) {
        Main.interfaceSetUp();
    }

    private static void interfaceSetUp() {
        Frame frame = new Frame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Creates the new frame which enables GUI and is displayed. - ka16693
    }
}
