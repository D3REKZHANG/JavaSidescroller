import java.awt.image.BufferedImage;

public class SpriteSheet{

	private BufferedImage image;
	private int width,height;

	public SpriteSheet(BufferedImage image, int width, int height){
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public BufferedImage grabImage(int row, int col){
		return image.getSubimage((col*width)-width, (row*height)-height,width,height);
	}
}