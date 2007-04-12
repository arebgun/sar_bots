package obstacle;

import obstacle.deployment.*;
import baseobject.*;
import java.awt.Graphics2D;
import config.ConfigBobject;

public abstract class Obstacle extends Bobject
{

    public DeploymentStrategy deployStrategy;
    protected enum obstacleTypes {ROUND, TRIANGLE, RECTANGLE}
	protected obstacleTypes obstacleType;
	//Obstacle Constructors
	public Obstacle(ConfigBobject conf) throws Exception
	{
		this.config = conf;
		String deployClass     = config.getDeploymentName();
		initialize(deployClass);
		
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
	public obstacleTypes getObstacleType()
	{
		return obstacleType;
	}
	
    private void initialize( String deployClass) throws Exception
    {
        Class aC       = ConfigBobject.class;
        Class loader   = Class.forName( deployClass, true, this.getClass().getClassLoader() );
        deployStrategy = (DeploymentStrategy) loader.getConstructor( aC ).newInstance( config );
    }
    
	public abstract void draw(Graphics2D g2);
}
