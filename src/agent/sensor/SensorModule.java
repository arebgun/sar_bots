package agent.sensor;

/*
 * Class Name:    agent.sensor.SensorModule
 * Last Modified: 4/2/2006 3:3
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

public abstract class SensorModule
{
    protected ConfigAgent agentConfig;
    protected double radius;

    public SensorModule( ConfigAgent config )
    {
        agentConfig = config;
        radius      = agentConfig.getSensorRange();
    }

    public abstract Area getView( AgentLocation loc );
}
