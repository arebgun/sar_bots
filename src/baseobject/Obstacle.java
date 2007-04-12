package baseobject;

import agent.AgentLocation;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import config.ConfigBobject;

public class Obstacle extends Bobject{

	//Obstacle Constructors
	public Obstacle(ConfigBobject conf)
	{
		this.config = conf;
		location = config.objectLocation();
		color = config.getObjectColor();
		boundingRadius = config.getBoundingRadius();
		soundRadius = config.getBoundingRadius();
		type = types.OBSTACLE;
	}
	
	public Obstacle(AgentLocation loc, Color col, int rad, int id)
	{
		location = loc;
		color = col;
		boundingRadius = rad;
		objectID = id;
		soundRadius = boundingRadius;
		type = types.OBSTACLE;
	}
	
	public Obstacle(AgentLocation loc, Color col, int rad, int id, int sound)
	{
		location = loc;
		color = col;
		boundingRadius = rad;
		objectID = id;
		soundRadius = sound;
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
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(color);
		g2.fill(new Ellipse2D.Float((float)location.getX(),
				(float)location.getY(),
				(float)boundingRadius,
				(float)boundingRadius));

	}
}
