package baseobject;

import agent.AgentLocation;
import java.awt.Color;

public class Obstacle extends Bobject{

	//Obstacle Constructors
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
	}
	
	//abstracted method getSoundRadius
	public int getSoundRadius()
	{
		return soundRadius;
	}
}
