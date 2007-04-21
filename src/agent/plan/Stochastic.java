package agent.plan;

import agent.*;
import baseobject.*;
import config.ConfigBobject;
import env.Environment;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;

import obstacle.Obstacle;


public class Stochastic extends PlanModule
{
    private static Random rand = null;
    private agent.Agent.state agentState = agent.Agent.state.SEARCH;
	
    public Stochastic( ConfigBobject config )
    {
        super( config );
        if ( rand == null )
        {
            rand = new Random( objectConfig.getPlanSeed() );
        }
    }
    public agent.Agent.state getAgentState()
	{
		return agentState;
	}
    
    public AgentLocation getGoalLocation( Agent a )
    {
        double curX        = a.getLocation().getX();
        double curY        = a.getLocation().getY();
        double newX     = -1;
        double newY     = -1;
        double newTheta = a.getLocation().getTheta() ;
        
        double dist = 0;
        double bound = 0;

        //used to determine a max distance the agent may move
        //soundRadius is the radius of the circle that an agent 
        //makes noise in.
        double range = a.getSoundRadius();
        
        int limit      = 1000;
        boolean found = false;
        boolean good = true;
        
        while (!found && --limit > 0)
        {
        	//randomly choose a new heading for our agent
        	//then using that new heading, move in that direction 
        	//a random amount of range.
        	//since 0,0 is in the top left corner of the map and
        	//maxX,maxY is in the bottom right corner we add the 
        	//distance for x and subract the distance for y (y is
        	//opposite orientation from the cartisian co-ordinate system
        	range *= rand.nextDouble();
        	newTheta = rand.nextDouble() * 360;
        	double ang = Math.toRadians(newTheta);
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
        	
        	//check against agents heard
        	Iterator<Agent> heard = a.getAgentsHeard();
        	while ( heard.hasNext() && good)
        	{
        		Agent b = heard.next();
        		if (a.getObjectID() != b.getObjectID())
        		{
        			dist = Math.hypot((newX - b.getLocation().getX()), (newY - b.getLocation().getY()));
        			bound = a.getBoundingRadius() + b.getBoundingRadius();
        			if ( bound >= dist)
        			{
        				System.out.println("Agent : " + a.getObjectID() + "  hit agent : " + b.getObjectID());
        				good = false;
        			}
        		}
        	}
        	found = good;
        	
        }
        	
       	//check to make sure we are within the borders
        //only need to check if the location is a good one so far
        if (good)
       	{
        	Rectangle2D world = Environment.groundShape();
        	if (newX > 15 && newX < (world.getMaxX()-15) && newY > 15 && newY < (world.getMaxY()-15))
        		found = true;
        	else
        		found = false;  	
        }
        
        //if after 1000 tries we still don't have a new location
        //keep the current x and y location, but rotate the heading
        //counter-clockwise by 30 degrees.
        if ( !found )
        {
            newX     = curX;
            newY     = curY;
            newTheta += Math.PI/6;
        }
 		
		return new AgentLocation( newX, newY, newTheta );
    }
    
    public void Dead(Agent a)
	{
		
	}
    public void FlagCarrier(Agent a)
    {
    	
    }
    public void Guard(Agent a)
    {
    	
    }
    public void Attacking(Agent a)
    {
    	
    }
    public void Flee(Agent a)
    {
    	
    }
    public void Hide(Agent a)
    {
    	
    }
    public void Search(Agent a)
    {
    	
    }
    public void RecoverFlag(Agent a)
    {
    	
    }
    public void CleanUp(Agent a)
    {
    	
    }
 
    public void Patrol(Agent a)
    {
    	
    } 
}
