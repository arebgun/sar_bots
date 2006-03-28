package agent.sensor;

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.Area;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */
public abstract class SensorModule
{
    protected ConfigAgent agentConfig;
    protected double radius;

    public SensorModule( ConfigAgent config )
    {
        agentConfig = config;
        radius = agentConfig.getSensorRange();
    }

    public abstract Area getView( AgentLocation loc );
}
