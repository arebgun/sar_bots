package config;

/**
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 */

public class ConfigAgent
{
    public ConfigAgent( String configFilePath )
    {

    }

    public String getAgentClassName()
    {
        return "agent.Scout";
    }

    public int getNumberOfAgents()
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

    public double getWingSpan()
    {
        return 0;
    }

    public String getAgentDeploymentStrategy()
    {
        return "DeploymentStrategy";
    }

    public String getAgentSensor()
    {

        return null;
    }

    public double getAgentSensorRange()
    {

        return 0;
    }

    public String getAgentPlan()
    {
        return null;
    }

    public String getAgentComm()
    {
        return null;
    }

    public double getAgentCommRange()
    {
        return 0;
    }

    public String getAgentPropulsion()
    {
        return null;
    }


}