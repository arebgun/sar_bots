package agent.sensor;

import agent.AgentLocation;
import config.ConfigAgent;

import java.awt.geom.Area;

/**
 * Author: Anton Rebgun
 * Date:   Mar 20, 2006
 * Time:   7:52:27 PM
 */
public class Circle extends SensorModule
{
    public Circle( ConfigAgent config )
    {
        super( config );
    }

    public Area getView( AgentLocation loc )
    {
        return null;
    }
}
