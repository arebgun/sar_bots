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
}
