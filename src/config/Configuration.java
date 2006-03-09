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

    public double getWingSpan()
    {
        return 0;
    }

    // ----------- SCOUT CONFIGURATION ----------- //

    public int getNumberOfScouts()
    {
        return 0;
    }

    public String getScoutDeploymentStrategy()
    {
        return "DeploymentStrategy";
    }

    public String getScoutSensor()
    {

        return null;
    }

    public double getScoutSensorRange()
    {

        return 0;
    }

    public String getScoutPlan()
    {
        return null;
    }

    public String getScoutComm()
    {
        return null;
    }

    public double getScoutCommRange()
    {
        return 0;
    }

    public String getScoutPropulsion()
    {
        return null;
    }

    // ----------- WORKER CONFIGURATION ----------- //

    public int getNumberOfWorkers()
    {
        return 0;
    }

    public String getWorkerDeploymentStrategy()
    {
        return null;
    }

    public String getWorkerSensor()
    {

        return null;
    }

    public double getWorkerSensorRange()
    {

        return 0;
    }

    public String getWorkerPlan()
    {
        return null;
    }

    public String getWorkerComm()
    {
        return null;
    }

    public double getWorkerCommRange()
    {
        return 0;
    }

    public String getWorkerPropulsion()
    {
        return null;
    }

    // ----------- WORLD CONFIGURATION ----------- //

    public int getWorldWidth()
    {
        return 0;
    }

    public int getWorldHeight()
    {
        return 0;
    }

    public long getPlanModuleSeed()
    {
        return 0;
    }

    public double getPropulsionMaxSpeed()
    {
        return 0;
    }

    public double getPropulsionEnergyAmount()
    {
        return 0;
    }
}
