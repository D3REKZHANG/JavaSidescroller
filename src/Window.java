/**
 * Name: Unsight Labs
 * Teacher: Ms. Krasteva
 * Date: June 7, 2018
 * Time Spent: 5 minutes
 */

/**
 * Change log
 *
 * May 4, 2018 - Added to create the JFrame and window for the game
 */

import javax.swing.*;
import java.util.*;
import java.awt.Dimension;

/**
 * Window class to handle the game window
 * 
 * @author Derek Zhang William Xu
 * @version 1
 */
public class Window{
    
    /**
     * Constructor to create new window
     * 
     * @param  w     window width
     * @param  h     window height
     * @param  title window title
     * @param  game  Game reference
     */
    public Window(int w,int h, String title, Launcher launcher){
        
        launcher.setPreferredSize(new Dimension(w,h));
        launcher.setMaximumSize(new Dimension(w,h));
        launcher.setMinimumSize(new Dimension(w,h));
        
        JFrame frame = new JFrame(title);
        frame.add(launcher);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        launcher.start();
    }
    
}
