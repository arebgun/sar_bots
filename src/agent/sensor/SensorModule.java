package agent.sensor;
/**
 * @(#) SensorModule.java
 */

import agent.AgentLocation;

import java.awt.geom.Area;

public interface SensorModule
{
    public Area getView( AgentLocation loc );
}
