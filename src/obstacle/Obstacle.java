package obstacle;

import baseobject.*;
import java.awt.Graphics2D;
import config.ConfigBobject;

public abstract class Obstacle extends Bobject
{

	//Obstacle Constructors
	public Obstacle(ConfigBobject conf)
	{
		this.config = conf;
		location = config.objectLocation();
		color = config.getObjectColor();
		soundRadius = config.getBoundingRadius();
		type = types.OBSTACLE;
	}
	
	//abstracted method getSoundRadius
	public int getSoundRadius()
	{
		return soundRadius;
	}
	
	public void reset()
	{
		
	}
	
	public abstract void draw(Graphics2D g2);
}
