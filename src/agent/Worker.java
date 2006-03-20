package agent;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import config.ConfigAgent;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Worker extends Agent
{
    public Worker( ConfigAgent config ) throws Exception
    {
        super( config );
    }
}
