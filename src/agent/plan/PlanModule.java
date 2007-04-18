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
    
    public abstract AgentLocation getGoalLocation(Agent a );
    
    public abstract void Dead(Agent a);
    public abstract void FlagCarrier(Agent a);
    public abstract void Guard(Agent a);
    public abstract void Attacking(Agent a);
    public abstract void Flee(Agent a);
    public abstract void Hide(Agent a);
    public abstract void Search(Agent a);
    public abstract void RecoverFlag(Agent a);
    
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
    		if (b.getTeamID() != a.getTeamID())
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
    	return true;
    }
    
    //recieves an agent, returns the location of the seen flag
    protected AgentLocation opponentsFlagLocation(Agent a)
    {
    	AgentLocation temp = new AgentLocation(-1,-1,-1);
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
    	AgentLocation temp = new AgentLocation(-1,-1,-1);
    	Iterator seen = a.getFlagsSeen();
    	while (seen.hasNext())
    	{
    		Flag f = (Flag)seen.next();
    		if (f.getTeamID() == a.getTeamID())
    			temp = f.getLocation();
    	}
    	return temp;
    }
}
