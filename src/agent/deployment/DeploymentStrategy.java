package agent.deployment;

/*
 * Class Name:    agent.deployment.DeploymentStrategy
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

import agent.Agent;
import agent.AgentLocation;
import config.ConfigAgent;

/**
 * Specifies the initial position of all MAVs.
 */
public abstract class DeploymentStrategy
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
    protected DeploymentStrategy( ConfigAgent config )
    {
        agentConfig = config;
    }

    /**
     * Return a Location object that describes x, y coordinates
     * as well heading of the agent specified by id.
     *
     * @param id unique agent id
     * @return AgentLocation object for the agent specified by id
     */
    public abstract AgentLocation getNextLocation( Agent a );
}
