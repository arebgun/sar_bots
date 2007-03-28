
package agent.deployment;

import config.ConfigBobject;

import agent.*;
import baseobject.Bobject;
import java.util.Iterator;
import java.util.Random;
import sim.Simulator;

public class CircleDeploy extends DeploymentStrategy{
	
	private static Random rand = null;
	private static double degree = 0;
	protected double circleRadius = 20.0;
	protected double xCenter = 120.0;
	protected double yCenter = 120.0;
	
	public CircleDeploy( ConfigBobject config )
    {
        super( config );
        if ( rand == null ) { rand = new Random( objectConfig.getDeploymentSeed() );}
        circleRadius = config.getCircleRadius();
        xCenter = config.getXCircle();
        yCenter = config.getYCircle();
    }
	
	public AgentLocation getNextLocation( Agent a )
    {
		//how to put xCenter and yCenter in config file (inherited from hash)
		double circleAngle =   0.0;
		double x           =   0;
        double y           =   0;
        int limit          = 1000;
        boolean found      = false;
	
		while ( !found && --limit > 0 )
	    {
			boolean good = true;
			circleAngle =    rand.nextDouble() * 360.0;
			x = xCenter + (circleRadius*Math.sin(circleAngle));
	        y = yCenter + (circleRadius*Math.cos(circleAngle));
	        Iterator<Bobject> iter = Simulator.objectIterator();
	        while ( iter.hasNext())
	        {
	        	Bobject b = iter.next();
	        	if (b.isPlaced() && a.getObjectID() != b.getObjectID())
	        	{
	        		double dist = Math.sqrt(Math.pow(x-b.getLocation().getX(),2) + Math.pow(y-b.getLocation().getY(), 2));
	        		if (a.getBoundingRadius() + b.getBoundingRadius() >= dist)
	        			good = false;
	        	}
		        found = good;
	        }
	       // if (found)
	        //	a.setPlaced(true);
	    }

	    if ( !found ) { throw new IllegalStateException( "unable to deploy agent #" + a.getObjectID() ); }
		a.setPlaced(true); //degree = (degree + 15.0 ) % 360.0;
		
		return new AgentLocation( x, y, rand.nextGaussian()*360.0 );
    }
}