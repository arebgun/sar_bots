package agent.sensor;

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.Area;

/**
 * @(#) SensorModule.java
 */
public abstract class SensorModule
{
    protected ConfigAgent agentConfig;

    public SensorModule( ConfigAgent config )
    {
        agentConfig = config;
    }

    public abstract Area getView( AgentLocation loc );
}
