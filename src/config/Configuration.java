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

    public String agentDeploymentStrategy()
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


    public String scoutSensor()
    {

        return null;
    }

    public String workerSensor()
    {

        return null;
    }

    public double scoutSensorRange()
    {

        return 0;
    }

    public double workerSensorRange()
    {

        return 0;
    }
}
