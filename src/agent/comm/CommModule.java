package agent.comm;

/*
 * Class Name:    agent.comm.CommModule
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

import config.ConfigAgent;

/**
 * Agent communcation module. Implement inter-agent communication.
 */
public abstract class CommModule
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
    protected CommModule( ConfigAgent config )
    {
        agentConfig = config;
    }
}
