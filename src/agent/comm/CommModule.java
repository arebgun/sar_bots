package agent.comm;

/*
 * Class Name:    agent.comm.CommModule
 * Last Modified: 4/2/2006 2:49
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

public abstract class CommModule
{
    protected ConfigAgent agentConfig;

    protected CommModule( ConfigAgent config )
    {
        agentConfig = config;
    }
}
