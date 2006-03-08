package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import sim.Simulator;
public abstract class PropulsionModule
{
    protected double currentSpeed;

    protected double maxSpeed;

    protected double energyAmount;

    public PropulsionModule() {
	currentSpeed = 0;
	maxSpeed     = Simulator.config.getPropulsionMaxSpeed();
	energyAmount = Simulator.config.getPropulsionEnergyAmount();
    }


    public double getCurrentSpeed()
    {
        return currentSpeed;
    }

    public abstract AgentLocation move( AgentLocation location, AgentLocation goal );
}
