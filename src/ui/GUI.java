package ui;

/*
 * Class Name:    ui.GUI
 * Last Modified: 3/13/2007 3:21
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 * @author Steve Diersen
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import agent.Agent;
import env.Environment;
import sim.Simulator;
import baseobject.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

public class GUI
{
    private static int DEFAULT_WIDTH  = 1024;
    private static int DEFAULT_HEIGHT = 768;
    private static boolean showGrid   = false;
    private static boolean showSight = false;
    private static boolean showHearing = false;
    private final Timer tmrSim;
    private final Timer tmrUpdate;

    private static GUI guiInstance;
    private static JFrame main;
    private static JTabbedPane jtViewSwitcher;

    // Tab scroll panes
    private SimScrollPane area;
    private SimScrollPane coverage;

    // Bottom control panels
     private BottomPanel bottom;

    private int delay = 0;

    private GUI()
    {
        // Set window decorations (minimize, maximize, close, etc. buttons)
        JFrame.setDefaultLookAndFeelDecorated( true );

        // Set application icon, if not found system default will be used
        URL iconURL = ClassLoader.getSystemClassLoader().getResource( "images/icons/bot_16.gif" );
        Image icon  = null;
        if ( iconURL != null ) { icon = new ImageIcon( iconURL.getPath() ).getImage();  }

        // Set main window to be the size of the screen resolution
        // for optimal appereance screen resolution should be 1024x768
        // screen will be equal to the current resolution of the computer
        // covering the entire screen.
        int locX = 0;
        int locY = 0;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        DEFAULT_WIDTH = (int) screenSize.getWidth();
        DEFAULT_HEIGHT = (int) screenSize.getHeight();
        
        // Create main window, size and position it on the screen
        main = new JFrame( "Capture the Flag" );
        main.setIconImage( icon );
        main.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        main.setMinimumSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );
        main.setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );
        main.setLocation( locX, locY );
        main.setLayout( new BorderLayout() );

        main.addComponentListener( new ComponentAdapter()
        {
            public void componentResized( ComponentEvent e )
            {
                Dimension newSize = jtViewSwitcher.getSize();
                Environment.scaleDrawing( area, newSize );
                Environment.scaleDrawing( coverage, newSize );
            }
        } );

        tmrSim = new Timer( 100, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Simulator.step();
                delay = tmrSim.getDelay();
            }
        } );

        tmrUpdate = new Timer( 100, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                update();
            }
        } );
    }

    public static GUI getInstance()
    {
        if ( guiInstance == null ) { guiInstance = new GUI(); }
        return guiInstance;
    }

    public Timer getTmrUpdate()
    {
        return tmrUpdate;
    }

    public int getDelay()
    {
        return delay;
    }

    public Dimension getMainWindowSize()
    {
        return main.getSize();
    }

    public Point getMainWindowXyPoint()
    {
        return new Point( main.getX(), main.getY() );
    }

    public static boolean getShowGrid()
    {
        return showGrid;
    }
    
    public static boolean getShowSight()
    {
    	return showSight;
    }
    
    public static boolean getShowHearing()
    {
    	return showHearing;
    }
    
    public static void setShowSight(boolean show)
    {
    	showSight = show;
    }

    public static void setShowGrid( boolean show )
    {
        showGrid = show;
    }
    
    public static void setShowHearing( boolean show )
    {
    	showHearing = show;
    }

    public static Component getSelectedTabView()
    {
        return jtViewSwitcher.getSelectedComponent();
    }

    public void show()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                build();
            }
        } );
    }

    public void update()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                jtViewSwitcher.getSelectedComponent().repaint();
    
                Toolkit.getDefaultToolkit().sync();
            }
        } );
    }

    private void build()
    {
        addComponents();
        main.pack();
        main.setVisible( true );
    }

    private void addComponents()
    {
        // Main simulator window scroll pane and rescue area
        area     = new SimScrollPane( new JPanel(), new RescueArea() );
        coverage = new SimScrollPane( new JPanel(), new SensCoverage() );

        // Create tabs and add simulator views
        jtViewSwitcher = new JTabbedPane( JTabbedPane.BOTTOM );
        jtViewSwitcher.addTab( "Environment", area );
        jtViewSwitcher.addTab( "Sensor Coverage", coverage );
        main.add( jtViewSwitcher, BorderLayout.CENTER );

        // Create and add bottom panel
        bottom = new BottomPanel( tmrSim );
        main.add( bottom, BorderLayout.PAGE_END );
    }

   // @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}

class SimScrollPane extends JScrollPane
{
    private final SimDrawPanel drawArea;

    public SimScrollPane( JPanel ornament, SimDrawPanel drawArea )
    {
        super( ornament );
        ornament.add( drawArea );
        this.drawArea = drawArea;
    }

    public void setSize( Dimension newSize )
    {
        drawArea.setSize( newSize );
    }

    public void setPreferredSize( Dimension newSize )
    {
        drawArea.setPreferredSize( newSize );
    }
}

abstract class SimDrawPanel extends JPanel
{
    private final BasicStroke lineStroke = new BasicStroke( 0.3f );

    public void paint( Graphics g )
    {
        Graphics2D g2 = (Graphics2D) g;
        Environment.scaleGraphics( g2, getSize() );
        simPaint( g2 );
    }

    protected void paintGrid( Graphics2D g2 )
    {
        g2.setColor( Color.GRAY );
        g2.setStroke( lineStroke );
        Iterator<Rectangle2D> iter = Environment.gridIterator();

        while ( iter.hasNext() )
        {
            g2.draw( iter.next() );
        }
    }

    protected abstract void simPaint( Graphics2D g2 );
}

class RescueArea extends SimDrawPanel
{
    private final String soilTextureFilename = "images/textures/grass.jpg";
    private final String roofTextureFilename = "images/textures/rocks.jpg";
    private TexturePaint soilTexture;
    private TexturePaint roofTexture;
   

    public RescueArea()
    {
        soilTexture = createTexture( soilTextureFilename, 1 );
        roofTexture = createTexture( roofTextureFilename, 10 );
    }

    private TexturePaint createTexture( String fileName, double divisor )
    {
        try
        {
            Image texture = new ImageIcon( ClassLoader.getSystemClassLoader().getResource( fileName ).getPath() ).getImage();
            int width     = texture.getWidth( this );
            int height    = texture.getHeight( this );

            BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            bi.createGraphics().drawImage( texture, 0, 0, this );
            return new TexturePaint( bi, Environment.getTextureAnchor( divisor ) );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public void simPaint( Graphics2D g2 )
    {
    	g2.setPaint(soilTexture);
    	g2.fill(Environment.groundShape());
    	
    	if ( GUI.getShowGrid() ) { paintGrid( g2 ); }
    	
    	paintObjects(g2);
    }
   
    private void paintObjects( Graphics2D g2)
    {
    	int delay = GUI.getInstance().getDelay();
    	
    	Iterator<Bobject> iter = Simulator.objectIterator();
    	
    	while ( iter.hasNext())
    	{
    		Bobject b = iter.next();
    		
    		if (b.isAgent())
    		{
    			Agent a = (Agent)b;
    			g2.setColor(a.getColor());
    			g2.fill(new Ellipse2D.Float((float)a.getLocation().getX() - (float)a.getBoundingRadius(),
        				(float)a.getLocation().getY() - (float)a.getBoundingRadius(),
        				2f * (float)a.getBoundingRadius(),
        				2f * (float)a.getBoundingRadius()));
    			if (GUI.getShowSight())
    			{
    				g2.setColor(a.getSightColor());
    				g2.fillArc((int)a.getLocation().getX() - (int)(a.sensorSight.getlength() / 2), 
    						(int)a.getLocation().getY() - (int)(a.sensorSight.getlength() / 2), 
    						(int)a.sensorSight.getlength(), (int)a.sensorSight.getlength(),
    						(int)a.getLocation().getTheta()- (int)(a.sensorSight.getArcAngle() / 2),
    						(int)a.sensorSight.getArcAngle());
    			}
    			
    			if (GUI.getShowHearing())
    			{
    				g2.setColor(a.getHearColor());
        			g2.fill(new Ellipse2D.Float((float)a.getLocation().getX() - (float)a.getSoundRadius(),
            				(float)a.getLocation().getY() - (float)a.getSoundRadius(),
            				2f * (float)a.getSoundRadius(),
            				2f * (float)a.getSoundRadius()));
    			}
    			a.setSleepTime( delay );
    		}
    		if (b.isFlag())
    		{
    			Flag f = (Flag)b;
    			g2.setColor(f.getColor());
    			g2.fill(new Ellipse2D.Float((float)f.getLocation().getX(),
        				(float)f.getLocation().getY(),
        				2f * (float)f.getBoundingRadius(),
        				2f * (float)f.getBoundingRadius()));
    			System.out.println("painting a flag at " + f.getLocation().getX() + "  " + f.getLocation().getY());
    		}
    		if (b.isObstacle())
    		{
    			Obstacle o = (Obstacle)b;
    			g2.setColor(o.getColor());
    			g2.fill(new Ellipse2D.Float((float)o.getLocation().getX(),
        				(float)o.getLocation().getY(),
        				(float)o.getBoundingRadius(),
        				(float)o.getBoundingRadius()));
    		}    	
    	}
    }
}
 

class SensCoverage extends SimDrawPanel
{
    private final static int clrSize     = 1023;
    private final static Color[]clrTable = new Color[clrSize + 1];

    public SensCoverage()
    {
        setBackground( Color.WHITE );

        for ( int i = 0; i < clrTable.length; i++ )
        {
            clrTable[clrSize - i] = new Color( i / (float) clrSize, 1f, i / (float) clrSize );
        }
    }

    public void simPaint( Graphics2D g2 )
    {
        if ( GUI.getShowGrid() ) { paintGrid( g2 ); }
    }
}

/*
 * Class Name:    BottomPanel
 * Last Modified: 3/13/2007
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 * @author Steve Diersen
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */
class BottomPanel extends JPanel
{
	private final JButton btnStartStop = new JButton("Start");
	private final JButton btnReset = new JButton("Reset");
	private final JButton btnScreenshot = new JButton("Screenshot");
	private final JButton btnSave = new JButton("Save");
	
	private final JCheckBox cbShowGrid = new JCheckBox( "Show Grid", false );
	private final JCheckBox cbShowSight = new JCheckBox( "Show Sight", false );
	private final JCheckBox cbShowHearing = new JCheckBox( "Show Hearing", false);
	
	private final Timer tmrSim;

    public BottomPanel( Timer tmr )
    {
        tmrSim = tmr;
        setBorder( BorderFactory.createEmptyBorder( 5, 2, 5, 2 ) );
        JPanel jpCtrl = new JPanel();
        jpCtrl.setLayout( new BoxLayout( jpCtrl, BoxLayout.X_AXIS ) );

        cbShowGrid.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                GUI.setShowGrid( cbShowGrid.isSelected() );
                GUI.getSelectedTabView().repaint();
            }
        } );
        add( cbShowGrid );
          
        cbShowSight.addActionListener(new ActionListener()
        {
        	public void actionPerformed( ActionEvent e)
        	{
        		GUI.setShowSight( cbShowSight.isSelected() );
        		GUI.getSelectedTabView().repaint();
        	}
        } );

        add( cbShowSight);
        
        cbShowHearing.addActionListener(new ActionListener()
        {
        	public void actionPerformed( ActionEvent e)
        	{
        		GUI.setShowHearing( cbShowHearing.isSelected() );
        		GUI.getSelectedTabView().repaint();
        	}
        } );

        add( cbShowHearing);
        
        addConfiguredButton( jpCtrl, btnStartStop, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( tmrSim.isRunning() )
                {
                    GUI.getInstance().getTmrUpdate().stop();
                    tmrSim.stop();
                    Iterator<Bobject> it = Simulator.objectIterator();
                    while(it.hasNext())
                    {
                    	Bobject b = it.next();
                    	if (b.isAgent())
                    	{
                    		Agent a = (Agent)b;
                    		a.stop();
                    	}                    
                    }

                    btnStartStop.setText( "Start" );
                }
                else
                {
                    btnStartStop.setText( "Stop" );
                    tmrSim.start();
                    GUI.getInstance().getTmrUpdate().start();
                    Iterator<Bobject> it = Simulator.objectIterator();
                    while(it.hasNext())
                    {
                    	Bobject b = it.next();
                    	if (b.isAgent())
                    	{
                    		Agent a = (Agent)b;
                    		a.start(false);
                    	}
                    }
                }
            }
        } );

        jpCtrl.add( Box.createRigidArea( new Dimension( 0, 15 ) ) );

        addConfiguredButton( jpCtrl, btnReset, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Simulator.reset();
                GUI.getInstance().update();
            }
        } );

        jpCtrl.add( Box.createRigidArea( new Dimension( 0, 15 ) ) );

        addConfiguredButton( jpCtrl, btnScreenshot, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                SimpleDateFormat sdf = new SimpleDateFormat( "MMddyyHHmmss" );
                String currentDate   = sdf.format( Calendar.getInstance( TimeZone.getDefault() ).getTime() );
                String outFileName   = "images/screenshots/screen_" + currentDate + ".png";

                Dimension position   = GUI.getInstance().getMainWindowSize();
                Point xyCoord        = GUI.getInstance().getMainWindowXyPoint();
                Rectangle mainWindow = new Rectangle( xyCoord, position );

                try
                {
                    // create screen shot
                    Robot robot         = new Robot();
                    BufferedImage image = robot.createScreenCapture( mainWindow );

                    // save captured image to PNG file
                    ImageIO.write( image, "png", new File( outFileName ) );
                }
                catch ( Exception error )
                {
                    throw new RuntimeException( error );
                }
            }
        } );

        addConfiguredButton( jpCtrl, btnSave, null );
        add( jpCtrl, BorderLayout.PAGE_END );
    }

    public void paint( Graphics g )
    {
        super.paint( g );
    }

    private void addConfiguredButton( JPanel panel, JButton button, ActionListener action )
    {
        Component buttonGlue = Box.createRigidArea( new Dimension( 0, 5 ) );
        Dimension buttonSize = new Dimension( 125, 25 );

        button.addActionListener( action );
        button.setPreferredSize( buttonSize );
        button.setMaximumSize( buttonSize );
        panel.add( buttonGlue );
        panel.add( button );
    }
}