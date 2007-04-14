package messageBoard;

import agent.AgentLocation;
import agent.Agent;
import java.util.ArrayList;
import java.util.Iterator;
import sim.Simulator;

public class MessageBoard {
	/* private members*/
	
	/*Single Variables*/
	private static AgentLocation opponentFlagLocation;
	private static boolean opponentFlagSeen;
	private static int whoOwnsFlag;
	private static boolean flagAtHome;
	private static AgentLocation ourFlagLocation;
	private static boolean ourFlagSeen;
	
	/*Multi-Variable*/
	private static ArrayList<Integer> myId;
	private static ArrayList<AgentLocation> myLocation;
	private static ArrayList<Boolean> needHelp;
	private static ArrayList<Boolean> isAlive;
	private static ArrayList<Integer> currentHitPoints;
	private static ArrayList<Agent.state> currentState;
	private static ArrayList< ArrayList<Agent> > agentsSeen;
	private static ArrayList< ArrayList<Agent> > agentsHeard;
	
	public MessageBoard()
	{
		/*Single Variables*/
		opponentFlagLocation = null;
		opponentFlagSeen = false;
		whoOwnsFlag = -1;
		flagAtHome = false;
		ourFlagLocation = null;
		ourFlagSeen = false;
		
		/*Multi-Variable*/
		myId = null;
		myLocation = null;
		needHelp = null;
		isAlive = null;
		currentHitPoints = null;
		currentState = null;
		agentsSeen = null;
		agentsHeard = null;
	}
	
	/*Initialization Function*/
	public void initialize(int teamID)
	{
		for(int i = 0; i < Simulator.numberOnTeam.get(teamID); i++)
		{
			myId.add(i, -1);
			myLocation.add(i, null);
			needHelp.add(i, false);
			isAlive.add(i, false);
			currentHitPoints.add(i, 0);
			currentState.add(i, null);
			agentsSeen.add(i, null);
			agentsHeard.add(i, null);
		}
	}
	
	/*Accessors*/
	public AgentLocation getOpponentFlagLocation()
	{
		return opponentFlagLocation;
	}
	
	public boolean getOpponentFlagSeen()
	{
		return opponentFlagSeen;
	}
	
	public int getWhoOwnsFlag()
	{
		return whoOwnsFlag;
	}
	
	public boolean getFlagAtHome()
	{
		return flagAtHome;
	}
	
	public AgentLocation getOurFlagLocation()
	{
		return ourFlagLocation;
	}
	
	public boolean getOurFlagSeen()
	{
		return ourFlagSeen;
	}
	
	public Iterator<Integer> getMyId()
	{
		return myId.iterator();
	}
	
	public Iterator<AgentLocation> getMyLocation()
	{
		return myLocation.iterator();
	}
	
	public Iterator<Boolean> getNeedHelp()
	{
		return needHelp.iterator();
	}
	
	public Iterator<Boolean> getIsAlive()
	{
		return isAlive.iterator();
	}
	
	public Iterator<Integer> getCurrentHitPoints()
	{
		return currentHitPoints.iterator();
	}
	
	public Iterator<Agent.state> getCurrentState()
	{
		return currentState.iterator();
	}
	
	public Iterator<ArrayList<Agent>> getAgentsSeen()
	{
		return agentsSeen.iterator();
	}

	public Iterator<ArrayList<Agent>> getAgentsHeard()
	{
		return agentsHeard.iterator();
	}
	
	/*Mutators*/
	public void setOpponentFlagLocation(AgentLocation temp)
	{
		opponentFlagLocation = temp;
	}
	
	public void setOpponentFlagSeen(boolean temp)
	{
		opponentFlagSeen = temp;
	}
	
	public void setWhoOwnsFlag(int temp)
	{
		whoOwnsFlag = temp;
	}
	
	public void setFlagAtHome(boolean temp)
	{
		flagAtHome = temp;
	}
	
	public void setOurFlagLocation(AgentLocation temp)
	{
		ourFlagLocation = temp;
	}
	
	public void setOurFlagSeen(boolean temp)
	{
		ourFlagSeen = temp;
	}
	
	public void setMyId(int index, int temp)
	{
		myId.set(index, temp);
	}

	public void setMyLocation(int index, AgentLocation temp)
	{
		myLocation.set(index, temp);
	}

	public void setNeedHelp(int index, boolean temp)
	{
		needHelp.set(index, temp);
	}

	public void setIsAlive(int index, boolean temp)
	{
		isAlive.set(index, temp);
	}

	public void setCurrentHitPoints(int index, int temp)
	{
		currentHitPoints.set(index, temp);
	}	
	
	public void setCurrentState(int index, Agent.state temp)
	{
		currentState.set(index, temp);
	}
	
	public void setAgentsSeen(int index, ArrayList<Agent> temp)
	{
		agentsSeen.set(index, temp);
	}
	
	public void setAgentsHeard(int index, ArrayList<Agent> temp)
	{
		agentsHeard.set(index, temp);
	}
}
