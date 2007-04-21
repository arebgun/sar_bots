package obstacle;

import java.awt.Graphics2D;
import java.awt.Polygon;
import config.ConfigBobject;
import java.awt.Color;
import agent.AgentLocation;

public class PolygonObstacle extends Obstacle
{

	private Polygon polyObstacle = null;
	
	public PolygonObstacle(ConfigBobject config) throws Exception
	{
		super(config);
		
	}
	public PolygonObstacle(Polygon newPoly, Color newColor, int newID)
	{
		super();
		polyObstacle = newPoly;
		color = newColor;
		objectID = newID;
		location = getCenter();
		boundingRadius = (int)getRadius();
		boundingShape = shapes.POLYGON;
		
	}
	public void update()
	{
		
	}
	
	public Polygon getPolygon()
	{
		return polyObstacle;
	}
	public void draw(Graphics2D g2)
	{
		g2.setColor(color);
		g2.fill(polyObstacle);
	}
	
	//rough approximation of the center of the obstacle
	//used to determine bounding circle for collision detection only.
	private AgentLocation getCenter()
	{
		int size = polyObstacle.npoints;
		int x = 0;
		int y = 0;
		for(int n = 0; n < size; n++)
		{
			x += polyObstacle.xpoints[n];
			y += polyObstacle.ypoints[n];
		}
		x = x/size;
		y = y/size;
		return (new AgentLocation(x,y,0));
	}
	
	private double getRadius()
	{
		int size = polyObstacle.npoints;
		double cX = location.getX();
		double cY = location.getY();
		double dist = 0;
		for (int n = 0; n < size; n++)
		{
			int tempX = polyObstacle.xpoints[n];
			int tempY = polyObstacle.ypoints[n];
			double tempDist = Math.hypot((cX-tempX), (cY-tempY));
			if (tempDist > dist)
				dist = tempDist;
		}
		return dist;
	}
	
	public void cleanup()
	{
		
	}
}
