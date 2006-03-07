package agent.plan;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

import agent.AgentLocation;

import java.awt.*;
import java.awt.geom.*;

public interface PlanModule
{
    public AgentLocation getGoalLocation( Area sensorView );
}
