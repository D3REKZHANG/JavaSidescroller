/**
 * Name: Unsight Labs
 * Teacher: Ms. Krasteva
 * Date: June 7, 2018
 * Time Spent: 30 min
 */

/*
    Change Log
    May 11, 2018 - Created for handling key input
    May 15, 2018 - Added better movement
    May 26, 2018 - Added new way to process keys
 */

import java.awt.event.*;
import java.util.*;

/**
 * KeyInput class to get keyboard input
 *
 * @author Derek Zhang
 * @version 3
 */
public class KeyInput extends KeyAdapter{
    
    /**Left right pressed boolean to improve movement*/
    private static boolean leftPressed = false, rightPressed = false;

    /** Set of all keys being pressed down */
    private static Set<Integer> keysDown = new HashSet<Integer>();

    public void keyPressed(KeyEvent e){
        keysDown.add(e.getKeyCode());
    }

    public static void process(){
        for(int key : keysDown){
            Player p = Game.player;
            if(!p.isAttacking()){
                if(key == KeyEvent.VK_LEFT) {
                    p.setVelX(-p.speed);
                    leftPressed = true;
                }else if(key == KeyEvent.VK_RIGHT){
                    p.setVelX(p.speed);
                    rightPressed = true;
                }else if((key == KeyEvent.VK_UP) && (!p.isJumping() || p.boosted)) {
                    p.setVelY(Math.max(-(p.speed+5),-15));
                    p.setJumping(true);
                }
                if(key == KeyEvent.VK_SPACE){
                    p.attack();
                }
            }
            if(key == KeyEvent.VK_ESCAPE){
                System.exit(1);
            }
            if(key == KeyEvent.VK_L){
                p.speed = 5;
                p.boosted = true;
                Launcher.motionBlur = true;
            }
        }
    }
    
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        Player p = Game.player;

        //Camera Debugging
        if(key == KeyEvent.VK_LEFT) {
            if(!rightPressed) p.setVelX(0);
            leftPressed = false;
            keysDown.remove(KeyEvent.VK_LEFT);
        }else if(key == KeyEvent.VK_RIGHT){
            if(!leftPressed) p.setVelX(0);
            rightPressed = false;
            keysDown.remove(KeyEvent.VK_RIGHT);
        }else if(key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
            keysDown.remove(KeyEvent.VK_SPACE);
            keysDown.remove(KeyEvent.VK_UP);
        }
        if(key == KeyEvent.VK_O){
            Launcher.changeState("endscreen");
            Game.player.reset();
        }
        if(key == KeyEvent.VK_L){
           keysDown.remove(KeyEvent.VK_L);
           p.speed = 2;
           p.boosted = false;
           Launcher.motionBlur = false;
        }
        if(key == KeyEvent.VK_M){
            Launcher.motionBlur = !Launcher.motionBlur;
        }
    }
}