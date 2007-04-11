package obstacle.deployment;

import obstacle.Obstacle;
import agent.AgentLocation;
import config.ConfigBobject;

public abstract class DeploymentStrategy
{
    protected ConfigBobject objectConfig;
    
    protected DeploymentStrategy( ConfigBobject config )
    {
        objectConfig = config;
    }
    public abstract AgentLocation getNextLocation( Obstacle o );
}

