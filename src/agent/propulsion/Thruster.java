package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

class Thruster extends PropulsionModule
{
    private static final double MAX_SPEED = 1.0;

    public void setCurrentSpeed( final double speed ) {}

    public AgentLocation moveToward( AgentLocation goal )
    {
        return new AgentLocation( 0, 0, 0 );
    }
}
