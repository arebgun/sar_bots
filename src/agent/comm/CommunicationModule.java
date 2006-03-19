package agent.comm;

import config.ConfigAgent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
public abstract class CommModule
{
    protected ConfigAgent agentConfig;

    protected CommModule( ConfigAgent config )
    {
        agentConfig = config;
    }
}
