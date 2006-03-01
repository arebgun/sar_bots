package agent.propulsion;

/**
 * @(#) Thruster.java
 */

class Thruster extends PropulsionModule
{
    private static final MAX_SPEED = 1.0;

    public void setCurrentSpeed( final double speed );

    public abstract AgentLocation moveToward( AgentLocation goal );
}
