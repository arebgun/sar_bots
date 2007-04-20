package agent.deployment;

import config.ConfigBobject;
import agent.*;

public class BaseDeploy extends DeploymentStrategy {

	private AgentLocation myLocation;
	public BaseDeploy(ConfigBobject config) {
		super(config);
		myLocation = config.objectLocation();
	}
	
	public AgentLocation getNextLocation(Agent a) {
		return myLocation;
	}
}
