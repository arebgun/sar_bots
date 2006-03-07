package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

class Thruster extends PropulsionModule
{
    private static final double MAX_SPEED = 1.0;  // extract from config file

    public AgentLocation move( AgentLocation location, AgentLocation goal )
    {
        return new AgentLocation( 0, 0, 0 );
    }
}
