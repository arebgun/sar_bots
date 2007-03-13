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

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.Area;
// TODO: remove Area from PlanModule.java
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
    protected ConfigAgent agentConfig;

    /**
     * Default constructor.
     *
     * @param config ConfigAgent class that specifies all agent properties
     */
    protected PlanModule( ConfigAgent config )
    {
        agentConfig = config;
    }

    /**
     * Calculates next location that the agent will move to.
     *
     * @param location current location of the agent
     * @param sensorView current sensor readings
     * @return next location that the agent will move to
     */
    
    // TODO: remove Area from GetGoalLocation
    public abstract AgentLocation getGoalLocation( AgentLocation location, Area sensorView );
}
