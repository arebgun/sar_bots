package agent.propulsion;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

class Thruster extends PropulsionModule
{
    public AgentLocation move( AgentLocation location, AgentLocation goal )
    {
	double goalX = goal.getX(), goalY = goal.getY(), goalDist = hypot( goalX - location.getX(), goalY - location.getY() );
	if ( goalDist > maxSpeed ) 
	    {
		goalX *= maxSpeed/goalDist;
		goalY *= maxSpeed/goalDist;
	    }
        return new AgentLocation( goalX, goalY, goal.getTheta() );
    }
}
