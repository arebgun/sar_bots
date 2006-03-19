package agent.plan;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.Area;

public abstract class PlanModule
{
    protected ConfigAgent agentConfig;

    protected PlanModule( ConfigAgent config )
    {
	agentConfig = config;
    }

    public abstract AgentLocation getGoalLocation( AgentLocation location, Area sensorView );
}
