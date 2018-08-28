import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class Player extends GameObject{
    
    public int w = 100, h = 74;
    private String lastDir = "L"; 
    private double gravity = 0.5;
    public int speed = 4;
    private final double MAX_VEL = 10;
    
    public boolean boosted = false;

    private SpriteSheet playerL;
    private SpriteSheet playerR;

    private Animation idleRight,idleLeft;
    private Animation idleSwordRight,idleSwordLeft;
    private Animation runRight,runLeft;
    private BufferedImage jumpAnim[][] = new BufferedImage[2][2];

    private boolean swordOut = false;
    private int atkCycle = 1;
    private int atkTimer = 0;
    private Animation atkAnim[][] = new Animation[2][3];
    private boolean attacking = false;

    Handler handler;
    Game game;

    public Player(int x, int y, Handler handler, Game game, ObjectId id){
        super(x,y,id);
        this.handler = handler;
        this.game = game;
        try{
            BufferedImage img1 = ImageIO.read(new File("../images/playerL.png"));
            BufferedImage img2 = ImageIO.read(new File("../images/playerR.png"));
            playerL = new SpriteSheet(img1,w,h);
            playerR = new SpriteSheet(img2,w,h);
        }catch(IOException e){
            System.err.println("Could not find spritesheet!");
        }

        idleRight = new Animation(10,playerR.grabImage(1,1),playerR.grabImage(1,2),playerR.grabImage(1,3),playerR.grabImage(1,4));
        idleSwordRight = new Animation(10, playerR.grabImage(6,4),playerR.grabImage(6,5),playerR.grabImage(6,6),playerR.grabImage(6,7));
        idleLeft = new Animation(10,playerL.grabImage(1,7),playerL.grabImage(1,6),playerL.grabImage(1,5),playerL.grabImage(1,4));
        idleSwordLeft = new Animation(10, playerR.grabImage(6,4),playerR.grabImage(6,3),playerR.grabImage(6,2),playerR.grabImage(6,1));
        runRight = new Animation(10,playerR.grabImage(2,2),playerR.grabImage(2,3),playerR.grabImage(2,4),playerR.grabImage(2,5),playerR.grabImage(2,6),playerR.grabImage(2,7));
        runLeft = new Animation(10,playerL.grabImage(2,6),playerL.grabImage(2,5),playerL.grabImage(2,4),playerL.grabImage(2,3),playerL.grabImage(2,2),playerL.grabImage(2,1));

        Animation[][] temp = {
            {
                new Animation(5,"once",playerR.grabImage(7,1),playerR.grabImage(7,2),playerR.grabImage(7,3),playerR.grabImage(7,4),playerR.grabImage(7,5),playerR.grabImage(7,6),playerR.grabImage(7,7)),
                new Animation(5,"once",playerR.grabImage(8,1),playerR.grabImage(8,2),playerR.grabImage(8,3),playerR.grabImage(8,4)),
                new Animation(5,"once",playerR.grabImage(8,5),playerR.grabImage(8,6),playerR.grabImage(8,7),playerR.grabImage(9,1),playerR.grabImage(9,2),playerR.grabImage(9,3))
            },
            {
                new Animation(5,"once",playerL.grabImage(7,7),playerL.grabImage(7,6),playerL.grabImage(7,5),playerL.grabImage(7,4),playerL.grabImage(7,3),playerL.grabImage(7,2),playerL.grabImage(7,1)),
                new Animation(5,"once",playerL.grabImage(8,7),playerL.grabImage(8,6),playerL.grabImage(8,5),playerL.grabImage(8,4)),
                new Animation(5,"once",playerL.grabImage(8,3),playerL.grabImage(8,2),playerL.grabImage(8,1),playerL.grabImage(9,7),playerL.grabImage(9,6),playerL.grabImage(9,5))
            }
        };
        for(int i=0;i<2;i++){
            for(int j=0;j<3;j++){
                atkAnim[i][j] = temp[i][j];
            }
        }

        BufferedImage[][] ttemp = {
            {playerR.grabImage(3,1),playerR.grabImage(3,2)},
            {playerL.grabImage(3,7),playerL.grabImage(3,6)}
        };
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                jumpAnim[i][j] = ttemp[i][j];
            }
        }
    }

    public void update(ArrayList<GameObject> objects){
        y += velY;
        if(!attacking) x += velX;
        
        falling = true;
        
        if(falling || jumping){
            velY += gravity;
            
            if(velY > MAX_VEL){
                velY = MAX_VEL;
            }
        }
        
        // Falling too low
        if(y > 1450){
            this.reset();
            handler.setLevel(Game.level);
        }

        // Last facing direction
        if(velX < 0) lastDir = "L";
        else if(velX > 0) lastDir = "R";    

        // Attack cycle reset
        atkTimer++;
        if(atkTimer > 200){
            atkCycle = 3;
            swordOut = false;
        }
        System.out.println(atkCycle);
        checkCollision();       
        if(velX != 0){
            runRight.runAnimation();
            runLeft.runAnimation();
        }else{
            idleLeft.runAnimation();
            idleRight.runAnimation();
            idleSwordRight.runAnimation();
        }
        if(attacking){
            for(int i=0;i<2;i++){
                for(int j=0;j<3;j++){
                    atkAnim[i][j].runAnimation();
                }
            }
        }
        boolean b = true;
        for(int i=0;i<2;i++){
            for(int j=0;j<3;j++){
                if(!atkAnim[i][j].isDone()){
                    b = false;
                }
            }
        }
        if(b){
            for(int i=0;i<2;i++){
                for(int j=0;j<3;j++){
                    atkAnim[i][j].reset();
                }
            }
            attacking = false;
        }
    }
    
    public void drawFrame(Graphics g){
     g.setColor(Color.red);
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(getBoundsL());
        g2d.draw(getBoundsR());
        g2d.draw(getBoundsT());
        g2d.draw(getBoundsB());
        g.drawLine(x-5,y,x+5,y);
        g.drawLine(x,y+5,x,y-5);
    }

    public void draw(Graphics g){
        if (lastDir.equals("R")){
            if (attacking){ 
                if(atkCycle == 1) atkAnim[0][0].draw(g,x-w/2,y-h/2);
                else if(atkCycle == 2) atkAnim[0][1].draw(g,x-w/2,y-h/2);
                else if(atkCycle == 3) atkAnim[0][2].draw(g,x-w/2,y-h/2);
            }else{
                if(jumping){
                    if(velY < 0) g.drawImage(jumpAnim[0][0],x-w/2,y-h/2,null);
                    else g.drawImage(jumpAnim[0][1],x-w/2,y-h/2,null);
                }else{ 
                    if (velX != 0) runRight.draw(g,x-w/2,y-h/2);
                    else 
                        if(swordOut) idleSwordRight.draw(g,x-w/2,y-h/2);
                        else idleRight.draw(g,x-w/2,y-h/2);
                }
            }
        }else{
            if (attacking){ 
                if(atkCycle == 1) atkAnim[1][0].draw(g,x-w/2,y-h/2);
                else if(atkCycle == 2) atkAnim[1][1].draw(g,x-w/2,y-h/2);
                else if(atkCycle == 3) atkAnim[1][2].draw(g,x-w/2,y-h/2);
            }else{
                if(jumping){
                    if(velY < 0) g.drawImage(jumpAnim[1][0],x-w/2,y-h/2,null);
                    else g.drawImage(jumpAnim[1][1],x-w/2,y-h/2,null);
                }else{ 
                    if (velX != 0) runLeft.draw(g,x-w/2,y-h/2);
                    else 
                        if(swordOut) idleSwordLeft.draw(g,x-w/2,y-h/2);
                        else idleLeft.draw(g,x-w/2,y-h/2);
                }
            }
        }
        drawFrame(g);
        for(int x=1;x<=7;x++){
            g.drawImage(playerR.grabImage(2,x),x*50,100,null);
        }
    }

    public void reset(){
        y = 200;
        x = 100;
    }

    public void attack(){
        if(!attacking){
            attacking = true;
            if(atkCycle == 3) atkCycle = 1;
            else atkCycle++;
            atkTimer = 0;
            swordOut = true;
        }
    }

    public boolean isAttacking(){
        return attacking;
    }

    public void checkCollision(){
        for(GameObject obj : handler.objects){
            if(obj.getId().equals(ObjectId.Platform)){
                if(getBoundsB().intersects(obj.getBounds())){
                    velY = 0;
                    falling = false;
                    jumping = false;
                    this.y = obj.getY() - this.h/2;

                    if(((Platform)obj).type == "ice") speed = 6;
                    else speed = 4;
                    
                }
                if(getBoundsT().intersects(obj.getBounds())){
                    velY = 0;
                    falling = true;
                    this.y = obj.getY() + 40 + this.h/2;
                }
                if(getBoundsL().intersects(obj.getBounds())){
                    velX = 0;
                    this.x = obj.getX() + 40 + this.w/2;
                }
                if(getBoundsR().intersects(obj.getBounds())){
                    velX = 0;
                    this.x = obj.getX() - this.w/2;
                }
            }else if(obj.getId().equals(ObjectId.Waypoint)){
                if(getBounds().intersects(obj.getBounds())){
                    this.reset();
                    break;
                }
            }
        }
    }

    public Rectangle getBounds(){
        return new Rectangle(x-w/2,y-h/2,w,h);
    }
    public Rectangle getBoundsL(){
        if(speed <= 3){
            return new Rectangle(x-w/2,y-h/2+h/20,w/20,h-h/10); 
        }
        return new Rectangle(x-w/2,y-h/2+h/20,w/5,h-h/10);
    }
    public Rectangle getBoundsR(){
        if(speed <= 3){
            return new Rectangle(x+w/2-w/20,y-h/2+h/20,w/20,h-h/10); 
        }
        return new Rectangle(x+w/2-w/5,y-h/2+h/20,w/5,h-h/10);
    }
    public Rectangle getBoundsT(){
        if(speed <= 3){
            return new Rectangle(x-w/2+w/20,y-h/2,w-(w/2+w/20)/2+w/6,h/5);
        }
        return new Rectangle(x-w/2+w/5+w/40,y-h/2,w-w/2+w/20,h/5);
    }
    public Rectangle getBoundsB(){
        if(speed <= 3){
            return new Rectangle(x-w/2+w/20,y-h/2+(h-h/5),w-(w/2+w/20)/2+w/6,h/5);
        }
        return new Rectangle(x-w/2+w/5+w/40,y-h/2+(h-h/5),w-w/2+w/20,h/5);
    }
}