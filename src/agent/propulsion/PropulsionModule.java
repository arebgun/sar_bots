package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import config.ConfigAgent;

public abstract class PropulsionModule
{
    protected double currentSpeed;
    protected double maxSpeed;
    protected double energyAmount;
    protected ConfigAgent agentConfig;

    public PropulsionModule( ConfigAgent config )
    {
        agentConfig = config;
        currentSpeed = 0;
        maxSpeed = agentConfig.getPropulsionMaxSpeed();
        energyAmount = agentConfig.getPropulsionEnergyAmount();
    }

    public double getCurrentSpeed()
    {
        return currentSpeed;
    }

    public abstract AgentLocation move( AgentLocation location, AgentLocation goal );
}
