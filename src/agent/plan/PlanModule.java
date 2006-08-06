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

public abstract class PlanModule
{
    protected ConfigAgent agentConfig;

    protected PlanModule( ConfigAgent config )
    {
        agentConfig = config;
    }

    public abstract AgentLocation getGoalLocation( AgentLocation location, Area sensorView );
}
