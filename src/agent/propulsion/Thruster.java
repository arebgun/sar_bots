package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import config.ConfigAgent;

import static java.lang.Math.*;

public class Thruster extends PropulsionModule
{
    public Thruster( ConfigAgent config )
    {
        super( config );
    }

    public AgentLocation move( AgentLocation location, AgentLocation goal )
    {
        double curX = location.getX();
        double curY = location.getY();
        double goalX = goal.getX();
        double goalY = goal.getY();
        double goalDist = hypot( goalX - curX, goalY - curY );

        if ( goalDist > maxSpeed )
        {
            double theta = atan2( goalY - curY, goalX - curX );
            goalX = curX + maxSpeed * cos( theta );
            goalY = curY + maxSpeed * sin( theta );
        }

        return new AgentLocation( goalX, goalY, goal.getTheta() );
    }
}
