package agent.plan;

import agent.*;
import baseobject.*;
import config.ConfigBobject;
import env.Environment;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;

import obstacle.Obstacle;


public class StochasticView extends PlanModule
{
    private static Random rand = null;

    public StochasticView( ConfigBobject config )
    {
        super( config );
        if ( rand == null )
        {
            rand = new Random( objectConfig.getPlanSeed() );
        }
    }
 
    // takes an angle and a heading, returns a random new heading
    // that is between (theta - arc/2 and theta + arc/2)
    // the angle returned is in radians.
    private double getAngle(double arc, double theta)
    {
    	double newTheta = theta;
    	newTheta = rand.nextDouble() * arc + (theta - arc/2);
    	while (newTheta > 360)
    		newTheta -= 360;
    	while (newTheta < 0)
    		newTheta += 360;
    	return Math.toRadians(newTheta);
    }
    
    private boolean inWorld(double x, double y)
    {
    	//assume the co-ords given are within the world
    	boolean isSafe = true;
    	//get the size of the world
    	Rectangle2D world = Environment.groundShape();
		
    	if ( x <= (world.getMinX() + 5) || x >= (world.getMaxX() - 5))
    		isSafe = false;
    	if ( y <= (world.getMinY() + 5) || y >= (world.getMaxY() - 5))
    		isSafe = false;
    	return isSafe;
    }
    
    public AgentLocation getGoalLocation( Agent a )
    {
        double curX        = a.getLocation().getX();
        double curY        = a.getLocation().getY();
        double curTheta    = a.getLocation().getTheta() ;
        
        double newX     = -1;
        double newY     = -1;
        double newTheta = a.getLocation().getTheta() ;
        
        double dist = 0;
        double bound = 0;

        int range = a.getSoundRadius();
        
        int limit      = 1000;
        boolean found = false;
        boolean good = true;
        double arc = a.sensorSight.getArcAngle();
        
  		        
        while (!found && --limit > 0)
        {
        	double ang = getAngle(arc, curTheta);
        	
        	newTheta = Math.toDegrees(ang);
        	newX = curX + range * Math.cos(ang);
        	newY = curY - range * Math.sin(ang);
        	
        	//check against agents
        	//get the array of seen agents, go through each agent comparing the distance from
        	//the center of the agent to the center of this agent to the distance of the bounding
        	//radi for both agents. if the distance is less then the bounding distance the there 
        	//is a collision and this new position is invalid. 
        	//check against obstacles and agents heard is done the same way.
        	Iterator<Agent> iter = a.getAgentsSeen();
        	while ( iter.hasNext() && good)
        	{
        		Agent b = iter.next();
        		if (a.getObjectID() != b.getObjectID())
        		{
        			dist = Math.hypot((newX - b.getLocation().getX()), (newY - b.getLocation().getY()));
        			bound = a.getBoundingRadius() + b.getBoundingRadius();
        			if ( bound >= dist)
        				good = false;
        		}
        	}
        	found = good;
        	
        	//check against obstacles
        	Iterator<Obstacle> obs = a.getObstaclesSeen();
        	while ( obs.hasNext() && good)
        	{
        		Obstacle o = obs.next();
        		dist = Math.hypot((newX - o.getLocation().getX()),(newY - o.getLocation().getY()));
        		bound = a.getBoundingRadius() + o.getBoundingRadius();
        		if (bound >= dist)
        			good = false;
        	}
        	found = good;
         	
//        	check against agents heard
        	Iterator<Agent> heard = a.getAgentsHeard();
        	while ( heard.hasNext() && good)
        	{
        		Agent b = heard.next();
        		if (a.getObjectID() != b.getObjectID())
        		{
        			dist = Math.hypot((newX - b.getLocation().getX()), (newY - b.getLocation().getY()));
        			bound = a.getBoundingRadius() + b.getBoundingRadius();
        			if ( bound >= dist)
        				good = false;
        		}
        	}
        	found = good;
       	
        	//check against flags seen
        	Iterator<Flag> flag = a.getFlagsSeen();
        	while (flag.hasNext() && good)
        	{
        		Flag f = flag.next();
        		dist = Math.hypot((newX - f.getLocation().getX()),(newY - f.getLocation().getY()));
        		bound = a.getBoundingRadius() + f.getBoundingRadius();
        		if (bound >= dist && (f.getOwner() != a.getObjectID()))
        		{
        			good = false;
        			if (!f.getOwned())
        			{
        				a.pickUpFlag(f);
        			}
        		}
        	}
        	found = good;
       }
        
        //ok we have new location now, check to see if this location is within the world boundry
        if (found)
        	found = inWorld(newX, newY);
        
        //if after 1000 tries we still don't have a new location
        //keep the current x and y location, but rotate the heading
        //counter-clockwise by 30 degrees.
        if ( !found )
        {
            newX     = curX;
            newY     = curY;
            newTheta = curTheta + a.sensorSight.getHalfAngle();
        }
        AgentLocation newAgentLocation = new AgentLocation(newX, newY, newTheta); 
        a.setLocation(newAgentLocation);
 		a.checkSensors();
 		Iterator<Agent> iter = a.getAgentsSeen();
    	while ( iter.hasNext())
    	{
    		Agent b = iter.next();
    		if (a.getTeamID() != b.getTeamID())
    		{
    			b.decrementHealth(1);
    		}
    	}
		return new AgentLocation( newX, newY, newTheta );
    }
    
 
}
