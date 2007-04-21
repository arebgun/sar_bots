package agent.plan;

import agent.Agent;
import agent.AgentLocation;
import config.ConfigBobject;

public class APPlanner extends PlanModule
{

	private agent.Agent.state agentState = agent.Agent.state.SEARCH;
	
	
	public APPlanner( ConfigBobject config )
    {
        super( config );
    }
	public agent.Agent.state getAgentState()
	{
		return agentState;
	}
	public AgentLocation getGoalLocation( Agent a )
    {
		
		 double newX     = -1;
	     double newY     = -1;
	     double newTheta = a.getLocation().getTheta();
	     
	     return new AgentLocation( newX, newY, newTheta );
    }
	public void Dead(Agent a)
	{
		
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
    public void Patrol(Agent a)
    {
    	
    } 
}
