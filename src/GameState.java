import java.awt.*;
import javax.swing.*;

public abstract class GameState{
	public abstract void update();
	public abstract void draw(Graphics g);
}