public class Camera{

    private int x,y;
    private int velX = 0,velY = 0;

    public Camera(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void update(Player player){
        x = -player.getX() + Launcher.WIDTH/2;
        y = -player.getY() + 100 + Launcher.HEIGHT/2;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
}