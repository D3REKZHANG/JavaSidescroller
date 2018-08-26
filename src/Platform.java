/**
 * Name: Derek 
 * Teacher: Ms. Krasteva
 * Date: June 7, 2018
 * Time Spent: 15 min
 */

/*
    Change Log
    May 11, 2018 - Created to represent the blocks
    May 29, 2018 - Updated the graphics
 */

import java.awt.*;
import java.util.*;
/**
 * Class that represents the Player. It extends GameObject
 * 
 * @author Derek Zhang
 * @version 2
 */
public class Platform extends GameObject{

    public int w,h;
    public String type = "default";
    
    public Platform(int x, int y, int w, int h, ObjectId id){
        super(x,y,id);
        this.w = w;
        this.h = h;
    }

    public Platform(int x, int y, int w, int h, ObjectId id, String type){
        super(x,y,id);
        this.w = w;
        this.h = h;
        this.type = type;
    }

    public void update(ArrayList<GameObject> objects){
        // Empty
    }

    public void draw(Graphics g){
        if(type == "ice") g.setColor(new Color(155,215,235));
        else g.setColor(new Color(50,50,50));
        g.fillRect(x,y,w,h);
        g.setColor(new Color(100,100,100));
        g.fillRect(x+5,y+5,w-10,h-10);
    }   

    public Rectangle getBounds(){
        return new Rectangle(x,y,w,h);
    }
}