package agent.plan;

import java.util.Random;
import agent.Agent;
import baseobject.Flag;
import agent.AgentLocation;
import config.ConfigBobject;
import messageBoard.MessageBoard;
import sim.Simulator;
import java.util.Iterator;
import statistics.Statistics;

public class Defensive extends PlanModule
{
	private static Random rand = null;
    private Agent.state agentState = Agent.state.PATROL;
    private double patrolDistance = 0;
    public Defensive(ConfigBobject config)
	{
		super(config);
		agentState = Agent.state.PATROL;
		initialState = agentState;
		patrolDistance = config.getPatrolDistance();
		if ( rand == null )
        {
            rand = new Random( objectConfig.getPlanSeed() );
        }
	}
	
	public Agent.state getAgentState()
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
    	AgentLocation holder = null;
    	
    	if(a.getHasFlag())
    		a.dropFlag();
    	a.setIsAlive(false);
    	
    	Statistics.incStateDead(a.getObjectID());
    	int blah = Statistics.getDamageDone(a.getObjectID());
    	System.out.println("Agent : " + a.getObjectID() + " did : " + blah + " damage before he died");
    	a.sendMessage(false, false, holder, false, holder);
    	agentState = Agent.state.HIDE;
    	a.setAgentState(agentState);
    	   		
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
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = null;
    	boolean iSeeOurFlag = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	boolean ourFlagHome = mb.getFlagAtHome();
    	boolean ourFlagSeen = mb.getOurFlagSeen();
    	
    	if (!ourFlagHome)
    	{
    		if(ourFlagSeen || iSeeOurFlag)
    		{
    			agentState = Agent.state.RECOVER_FLAG;
    			a.setAgentState(agentState);
        		a.update();
    			return;
    		}
    		if(!a.getIsBeingShot())
    		{
    			agentState = Agent.state.SEARCH;
    			a.setAgentState(agentState);
    			a.update();
    			return;
    		}
    	}
    	
    	if(!a.getIsBeingShot())
    	{
    		agentState = Agent.state.PATROL;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	
    	double newHeading = 0;
		int numEnemies = 0;
		Iterator <Agent> enemy = a.getAgentsHeard();
    	while (enemy.hasNext())
    	{
    		Agent b = (Agent)enemy.next();
    		if (a.getTeamID() != b.getTeamID())
    		{
    			numEnemies++;
    			newHeading += a.arctangentToGoal(b.getLocation());
    		}
    	}
    	newHeading = newHeading/numEnemies;
    	
    	moveAgent(a,heading,newHeading);
    	
		a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	ourFlagSeen = ourFlagSeen(a);
    	if (ourFlagSeen)
    		ourFlagLoc = ourFlagLoc(a);
    	Statistics.incStateFlee(a.getObjectID());
		a.sendMessage(false, oppFlagSeen, oppFlagLoc, ourFlagSeen, ourFlagLoc);
   }
    
    public void Hide(Agent a)
    {
    	
    }
    public void Search(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	double newHeading = heading;
    	int moveChance = 100;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = null;
    	boolean iSeeOurFlag = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	
    	if (mb.getFlagAtHome())
    	{
    		agentState = Agent.state.PATROL;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	
    	if (mb.getOurFlagSeen() || iSeeOurFlag)
    	{
    		agentState = Agent.state.RECOVER_FLAG;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	
    	if (a.getIsBeingShot() && (a.getHealth() < a.getThreshold()))
    	{
    		agentState = Agent.state.FLEE;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	
    	moveChance = 50;
    	newHeading = heading;
		if ((rand.nextDouble()*100) > 75 )
			newHeading = rand.nextDouble()*360;
		
		if (opponentsSeen(a) > 0)
		{
			int chance = (int)(rand.nextDouble() * 100);
			if (chance > moveChance)
				a.shootAll();
			else
				moveAgent(a,heading,newHeading);
		}
		else
			moveAgent(a,heading,newHeading);
    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	iSeeOurFlag = ourFlagSeen(a);
    	if (iSeeOurFlag)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	Statistics.incStateSearch(a.getObjectID());
    	//now send all your info to the messageboard
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, iSeeOurFlag, ourFlagLoc);
    }
    public void RecoverFlag(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	double newHeading = heading;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = null;
    	boolean iSeeOurFlag = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	
    	if (mb.getFlagAtHome())
    	{
    		agentState = Agent.state.PATROL;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	if (!mb.getOurFlagSeen() && !iSeeOurFlag)
    	{
    		agentState = Agent.state.SEARCH;
    		a.setAgentState(agentState);
    		a.update();
    		return;
    	}
    	if (iSeeOurFlag)
    	{
    		if (mb.getOurFlagSeen())
	    		a.shootAll();
    		
			Flag ourFlag = getFlagSeen(a);
			if (ourFlag == null)
			{
				System.out.println("flag was moved before i could get it's location");
				return;
			}
			ourFlagLoc = ourFlag.getLocation();
			double dist = Math.hypot(a.getLocation().getX() - ourFlagLoc.getX(),
					 a.getLocation().getY() - ourFlagLoc.getY());
			newHeading = a.arctangentToGoal(ourFlagLoc);
			if (dist <= a.sensorSight.getlength()/2 && mb.getOurFlagOwned())
				a.turnMove(newHeading);
			else
				moveAgent(a,heading,newHeading);    
			
			double bound = a.getBoundingRadius() + ourFlag.getBoundingRadius();
			if (bound >= dist)
				a.pickUpFlag(ourFlag);
    		
    	}
    	else
    	{
    		ourFlagLoc = mb.getOurFlagLocation();
			newHeading = a.arctangentToGoal(ourFlagLoc);
			moveAgent(a,heading,newHeading); 
    	}
    	
    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	iSeeOurFlag = ourFlagSeen(a);
    	if (iSeeOurFlag)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	Statistics.incStateRecoverFlag(a.getObjectID());
    	//now send all your info to the messageboard
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, iSeeOurFlag, ourFlagLoc);
    		
    }
    
    public void Patrol(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	double heading = a.getLocation().getTheta();
    	double newHeading = heading;
    	boolean oppFlagSeen = opponentsFlagSeen(a);
    	AgentLocation oppFlagLoc = null;
    	boolean iSeeOurFlag = ourFlagSeen(a);
    	AgentLocation ourFlagLoc = null;
    	a.checkSensors();
    	
    	if (!mb.getFlagAtHome())
    	{
    		if (!mb.getOurFlagSeen() && !iSeeOurFlag)
    		{
    			agentState = Agent.state.SEARCH;
    			a.setAgentState(agentState);
    			a.update();
    			return;
    		}
    		else
    		{
    			agentState = Agent.state.RECOVER_FLAG;
    			a.setAgentState(agentState);
    			a.update();
    			return;
    		}
    	}
    	double dist = Math.hypot(a.getLocation().getX() - patrolLocation.getX(),
    							 a.getLocation().getY() - patrolLocation.getY());
    	double bound = a.getBoundingRadius() + patrolDistance;
    	if (bound < dist)
    		newHeading = a.arctangentToGoal(patrolLocation);
    	else
    	{
    		newHeading = heading;
    		if ((rand.nextDouble()*100) > 75 )
    			newHeading = rand.nextDouble()*360;
    	}
    	if (opponentsSeen(a) > 0)
			a.shootAll();
		else
			moveAgent(a,heading,newHeading);
    	
    	//we have now moved. check sensors again so we can send current info to the mb
    	a.checkSensors();
    	oppFlagSeen = opponentsFlagSeen(a);
    	if (oppFlagSeen)
    		oppFlagLoc = opponentsFlagLocation(a);
    	iSeeOurFlag = ourFlagSeen(a);
    	if (iSeeOurFlag)
    		ourFlagLoc = ourFlagLoc(a);
    	
    	Statistics.incStateRecoverFlag(a.getObjectID());
    	//now send all your info to the messageboard
    	a.sendMessage(false, oppFlagSeen, oppFlagLoc, iSeeOurFlag, ourFlagLoc);
    	
	}
    
    public void CleanUp(Agent a)
    {
    	/*There are two conditions, if the agent is dead or alive*/
		if(!a.getIsAlive())
		{
			a.reset();
			a.setFadeIn(true);
			agentState = Agent.state.FADE;
			a.setAgentState(agentState);
		}
		else
		{
			if(!a.closeToGoal(a.getMoveRadius(), a.getInitialLocation()))
			{
				double initHeading = a.arctangentToGoal(a.getInitialLocation());
				moveAgent(a, a.getLocation().getTheta(), initHeading);
			}
			else
			{
				a.setLocation(a.getInitialLocation());
				agentState = Agent.state.WAIT;
				a.setAgentState(agentState);
			}
		}
    }
    public void Fade(Agent a)
    {
    	if(!a.getFadeIn())
    	{
    		agentState = Agent.state.WAIT;
    		a.setAgentState(agentState);
    	}
    }
    public void Wait(Agent a)
    {
    	MessageBoard mb = Simulator.teamBoards.get(a.getTeamID());
    	mb.setIAmFaded(a.getMsgID());
    	agentState = Agent.state.HIDE;
    	a.setAgentState(agentState);
    	if (mb.getAllFadeIn())
    		Simulator.addFaded(a.getTeamID());
    	
    }
}
