package obstacle.deployment;

import java.util.Iterator;
import java.util.Random;
import baseobject.Bobject;
import obstacle.Obstacle;
import agent.AgentLocation;
import config.ConfigBobject;
import sim.Simulator;

public class Stochastic extends DeploymentStrategy
{
	protected int minX = 0;
	protected int maxX = 900;
	protected int minY = 0;
	protected int maxY = 580;
	private static Random rand = null;

   public Stochastic (ConfigBobject config)
	{
		super(config);
		if ( rand == null ) { rand = new Random( objectConfig.getDeploymentSeed() ); }
	    
		minX = config.getObsMinX();
		maxX = config.getObsMaxX();
		minY = config.getObsMinY();
		maxY = config.getObsMaxY();
		
	}
	
	public AgentLocation getNextLocation( Obstacle o )
	{
		double x           =   -1;
        double y           =   -1;
        int limit          = 1000;
        boolean found      = false;

        while ( !found && --limit > 0 )
        {
        	//find a random spot on the map
            x = rand.nextDouble() * (maxX - minX) + minX;
            y = rand.nextDouble() * (maxY - minY) + minY;
            //make sure nothing is in that spot too
           
            boolean good = true;
            Iterator<Bobject> iter = Simulator.objectIterator();
        	while ( iter.hasNext())
        	{
        		Bobject b = iter.next();
        		if(b.isPlaced())
        		{
        			double dist = Math.sqrt(x * b.getLocation().getX() +
        					y * b.getLocation().getY());
        			//if object b is not within the bounding radius of a, do nothing.
        			//if object b is within the bounding radius of a, then good = false
        			//and this location is not a good one to start with
        			if (o.getBoundingRadius() + b.getBoundingRadius() <= dist &&
        					o.getObjectID() != b.getObjectID())
        				;
        			else
        				good = false;
        		}
        	}
        	found = good; 
        }

        if ( !found ) { throw new IllegalStateException( "unable to deploy obstacle #" + o.getObjectID() ); }

        o.setPlaced(true);
        
        return new AgentLocation( x, y, rand.nextDouble() * 360 );
	}
}
