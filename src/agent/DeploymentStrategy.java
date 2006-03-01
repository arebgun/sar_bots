package agent;

/**
 * Author: Anton Rebgun
 * Date:   Feb 24, 2006
 * Time:   8:51:45 PM
 */
public class DeploymentStrategy
{
    // put in a random gen in here (piggy back on Sim.config for the random seed)

    public AgentLocation getNextLocation( int id )
    {
        // TODO: Add logic here
        return new AgentLocation(0, 0, 0);
    }
}
