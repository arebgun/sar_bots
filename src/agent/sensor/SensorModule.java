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

import config.ConfigBobject;
import java.util.ArrayList;

import obstacle.Obstacle;
import agent.Agent;
import baseobject.*;

/**
 * Provides a common initialization routines for all sensor modules using the agent configuration
 * object supplied by the Simulator.
 */
public abstract class SensorModule
{
    protected ConfigBobject objectConfig;
	protected double arcAngle;
	protected double length;
	protected double radius;

    /**
     * Initializes basic sensor module state using the agent configuation object.
     *
     * @param config
     */
    public SensorModule( ConfigBobject config )
    {
        objectConfig = config;
    }
    public abstract ArrayList<Agent> getSightAgents(Agent a);
    public abstract ArrayList<Obstacle> getSightObstacles(Agent a);
    public abstract ArrayList<Flag> getSightFlags(Agent a);
    public abstract ArrayList<Agent> getHeardAgents(Agent a);
    public double getArcAngle() {
		return arcAngle;
	}
	
	public double getlength() {
		return length;
	}
    public double getHearingRadius()
    {
    	return radius;
    }
    
}
