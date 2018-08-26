import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Launcher extends Canvas implements Runnable{
    
    /**Dimensions*/
    public static int WIDTH, HEIGHT;
    
    /**Game thread*/
    private Thread thread;
    private boolean running = false;

    public static ArrayList <Integer> [] highscores;
    public static ArrayList <String> [] usernames;
    
    //private Menu menu = new Menu(this);
    public static Game game = new Game();
    //private LoadingScreen loadScreen = new LoadingScreen(this);
    //private LevelSelect lvlSelect = new LevelSelect(this);
    //private Highscore highscore = new Highscore(this);
    //private Endscreen endscreen = new Endscreen(this);
    
    public GameState currentGameState = game;

    public static void changeState(String state){
        //if(state.equals("endscreen")) currentGameState = endscreen;
        //else if(state.equals("lvlSelect")) currentGameState = lvlSelect;
    }

    public static boolean motionBlur = false;
    
    private int fadeAlpha = 0;
    private boolean fade = false;

    public void fade(){
        fade = true;
        fadeAlpha = 255;
    }

    private void init(){
        WIDTH = getWidth();
        HEIGHT = getHeight();
        
        //read();
        this.addKeyListener(new KeyInput());
        //this.addKeyListener(endscreen);
        //this.addMouseListener(new MouseInput(handler,this));
    }
    
    public synchronized void start(){
        if(!running){
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    public void run(){
        
        // Initialize needed variables
        init();
        
        // Time variables
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        
        // Game loop
        while(running){
            // Process the keys being pressed
            try{
                KeyInput.process();
            }catch(ConcurrentModificationException err){
                System.out.println();
            }            
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            for(;delta >= 1;delta--){
                this.update();
                updates++;
            }
            this.draw();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                frames = 0;
                updates = 0;
            }
        }
    }
    
    /**
     * Update that is called each frame
     */
    private void update(){
        currentGameState.update();

        // Compute Fade
        if(fade){
            fadeAlpha-=3;
        }
        if(fadeAlpha <= 0){
            fadeAlpha = 0;
            fade = false;            
        }
    }

    /**
     * Draws all graphics per frame
     */
    private void draw(){
        // Triple Buffering
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();

        currentGameState.draw(g);
        
        // Fade
        g.setColor(new Color(0,0,0,fadeAlpha));
        g.fillRect(0,0,WIDTH,HEIGHT);
        
        g.dispose();
        bs.show();
    }
    
    /**
     * Main method
     */
    public static void main(String[] args){
        new Window(790,590, "ICS4U0 Final", new Launcher());
    }
}

