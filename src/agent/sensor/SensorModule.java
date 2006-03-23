package agent.sensor;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.Area;

public abstract class SensorModule
{
    protected ConfigAgent agentConfig;

    public SensorModule( ConfigAgent config )
    {
        agentConfig = config;
    }

    public abstract Area getView( AgentLocation loc );
}
