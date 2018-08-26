import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Handler{
    public ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private Camera cam;
    private BufferedImage level1,level2,level3,tutlvl;

    /**
     * Constructor
     * @param  cam Camera reference
     */
    public Handler(Camera cam){
        this.cam = cam;

        try{
            level1 = ImageIO.read(new File("../images/level_1.png"));
            //level2 = ImageIO.read(new File("../images/LLevel 2.png"));
            //level3 = ImageIO.read(new File("../images/LLevel 3.png"));
        }catch(IOException e){
            System.err.println("Could not find level images!");
        }
    }

    /**
    * Loops through objects and calls their update methods, so each object doesn't have 
    * to be updated manually.
    */
    public void update(){
        for(int x=0;x<objects.size();x++){
            objects.get(x).update(objects);
        }
    }

    /**
    * Loops through objects and calls their draw methods.
    * @param g [description]
    */
    public void draw(Graphics g){
        int n = 0;
        for(int x=0;x<objects.size();x++){
            GameObject obj = objects.get(x);
            // Optimization: Draw only on screen
            if(obj.getId().equals(ObjectId.Platform)){
                if((Math.abs(Game.player.getX()-obj.getX())>=440 || Math.abs(Game.player.getY()-obj.getY())>=540) && obj.getX() >= 800){
                    continue;
                }
            }else if(obj.getId().equals(ObjectId.Text)){
                if(Math.abs(Game.player.getX()-obj.getX())>=700 || Math.abs(Game.player.getY()-obj.getY())>=540){
                    continue;
                }
            }
            obj.draw(g);
            n++;
        }
    }

    /**
    * Add GameObject to objects ArrayList
    * @param obj GameObject to be added
    */
    public void add(GameObject obj){
        this.objects.add(obj);
    }

    /**
    * Remove GameObject from objects ArrayList
    * @param obj GameObject to remove
    */
    public void remove(GameObject obj){
        this.objects.remove(obj);
    }

    /**
     * Loads level by image
     * @param image BufferedImage
     */
    public void loadImageLevel(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();

        int txtcount = 0;

        for(int x=0;x<w;x++){
            for(int y=0;y<h;y++){
                int pixel = image.getRGB(x,y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if(red == 0 && green == 0 && blue == 0){
                    this.add(new Platform(x*40,y*40,40,40,ObjectId.Platform));
                }else if(red == 0 && green == 255 && blue == 255){
                    this.add(new Platform(x*40,y*40,40,40,ObjectId.Platform,"ice"));
                }
            }
        }
        Game.score = 60000;
    }

    public void setLevel(int level){
        this.clearLevel();
        Game.level = level;
        switch(level){
            case 1:
              this.loadImageLevel(level1);
              break;
            case 2:
              this.loadImageLevel(level2);
              break;
            case 3:
              this.loadImageLevel(level3);
              break;
            case 4:
              this.loadImageLevel(tutlvl);
              break;
        }
        this.add(Game.player);
        Game.player.setX(100);
    }

    private void clearLevel(){
        this.objects.clear();
    }

}