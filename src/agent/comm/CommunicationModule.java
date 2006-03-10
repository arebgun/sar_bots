package agent.comm;

import config.ConfigAgent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

public abstract class CommunicationModule
{
    protected ConfigAgent agentConfig;

    public CommunicationModule( ConfigAgent config )
    {
        agentConfig = config;
    }
}
