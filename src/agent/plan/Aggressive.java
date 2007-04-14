package agent.plan;

import agent.Agent;
import agent.AgentLocation;
import config.ConfigBobject;

public class Aggressive extends PlanModule
{
	private agent.Agent.state agentState = agent.Agent.state.SEARCH;
	
	public Aggressive(ConfigBobject config)
	{
		super(config);
	}
	
	public agent.Agent.state getAgentState()
	{
		return agentState;
	}

    public AgentLocation getGoalLocation(Agent a )
    {
    	AgentLocation temp = null;
    	return temp;
    }
    
    public void Dead(Agent a)
    {
    	if (a.getIsAlive())
    	{
    		a.setAgentState(agentState);
    		return;
    	}
    	if (a.getHasFlag())
    		a.dropFlag();    		
    }
    public void FlagCarrier(Agent a)
    {
    	
    }
    public void Guard(Agent a)
    {
    	
    }
    public void Attacking(Agent a)
    {
    	
    }
    public void Flee(Agent a)
    {
    	
    }
    public void Hide(Agent a)
    {
    	
    }
    public void Search(Agent a)
    {
    	
    }
    public void RecoverFlag(Agent a)
    {
    	
    }

}
