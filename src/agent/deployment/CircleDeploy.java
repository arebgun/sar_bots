
package agent.deployment;

import config.ConfigAgent;
/*
import agent.*;
import baseObject.Bobject;
import env.Environment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import sim.Simulator;

public class CircleDeploy extends DeploymentStrategy{
	
	 private static Random rand = null;
	
	public CircleDeploy( ConfigAgent config )
    {
        super( config );
        if ( rand == null ) { rand = new Random( agentConfig.getDeploymentSeed() );
    }
	
	public AgentLocation getNextLocation( Agent a )
    {
		//how to put xCenter and yCenter in config file (inherited from hash)
		double xCenter	   =   120;
		double yCenter     =   120;
		double circleAngle =    rand.nextDouble();
		double x           =   -1;
        double y           =   -1;
        int limit          = 1000;
        boolean found      = false;
  
	
		while ( !found && --limit > 0 )
	    {
	        
			x = xCenter + (20*Math.sin(circleAngle));
	        y = yCenter + (20*Math.cos(circleAngle));
	        boolean good = true;
	        Iterator<Bobject> iter = Simulator.objectIterator();
	        while ( iter.hasNext())
	        {
	        	Bobject b = iter.next();
	        	double dist = Math.sqrt(x * b.getLoaction().getX() +
	        			y * b.getLocation().getY());
	        	
	        	if (a.getBoundingRadius() + b.getBoundingRadius() <= dist &&
	        			a.getObjectID() != b.getObjectID());
	        	
	        	else
	        		good = false;
	        }
	        found = good;
	    }
	    
	    if ( !found ) { throw new IllegalStateException( "unable to deploy agent #" + a.getObjectID() ); }
		
		return new AgentLocation( x, y, rand.nextGaussian() );
    }
}
*/