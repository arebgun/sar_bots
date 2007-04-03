package obstacle;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import config.ConfigBobject;

public class RoundObstacle extends Obstacle
{

	public RoundObstacle(ConfigBobject config)
	{
		super(config);
		boundingRadius = config.getBoundingRadius();
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(color);
		g2.fill(new Ellipse2D.Float((float)location.getX() - (float)boundingRadius,
				(float)location.getY() - (float)boundingRadius,
				2f * (float)boundingRadius,
				2f * (float)boundingRadius));
	}
}
