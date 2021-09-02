package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * creates a JFrame and renders some text at a fontsize to grab the character widths. Not used by anything, just saved in case we need it in the future.
 */
public class FontWidthGrabber {

    final char[] alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ".toCharArray();

    public FontWidthGrabber() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(960, 640);
        JPanel p = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                System.out.print("{");
                for(char c : alphabet){
                    int width = g.getFontMetrics().stringWidth(c + "");
                    //System.out.print(width + ",");
                    System.out.println(c + ":  " + width);
                }
                System.out.println("}");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        };
        frame.add(p);
        frame.validate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new FontWidthGrabber();
    }
}
