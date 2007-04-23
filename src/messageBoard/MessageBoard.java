package messageBoard;

import agent.AgentLocation;
import agent.Agent;
import java.util.ArrayList;
import java.util.Iterator;

public class MessageBoard {
	/* private members*/
	
	/*Single Variables*/
	private AgentLocation opponentFlagLocation;
	private boolean opponentFlagSeen;
	private int whoOwnsFlag;
	private boolean flagAtHome;
	private AgentLocation ourFlagLocation;
	private AgentLocation ourBaseLocation;
	private int teamSize;
	private boolean ourFlagOwned;
	
	/*Multi-Variable*/
	private ArrayList<Integer> myId;
	private ArrayList<AgentLocation> myLocation;
	private ArrayList<Boolean> needHelp;
	private ArrayList<Boolean> isAlive;
	private ArrayList<Integer> currentHitPoints;
	private ArrayList<Agent.state> currentState;
	private ArrayList<Agent> agentsSeen;
	private int seenCounters[];
	private ArrayList<Agent> agentsHeard;
	private int heardCounters[];
	private ArrayList<Boolean> ourTeamFlagSeen;
	private ArrayList<Boolean> teamFadeIn;
	
	public MessageBoard()
	{
		/*Single Variables*/
		opponentFlagLocation = null;
		opponentFlagSeen = false;
		whoOwnsFlag = -1;
		flagAtHome = false;
		ourFlagLocation = null;
		ourBaseLocation = null;
		ourFlagOwned = false;
		
		/*Multi-Variable*/
		myId = new ArrayList<Integer>();
		myLocation = new ArrayList<AgentLocation>();
		needHelp = new ArrayList<Boolean>();
		isAlive = new ArrayList<Boolean>();
		currentHitPoints = new ArrayList<Integer>();
		currentState = new ArrayList<Agent.state>();
		agentsSeen = new ArrayList<Agent>();
		agentsHeard = new ArrayList<Agent>();
		seenCounters = new int[100];
		heardCounters = new int[100];
		ourTeamFlagSeen = new ArrayList<Boolean>();
		teamFadeIn = new ArrayList<Boolean>();
	}
	
	/*Initialization Function*/
	public void initialize(int numOnTeam)
	{
		teamSize = numOnTeam;
		for(int i = 0; i < numOnTeam; i++)
		{
			myId.add(-1);
			myLocation.add(null);
			needHelp.add(false);
			isAlive.add(false);
			currentHitPoints.add(0);
			currentState.add(null);
			agentsSeen.add(null);
			agentsHeard.add(null);
			ourTeamFlagSeen.add(false);
			teamFadeIn.add(false);
		}
		for(int i = 0; i < 100; i++)
		{
			seenCounters[i] = 0;
			heardCounters[i] = 0;
		}
	}
	
	public void reset()
	{
		
		/*Single Variables*/
		opponentFlagLocation = null;
		opponentFlagSeen = false;
		whoOwnsFlag = 0;
		flagAtHome = false;
		ourFlagLocation = null;
		
		/*Multi-Variable*/
		myId.clear();
		myLocation.clear();
		needHelp.clear();
		isAlive.clear();
		currentHitPoints.clear();
		currentState.clear();
		agentsSeen.clear();
		agentsHeard.clear();
		ourTeamFlagSeen.clear();
		teamFadeIn.clear();
		
		initialize(teamSize);
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
		Iterator<Boolean> b = ourTeamFlagSeen.iterator();
		while(b.hasNext())
		{
			boolean flagSeen = (boolean)b.next();
			if (flagSeen)
				return true;
		}
		return false;
	}
	
	public boolean getOurFlagOwned()
	{
		return ourFlagOwned;
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
	
	public Iterator<Agent> getAgentsSeen()
	{
		return agentsSeen.iterator();
	}

	public Iterator<Agent> getAgentsHeard()
	{
		return agentsHeard.iterator();
	}
	
	public AgentLocation getBaseLocation()
	{
		return ourBaseLocation;
	}
	
	public boolean getAllFadeIn()
	{
		Iterator<Boolean> b = teamFadeIn.iterator();
		while (b.hasNext())
		{
			if (!(b.next()))
				return false;
		}
		return true;
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
	
	public void setOurFlagSeen(int id, boolean temp)
	{
		ourTeamFlagSeen.set(id, temp);
	}
	
	public void setOurFlagOwned(boolean temp)
	{
		ourFlagOwned = temp;
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
	
	public void setBaseLocation(AgentLocation bl)
	{
		ourBaseLocation = bl;
	}
	
	public void setAgentsSeen(ArrayList<Agent> temp)
	{
		for(int i = 0; i < temp.size(); i++)
		{
			boolean found = false;
			for(int j = 0; j < agentsSeen.size() && !found; j++)
			{
				if(agentsSeen.get(j) == temp.get(i)) 
					found = true;
			}
			if(!found) 
				agentsSeen.add(temp.get(i));
		}
	}
	
	public void setAgentsHeard(ArrayList<Agent> temp)
	{	
		for(int i = 0; i < temp.size(); i++)
		{
			boolean found = false;
			for(int j = 0; j < agentsHeard.size() && !found; j++)
			{
				if(agentsHeard.get(j) == temp.get(i)) 
				{
					found = true;
					heardCounters[temp.get(i).getObjectID()] += 1;
				}
			}
			if(!found) 
			{
				agentsHeard.add(temp.get(i));
				heardCounters[temp.get(i).getObjectID()] = 15;
			}
		}
//		first decrement all counters for heard agents
		for(int d = 0; d < 100; d++)
		{
			if (heardCounters[d] > 0)
			{
				heardCounters[d]--;
				if (heardCounters[d] == 0)
				{
					boolean found = false;
					for(int i = 0; i < agentsHeard.size() && !found;i++)
					{
						if (agentsHeard.get(i) != null && agentsHeard.get(i).getObjectID() == d)
						{
							agentsHeard.remove(i);
							found = true;
						}
					}
				}
			}
		}
	}
	public void setIAmFaded(int id)
	{
		teamFadeIn.set(id, true);
	}
}