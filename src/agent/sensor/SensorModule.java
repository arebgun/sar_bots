package agent.sensor;
/**
 * @(#) SensorModule.java
 */

import agent.AgentLocation;

import java.awt.geom.Area;
import java.util.ArrayList;

public interface SensorModule
{
    public Area getView( AgentLocation loc );
}
