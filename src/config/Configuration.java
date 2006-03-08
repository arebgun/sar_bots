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

    public int getNumberOfScouts()
    {
        return 0;
    }

    public int getNumberOfWorkers()
    {
        return 0;
    }


    public String getScoutSensor()
    {

        return null;
    }

    public String getWorkerSensor()
    {

        return null;
    }

    public double getScoutSensorRange()
    {

        return 0;
    }

    public double getWorkerSensorRange()
    {

        return 0;
    }

    public long getPlanModuleSeed() {
	return 0;
    }
}
