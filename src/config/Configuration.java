package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

public class Configuration
{
    public Configuration( String configFilePath )
    {

    }

    public String getAgentDeploymentStrategy()
    {
        return "DeploymentStrategy";
    }

    public int numberOfScouts()
    {
        return 0;
    }

    public int numberOfWorkers()
    {
        return 0;
    }

}
