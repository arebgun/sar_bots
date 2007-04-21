package agent.plan;

/*
 * Class Name:    agent.plan.PlanModule
 * Last Modified: 4/2/2006 3:5
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import java.util.Iterator;
import baseobject.Flag;
import agent.*;
import config.ConfigBobject;
/**
 * This class is responsible for the "brain" of the MAV.
 * Real AI algorithms can be used here and placed into
 * geGoalLocation() method.
 */
public abstract class PlanModule
{
    /**
     * Configuration class that specifies all agent properties.
     */
    protected ConfigBobject objectConfig;
    protected double guardDistance = 50.0;
    protected AgentLocation patrolLocation = null;
	protected Agent.state initialState;

    /**
     * Default constructor.
     *
     * @param config ConfigAgent class that specifies all agent properties
     */
    protected PlanModule( ConfigBobject config )
    {
        objectConfig = config;
         }

    /**
     * Calculates next location that the agent will move to.
     *
     * @param location current location of the agent
     * @param sensorView current sensor readings
     * @return next location that the agent will move to
     */
    
    public Agent.state getInitialState()
    {
    	return initialState;
    }
    public void setPatrolLocation(AgentLocation temp)
    {
    	patrolLocation = temp;
    }
    public abstract AgentLocation getGoalLocation(Agent a );
    
    public abstract void Dead(Agent a);
    public abstract void FlagCarrier(Agent a);
    public abstract void Guard(Agent a);
    public abstract void Attacking(Agent a);
    public abstract void Flee(Agent a);
    public abstract void Hide(Agent a);
    public abstract void Search(Agent a);
    public abstract void RecoverFlag(Agent a);
    public abstract void Patrol(Agent a);
    
    public abstract agent.Agent.state getAgentState();
    
//  recieves an agent, returns true if the agent sees an opponent, false otherwise
    protected boolean seeOpponent(Agent a)
    {
    	boolean temp = false;
    	Iterator seen = a.getAgentsSeen();
    	while (seen.hasNext() && !temp)
    	{
    		Agent b = (Agent)seen.next();
    		if (b.getTeamID() != a.getTeamID())
    			temp = true;
    	}
    	return temp;
    }
    
    //recieves an agent, returns the number of opposing agents seen
    protected int opponentsSeen(Agent a)
    {
    	int temp = 0;
    	Iterator seen = a.getAgentsSeen();
    	while (seen.hasNext())
    	{
    		Agent b = (Agent)seen.next();
    		if (b.getTeamID() != a.getTeamID() && b.isMobile())
    			temp++;
    	}
    	return temp;
    }
    
    //recieves an agent, returns true if the agents seens an opponents flag, false otherwise
    protected boolean opponentsFlagSeen(Agent a)
    {
    	boolean temp = false;
    	Iterator seen = a.getFlagsSeen();
    	while (seen.hasNext() && !temp)
    	{
    		Flag f = (Flag)seen.next();
    		if (f.getTeamID() != a.getTeamID())
    			temp = true;
    	}
    	return temp;
    }
    
    //recieves an agent, returns the location of the seen flag
    protected AgentLocation opponentsFlagLocation(Agent a)
    {
    	AgentLocation temp = null;
    	Iterator seen = a.getFlagsSeen();
    	while (seen.hasNext())
    	{
    		Flag f = (Flag)seen.next();
    		if (f.getTeamID() != a.getTeamID())
    			temp = f.getLocation();
    	}
    	return temp;
    }
    
    //recieves an agent, returns true if the agent sees its flag
    protected boolean ourFlagSeen(Agent a)
    {
    	boolean temp = false;
    	Iterator seen = a.getFlagsSeen();
    	while (seen.hasNext() && !temp)
    	{
    		Flag f = (Flag)seen.next();
    		if (f.getTeamID() == a.getTeamID())
    			temp = true;
    	}
    	return temp;
    }
    
    //recieves an agent, returns the location of the agent's team's flag
    protected AgentLocation ourFlagLoc(Agent a)
    {
    	AgentLocation temp = null;
    	Iterator seen = a.getFlagsSeen();
    	while (seen.hasNext())
    	{
    		Flag f = (Flag)seen.next();
    		if (f.getTeamID() == a.getTeamID())
    			temp = f.getLocation();
    	}
    	return temp;
    }
    protected boolean move(Agent a, double heading)
    {
    	int count = (int)a.sensorSight.getHalfAngle();
		boolean found = a.move(heading);
		if (!found)
		{
			for(int i = 1; i <= count && !found; i++)
			{
				found = a.move(heading + count);
				if (!found)
					found = a.move(heading - count);
			}
		}
		return found;
    }
    
    protected boolean moveRight(Agent a, double heading)
    {
    	int count = (int)a.sensorSight.getHalfAngle();
    	boolean found = a.move(heading);
    	if (!found)
    	{
    		for (int i = 1; i <= count && !found; i++)
    			found = a.move(heading + count);
    	}
    	return found;
    }
    
    protected boolean moveLeft(Agent a, double heading)
    {
    	int count = (int)a.sensorSight.getHalfAngle();
    	boolean found = a.move(heading);
    	if (!found)
    	{
    		for (int i = 1; i <= count && !found; i++)
    			found = a.move(heading - count);
    	}
    	return found;
    }
    protected boolean maxMove(Agent a, double heading)
    {
    	int count = (int)a.sensorSight.getHalfAngle();
		boolean found = a.maxMove(heading);
		if (!found)
		{
			for(int i = 1; i <= count && !found; i++)
			{
				found = a.maxMove(heading + count);
				if (!found)
					found = a.maxMove(heading - count);
			}
		}
		return found;
    }
    
    protected boolean maxMoveLeft(Agent a, double heading)
    {
    	int count = (int)a.sensorSight.getHalfAngle();
    	boolean found = a.maxMove(heading);
    	if (!found)
    	{
    		for (int i = 1; i <= count && !found; i++)
    			found = a.maxMove(heading - count);
    	}
    	return found;
    }
    
    protected boolean maxMoveRight(Agent a, double heading)
    {
    	int count = (int)a.sensorSight.getHalfAngle();
    	boolean found = a.maxMove(heading);
    	if (!found)
    	{
    		for (int i = 1; i <= count && !found; i++)
    			found = a.maxMove(heading + count);
    	}
    	return found;
    }
    //returns the first flag seen, if no flags are seen returns null
    protected Flag getFlagSeen(Agent a)
    {
    	Iterator<Flag> flag = a.getFlagsSeen();
    	while (flag.hasNext())
    	{
    		Flag f = (Flag)flag.next();
    		return f;
    	}
    	return null;
    }
    //returns -x degrees if myHeading > newHeading (turn right)
    //returns x degrees if newHeading > myHeading (turn left)
    //and 0 if the headings are the same.
    protected int turnDir(Agent a, double newHeading)
    {
    	int dir = 0;
    	double myHeading = a.getLocation().getTheta();
    	if (Math.abs(newHeading - myHeading) <= 180)
    		dir = (int)(newHeading - myHeading);
    	//myheading is in quad 4 and newHeading is in quad 1, turn left
    	else if (myHeading > newHeading)
    		dir = (int)((360 + newHeading) - myHeading);
    	//otherwise, newHeading is in 4 and myHeading is in 1, turn right
    	else
    		dir = (int)(newHeading - (myHeading + 360));
    	//System.out.println("my Heading : " + myHeading + " new Heading : " + newHeading + " direction : " + dir);
    	return dir;
    }
    
    protected void moveAgent(Agent a, double heading, double newHeading)
    {
    	boolean found = false;
    	int turn = turnDir(a,newHeading);
    	if (turn > 0)
    	{
    		if (turn > a.sensorSight.getHalfAngle());
    			turn = (int)a.sensorSight.getHalfAngle();
    		found = maxMoveLeft(a,(heading+turn));
    		if (!found)
    			found = moveLeft(a,(heading+turn));
    		//System.out.println("turned left");
    	}
    	else if (turn < 0)
    	{
    		turn = Math.abs(turn);
    		if (turn > a.sensorSight.getHalfAngle())
    			turn = (int)a.sensorSight.getHalfAngle();
    		turn *= -1;
    		found = maxMoveRight(a,(heading+turn));
    		if (!found)
    			found = moveRight(a,(heading+turn));
    		//System.out.println("turned right");
    	}
    	else
    	{
    		found = maxMove(a,heading);
    		if (!found)
    			found = move(a,heading);
    		//System.out.println("went straight");
    	}
		if (!found)
		{
			double newTurn = heading + a.sensorSight.getHalfAngle();//rand.nextDouble()*a.sensorSight.getArcAngle() + (heading - a.sensorSight.getHalfAngle());
			a.turnMove(newTurn);
		}	
    }
    
    protected boolean atBase(Agent a)
    {
    	boolean inBase = false;
    	Iterator<Agent> ag = a.getAgentsHeard();
    	while (ag.hasNext() && !inBase)
    	{
    		Agent b = (Agent)ag.next();
    		if (b.isBase())
    		{
    			double dist = Math.hypot((a.getLocation().getX() - b.getLocation().getX()),
    									 (a.getLocation().getY() - b.getLocation().getY()));
    			double bound = a.getBoundingRadius();
    			if (bound >= dist)
    				inBase = true;
    		}
    	}
    	return inBase;
    }
}
