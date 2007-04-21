package obstacle;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import baseobject.Bobject;

import config.ConfigBobject;

public class RoundObstacle extends Obstacle
{
	private static Random rand = null;

	public RoundObstacle(ConfigBobject config) throws Exception
	{
		super(config);
		if ( rand == null ) { rand = new Random( config.getDeploymentSeed() ); }
	    
		boundingRadius = config.getBoundingRadius();
		if (boundingRadius <= 0)
			boundingRadius = (int)(rand.nextDouble() * 25.0);
		boundingShape = Bobject.shapes.CIRCLE;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(color);
		g2.fill(new Ellipse2D.Float((float)location.getX() - (float)boundingRadius,
				(float)location.getY() - (float)boundingRadius,
				2f * (float)boundingRadius,
				2f * (float)boundingRadius));
	}
	
	public void update()
	{
		
	}
	
	public void cleanup()
	{
		
	}
}