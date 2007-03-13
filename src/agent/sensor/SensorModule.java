package agent.sensor;

/*
 * Class Name:    agent.sensor.SensorModule
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

// TODO: remove Area from SensorModule.java (possibly the entire class depending on how we will implement collision detection)
/**
 * Provides a common initialization routines for all sensor modules using the agent configuration
 * object supplied by the Simulator.
 */
public abstract class SensorModule
{
    protected ConfigAgent agentConfig;
    protected double radius;

    /**
     * Initializes basic sensor module state using the agent configuation object.
     *
     * @param config
     */
    public SensorModule( ConfigAgent config )
    {
        agentConfig = config;
        radius      = agentConfig.getSensorRange();
    }

    public abstract Area getView( AgentLocation loc );
}
