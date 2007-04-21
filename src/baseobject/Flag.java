package baseobject;
import sim.Simulator;
import messageBoard.MessageBoard;
import config.ConfigBobject;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Flag extends Bobject implements Runnable{
	protected String idString;
	protected int owner;
	protected boolean isOwned;
	protected int teamID; //used to associate the flag with a specific team
	protected boolean atHome;
    private Thread flagThread;
    private int sleepTime;
    private boolean oneStep;
    //private MessageBoard board;
    
	//Consturctors for Flag    
	public Flag(ConfigBobject conf)
	{
		idString = "Agent unit id = " + objectID;
		this.config = conf;
		initialLocation = config.objectLocation();
		location = config.objectLocation();
		teamID = config.getTeamID();
		color = config.getObjectColor();
		boundingRadius = config.getBoundingRadius();
		soundRadius = boundingRadius;
		owner = 0;
		isOwned = false;
		type = types.FLAG;
		boundingShape = Bobject.shapes.CIRCLE;
		updateBoard(Simulator.teamBoards.get(teamID));
	}
	
	private void updateBoard(MessageBoard board)
	{
		board.setFlagAtHome(getAtHome());
		board.setWhoOwnsFlag(owner);
		board.setOurFlagOwned(isOwned);
	}
	public boolean getAtHome()
	{
		return (location == initialLocation);
	}
	public boolean getOwned()
	{
		return isOwned;
	}
	
	public void setOwned(boolean newOwned)
	{
		isOwned = newOwned;
	}
	
	public void toggleOwned()
	{
		isOwned = !isOwned;
	}
	
	public int getOwner()
	{
		return owner;
	}
	
	public void setOwner(int newOwner)
	{
		owner = newOwner;
	}
	
	public int getTeamID()
	{
		return teamID;
	}
	
	public int getSoundRadius()
	{
		return soundRadius;
	}
	
	public void reset()
	{
		location = initialLocation;
		isOwned = false;
		owner = 0;
		updateBoard(Simulator.teamBoards.get(teamID));	
	}
	
	public void setLocation()
	{
		if(this.isOwned)
			this.location = Simulator.getObjectByID(this.getOwner()).location;
	}
	
	public void flagDropped()
	{
		owner = -1;
		isOwned = false;
		System.out.println("Dropped the Flag <Flag> " + objectID);
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(color);
		g2.fill(new Ellipse2D.Float((float)location.getX() - boundingRadius,
				(float)location.getY() - boundingRadius,
				2f * (float)boundingRadius,
				2f * (float)boundingRadius));
	}
	
	public void update()
	{
		updateBoard(Simulator.teamBoards.get(teamID));	
	}
	
  public void start( boolean oneStep )
    {
        if ( flagThread == null ) { flagThread = new Thread( this, idString ); }
        this.oneStep = oneStep;
        flagThread.start();
    }

    /**
     * Stops current agent.
     */
    public void stop()
    {
        flagThread = null;
    }

    /**
     * Moves the current agent. If oneStep is true moves only one step,
     * otherwise agent moves until Stop button is pressed.
     */
    public void run()
    {
        if( oneStep && flagThread != null )
        {
            update();
        	stop();
        }
        else
        {
            while( flagThread != null )
            {
            	update();

                try
                {
                    Thread.sleep( sleepTime );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
