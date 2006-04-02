package agent.deployment;

/*
 * Class Name:    agent.deployment.DeploymentStrategy
 * Last Modified: 4/2/2006 2:50
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

public abstract class DeploymentStrategy
{
    protected ConfigAgent agentConfig;

    protected DeploymentStrategy( ConfigAgent config )
    {
        agentConfig = config;
    }

    public abstract AgentLocation getNextLocation( int id );
}
