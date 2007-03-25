package agent.propulsion;

/*
 * Class Name:    agent.propulsion.PropulsionModule
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
import config.ConfigBobject;

/**
 * Propulsion module is responsible for keeping track of
 * maximum/current speed, amount of available fuel. Propulsion
 * module actually moves the agent (calculates the trajectory
 * between current and goal location and moves the agent to
 * the goal).
 */
public abstract class PropulsionModule
{
    /**
     * Current speed. The maximum distance agent can move in one
     * "turn" can't be bigger that current speed.
     */
    protected double currentSpeed;

    /**
     * Maximum speed
     */
    protected double maxSpeed;

    /**
     * Amount of available energy (fuel)
     */
    protected double energyAmount;

    /**
     * Configuration class that specifies all agent properties.
     */
    protected ConfigBobject objectConfig;

    /**
     * Default constructor. Initializes maximum speed and energy (fuel)
     * amount with values from configuration files.
     *
     * @param config ConfigAgent class that specifies all agent properties
     */
    protected PropulsionModule( ConfigBobject config )
    {
        objectConfig  = config;
        currentSpeed = 0;
        maxSpeed     = objectConfig.getPropulsionMaxSpeed();
        energyAmount = objectConfig.getPropulsionEnergyAmount();
    }

    /**
     * Gets the current speed of the agent.
     *
     * @return current speed
     */
    public double getCurrentSpeed()
    {
        return currentSpeed;
    }

    /**
     * Actually moves the agent (calculates the trajectory between current
     * and goal location and moves the agent to the goal).
     *
     * @param location current location of the agent
     * @param goal goal location of the agent
     * @return new agent location after moving
     */
    public abstract AgentLocation move( AgentLocation location, AgentLocation goal );
}
