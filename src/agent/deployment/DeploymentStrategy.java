package agent.deployment;

import agent.AgentLocation;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *         Date:   Feb 24, 2006
 *         Time:   8:51:45 PM
 */

public class DeploymentStrategy
{
    public DeploymentStrategy()
    {

    }
    // put in a random gen in here (piggy back on Sim.config for the random seed)

    public AgentLocation getNextLocation( int id )
    {
        // TODO: Add logic here
        return new AgentLocation( 0, 0, 0 );
    }
}