package agent.plan;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

import java.awt.geom.Area;

public interface PlanModule
{
    public AgentLocation getGoalLocation( Area sensorView );
}
