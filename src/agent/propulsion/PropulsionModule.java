package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import agent.sensor.SensorModule;
import agent.plan.PlanModule;

public abstract class PropulsionModule
{
    protected double currentSpeed;

    protected double maximumSpeed;

    protected double energyAmount;

    public double getCurrentSpeed()
    {
        return currentSpeed;
    }

    public abstract AgentLocation move( AgentLocation location, AgentLocation goal ); 
}
