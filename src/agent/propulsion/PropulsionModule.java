package agent.propulsion;

/*
 * Class Name:    agent.propulsion.PropulsionModule
 * Last Modified: 4/2/2006 3:1
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

public abstract class PropulsionModule
{
    protected double currentSpeed;
    protected double maxSpeed;
    protected double energyAmount;
    protected ConfigAgent agentConfig;

    protected PropulsionModule( ConfigAgent config )
    {
        agentConfig  = config;
        currentSpeed = 0;
        maxSpeed     = agentConfig.getPropulsionMaxSpeed();
        energyAmount = agentConfig.getPropulsionEnergyAmount();
    }

    public double getCurrentSpeed()
    {
        return currentSpeed;
    }

    public abstract AgentLocation move( AgentLocation location, AgentLocation goal );
}
