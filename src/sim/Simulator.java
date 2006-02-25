package sim;

/**
 * @(#) Simulator.java
 */

import agent.Agent;
import agent.Scout;
import config.Configuration;
import env.Environment;
import ui.GUI;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Simulator
{
    private static final String CONFIG_FILE_PATH = ClassLoader.getSystemClassLoader().getResource( "conf/base.conf" ).getPath();

    private static final int AGENT_NUMBER = 10;

    private static Simulator simulatorInstance;

    private long time;

    private ArrayList<Agent> agents;

    private Environment env;

    public GUI gui;

    public BlackBoard blackBoard;

    public Configuration config;

    public Logger log;

    private Simulator() {}

    public static Simulator getSimulator()
    {
        if ( simulatorInstance == null )
        {
            simulatorInstance = new Simulator();
        }

        return simulatorInstance;
    }

    public void initialize()
    {
        time = 0;

        gui = GUI.getInstance();
        env = Environment.getInstance();
        blackBoard = BlackBoard.getInstance();
        config = new Configuration( CONFIG_FILE_PATH );
        log = Logger.getLogger( "simulator" );
        DeploymentStrategy strategy = null;

        try
        {
            strategy = (DeploymentStrategy) Class.forName( config.getAgentDeploymentStrategy() ).newInstance();
        }
        catch ( Exception e )
        {
            //To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }

        Agent.setProperties( this, strategy );
        agents = new ArrayList<Agent>( AGENT_NUMBER );

        for ( int i = 0; i < AGENT_NUMBER; i++ )
        {
            agents.add( new Scout() );
        }

    }

    public long getTime()
    {
        return time;
    }

    public void step()
    {
        for ( Agent agent : agents )
        {
            agent.move();
        }

        env.update();
        gui.update();

        time++;
    }

    @SuppressWarnings( { "CloneDoesntCallSuperClone" } )
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}
