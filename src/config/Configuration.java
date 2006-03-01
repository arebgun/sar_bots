/**
 * @(#) Configuration.java
 */

package config;

public class Configuration
{
    public Configuration( String configFilePath )
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    public String getAgentDeploymentStrategy()
    {
        //To change body of created methods use File | Settings | File Templates.
        return "DeploymentStrategy";
    }

    public int numberOfScouts() {
	return 0;
    }

    public int numberOfWorkers() {
	return 0;
    }

}
