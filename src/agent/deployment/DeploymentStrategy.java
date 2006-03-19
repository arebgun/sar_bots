package agent.deployment;

import agent.AgentLocation;
import config.ConfigAgent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *         Date:   Feb 24, 2006
 *         Time:   8:51:45 PM
 */

public abstract class DeploymentStrategy
{
    protected ConfigAgent agentConfig;

    protected DeploymentStrategy( ConfigAgent config )
    {
	agentConfig = config;
    }

    public abstract AgentLocation getNextLocation( int id );
}
