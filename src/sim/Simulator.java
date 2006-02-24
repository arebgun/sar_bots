/**
 * @(#) Simulator.java
 */

package sim;

import agent.Agent;
import java.util.logging;

public class Simulator 
{
    private static final String CONFIG_FILE_PATH = ClassLoader.getSystemClassLoader().getResource("conf/base.conf").getPath();

    private long time;

        private static Simulator simulatorInstance;
	
	private GUI gui;
	
	private ArrayList<Agent> agents;
		
	private Environment env;
	
	public BlackBoard blackBoard;
	
	public Configuration config;
	
        public Logger log;


	protected Simulator( )
	{
		
	}
	
	public static Simulator getSimulator( )
	{
		return simulatorInstance;
	}
	
	public void initialize( )
	{
	    time = 0;

	    gui        = new GUI( simulatorInstance );
	    env        = new Environment( simulatorInstance );
	    blackBoard = new BlackBoard( );
	    config     = new Configuration( CONFIG_FILE_PATH );
	    log        = Logger.getLogger("simulator");

	    DeploymentStrategy strategy = Class.forName( config.getAgentDeploymentStrategy() ).newInstance();
	    Agent.setProperties( simulatorInstance, strategy );
	    agents = new ArrayList<Agent>( 10 );
	    for (int i = 0; i < agents.length; i++) 
		{
		agents.add(new Agent( ) );
		}

	}
	
    public long getTime( ) 
    {
	return time;
    }

    public void step ( ) 
    {
	for (Agent agent : agents) 
	    {
	    agent.move( );
	}

	env.update( );

	gui.update( );
	
	t++;
    }

	public static void main( String[] arg )
	{
	    simulatorInstance = this;
	    initialize();

	    gui.show( );
	}	
}
