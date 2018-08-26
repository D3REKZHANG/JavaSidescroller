import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Game extends GameState{

    public static int level = 1;
    
    public static Handler handler;
    public static Camera cam;
    public static Player player;
    
    public static int score = 60000;

    public Game(){
        cam = new Camera(0,0);
        handler = new Handler(cam);
        player = new Player(200,100,handler,this,ObjectId.Player);

        handler.add(player);
    }

    public void update(){
        handler.update();
        cam.update(player);
    }

    public void draw(Graphics g){
        int motionAlpha = ((Launcher.motionBlur) ? 20 : 255);

        Graphics g2d = (Graphics2D) g;
        
        // Background
        g.setColor(new Color(0,0,0,motionAlpha));
        g.fillRect(0,0,Launcher.WIDTH,Launcher.HEIGHT);
        
        // Camera Area of Effect
        if(player.getX() > 400)
            g2d.translate(cam.getX(),0);
        g2d.translate(0,cam.getY());
        
        handler.draw(g);

        if(player.getX() > 400)
            g2d.translate(-cam.getX(),0);
        g2d.translate(0,-cam.getY());
        // End of Camera AOE  
    }
}