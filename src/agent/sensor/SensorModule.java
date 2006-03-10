package agent.sensor;
/**
 * @(#) SensorModule.java
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
