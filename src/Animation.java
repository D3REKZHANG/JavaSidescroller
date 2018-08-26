import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Animation{

	private int speed,frames,delay = 0, index = 0;
	private BufferedImage[] images;
	private BufferedImage currentImage;
	private String type = "loop";
	private boolean done = false;

	public Animation(int speed, BufferedImage... args){
		this.speed = speed;
		this.frames = args.length;
		this.images = new BufferedImage[frames];
		for(int i=0;i<frames;i++){
			images[i] = args[i];
		}
	}

	public Animation(int speed, String type, BufferedImage... args){
		this.speed = speed;
		this.frames = args.length;
		this.images = new BufferedImage[frames];
		for(int i=0;i<frames;i++){
			images[i] = args[i];
		}
		this.type = type;
	}

	public void runAnimation(){
		if(!done){
			delay++;
			if (delay > speed){
				delay = 0;
				nextFrame();
			}
		}
	}

	public void nextFrame(){
		currentImage = images[index];
		index++;
		if(index > frames-1){ 
			if(type == "once") done = true;
			index = 0;
		}
	}

	public boolean isDone(){
		return done;
	}

	public void reset(){
		done = false;
	}

	public void draw(Graphics g,int x,int y){
		g.drawImage(currentImage,x,y,null);
	}
}