package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

public abstract class PropulsionModule
{
    protected double currentSpeed;

    protected double maximumSpeed;

    protected double energyAmount;

    public double getCurrentSpeed()
    {
        return currentSpeed;
    }

    public abstract void setCurrentSpeed( final double speed );

    public abstract AgentLocation moveToward( AgentLocation goal );
}
