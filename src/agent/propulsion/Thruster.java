package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import config.ConfigAgent;

import static java.lang.Math.hypot;

public class Thruster extends PropulsionModule
{
    public Thruster( ConfigAgent config )
    {
        super( config );
    }

    public AgentLocation move( AgentLocation location, AgentLocation goal )
    {
        double goalX = goal.getX(), goalY = goal.getY(), goalDist = hypot( goalX - location.getX(), goalY - location.getY() );
        if ( goalDist > maxSpeed )
        {
            goalX *= maxSpeed / goalDist;
            goalY *= maxSpeed / goalDist;
        }
        return new AgentLocation( goalX, goalY, goal.getTheta() );
    }
}
