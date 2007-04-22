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
import java.awt.geom.Ellipse2D;
import java.awt.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;

import obstacle.Obstacle;
import statistics.*;
import javax.swing.border.*;

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
               // Environment.scaleDrawing( coverage, newSize );
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

        tmrUpdate = new Timer( 33, new ActionListener()
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
       // coverage = new SimScrollPane( new JPanel(), new SensCoverage() );
        StatPanel viewStats = new StatPanel();
  
        // Create tabs and add simulator views
        jtViewSwitcher = new JTabbedPane( JTabbedPane.BOTTOM );
        jtViewSwitcher.addTab( "Environment", area );
        jtViewSwitcher.addTab( "Game Stats", viewStats );
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

class GraphicsComponent extends JComponent
{
	void paint(Graphics2D g2)
	{
		paintComponent(g2);
	}
}

class StatPanel extends JPanel
{
		//private final static GraphicsComponent stat1Display = new GraphicsComponent();
		//private final static GraphicsComponent stat2Display = new GraphicsComponent();
	 	private static String[] drop1Strings = { "Team1", "Team2", "Seeker1", "Seeker2", "Defender1", "Defender2" };
	    private static String[] drop2Strings = { "Team1", "Team2", "Seeker1", "Seeker2", "Defender1", "Defender2" };

	   // private static JPanel dropDownP1 = new JPanel();
	    //private static JPanel dropDownP2 = new JPanel();

	    
	    private final static JComboBox dropDown1 = new JComboBox(drop1Strings);
	    private final static JComboBox dropDown2 = new JComboBox(drop2Strings);
	    
	    private static PieChart[] slices1 = new PieChart[7];
    	private static PieChart[] slices2 = new PieChart[7];
	
    	private static int[] sliceSizes1 = new int[7];
    	private static int[] sliceSizes2 = new int[7];
    	private static int[] test1 = {10, 10, 10, 10, 10, 10, 40};
    	private static int[] test2 = {10, 30, 5, 10, 10, 10, 15};
    	private static int[] test3 = {5, 10, 10, 5, 10, 40, 20};
    	private static int[] test4 = {5, 5, 20, 10, 10, 45, 5};
    	
	    StatPanel()
		{
	    	
	    	
	    	slices1[0] = new PieChart(5, Color.blue);
	        slices1[1] = new PieChart(10, Color.green);
	        slices1[2] = new PieChart(15, Color.yellow);
	        slices1[3] = new PieChart(20, Color.orange);
	        slices1[4] = new PieChart(25, Color.red);
	        slices1[5] = new PieChart(10, Color.DARK_GRAY);
	        slices1[6] = new PieChart(15, Color.black);
	        
	        
	        slices2[0] = new PieChart(5, Color.blue);
	        slices2[1] = new PieChart(10, Color.green);
	        slices2[2] = new PieChart(15, Color.yellow);
	        slices2[3] = new PieChart(20, Color.orange);
	        slices2[4] = new PieChart(25, Color.red);
	        slices2[5] = new PieChart(15, Color.DARK_GRAY);
	        slices2[6] = new PieChart(10, Color.black);
	        
	    	JPanel dropDownPanel = new JPanel();
	    	dropDownPanel.setLayout(new GridLayout(1,2,2,2));
	    	
	    	dropDownPanel.add(dropDown1);
	    	dropDownPanel.add(dropDown2);
	    	add(dropDownPanel, BorderLayout.NORTH);
	    	//add(stat1Display, BorderLayout.WEST);
	    	
	    	
	    	dropDown1.addItemListener(new ItemListener() {
	    		public void itemStateChanged(ItemEvent e){
	    			setGraph1(dropDown1.getSelectedIndex());
	    			}
	    		});
	    	
	    	dropDown2.addItemListener(new ItemListener() {
	    		public void itemStateChanged(ItemEvent e){
	    			setGraph2(dropDown2.getSelectedIndex());
	    			}
	    		});
			
	    	
		}
	    
	    public void setGraph1(int index)
    	{
    		if(index == 0)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes1[i] = test1[i];
    		}
    		if(index == 1)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes1[i] = test2[i];
    		}
    		if(index == 2)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes1[i] = test3[i];
    		}
    		if(index == 3)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes1[i] = test4[i];
    		}
    	}
	    
	    public void setGraph2(int index)
    	{
	    	if(index == 0)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes2[i] = test1[i];
    		}
    		if(index == 1)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes2[i] = test2[i];
    		}
    		if(index == 2)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes2[i] = test3[i];
    		}
    		if(index == 3)
    		{
    			for (int i = 0; i < 7; i++)
    				sliceSizes2[i] = test4[i];
    		}
    	}
	    
	    public void paintComponent( Graphics g2 )
	    {
	    	super.paintComponent(g2);
	        paintStat1( g2 );
	        paintStat2( g2 );
	    }
	    
	    public void paintStat1( Graphics g2 )
	    {
	    	slices1[0] = new PieChart(sliceSizes1[0], Color.blue);
	        slices1[1] = new PieChart(sliceSizes1[1], Color.green);
	        slices1[2] = new PieChart(sliceSizes1[2], Color.yellow);
	        slices1[3] = new PieChart(sliceSizes1[3], Color.orange);
	        slices1[4] = new PieChart(sliceSizes1[4], Color.red);
	        slices1[5] = new PieChart(sliceSizes1[5], Color.DARK_GRAY);
	        slices1[6] = new PieChart(sliceSizes1[6], Color.black);
	        
	        
	        Rectangle r = new Rectangle();
	        r.setBounds(100, 150, 400, 400);
	        PieChart.drawPie(g2, r.getBounds(), slices1);
	        g2.setColor(Color.black);
	        g2.drawString("dead", 580, 370);
	        g2.drawString("flee", 580, 410);
	        g2.drawString("search", 580, 450);
	        g2.drawString("recover flag", 580, 490);
	        g2.drawString("patrol", 580, 530);
	        g2.drawString("gaurd", 580, 570);
	        g2.drawString("flag carrier", 580, 610);
	        g2.setColor(Color.BLUE);
	        g2.fillRoundRect(550, 350, 20, 20, 10, 10);
	        g2.setColor(Color.green);
	        g2.fillRoundRect(550, 390, 20, 20, 10, 10);
	        g2.setColor(Color.yellow);
	        g2.fillRoundRect(550, 430, 20, 20, 10, 10);
	        g2.setColor(Color.orange);
	        g2.fillRoundRect(550, 470, 20, 20, 10, 10);
	        g2.setColor(Color.RED);
	        g2.fillRoundRect(550, 510, 20, 20, 10, 10);
	        g2.setColor(Color.DARK_GRAY);
	        g2.fillRoundRect(550, 550, 20, 20, 10, 10);
	        g2.setColor(Color.black);
	        g2.fillRoundRect(550, 590, 20, 20, 10, 10);
	        repaint();
	        //setVisible(true);
	        //stat1Display.paint(g2);
	    }
	
	    public void paintStat2( Graphics g2 )
	    {
	    	//PieChart[] slices = new PieChart[7];
	    	slices2[0] = new PieChart(sliceSizes2[0], Color.blue);
	        slices2[1] = new PieChart(sliceSizes2[1], Color.green);
	        slices2[2] = new PieChart(sliceSizes2[2], Color.yellow);
	        slices2[3] = new PieChart(sliceSizes2[3], Color.orange);
	        slices2[4] = new PieChart(sliceSizes2[4], Color.red);
	        slices2[5] = new PieChart(sliceSizes2[5], Color.DARK_GRAY);
	        slices2[6] = new PieChart(sliceSizes2[6], Color.black);
	        Rectangle r = new Rectangle();
	        r.setBounds(750, 150, 400, 400);
	        PieChart.drawPie(g2, r.getBounds(), slices2);
	        repaint();
	        //stat2Display.paint(g2);
	    }
	    
	    public void paintLegend(Graphics g2)
	    {
	    	Rectangle r1 = new Rectangle(450, 300, 20, 20);
	    	
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
    
    public void paintNoG()
    {
        simPaintNoG();
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
    protected abstract void simPaintNoG();
}

class RescueArea extends SimDrawPanel
{
    private final String soilTextureFilename = "images/textures/grass.jpg";
    private final String roofTextureFilename = "images/textures/bot_16.jpg";
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
    
    public void simPaintNoG()
    {
    	
    }
    
    private void paintWinner( Graphics2D g2 , String text)
    {    	
    	if(text != null)
    	{
    		Font font = new Font("SansSerif", Font.BOLD, 85);
    		g2.setFont(font);
    		g2.setColor(Color.RED);
    		g2.drawString(text, 100, 300);
    	}
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
    			a.draw(g2, GUI.getShowSight(), GUI.getShowHearing());
    			a.setSleepTime( delay );
    		}
    		if (b.isFlag())
    		{
    			g2.setPaint(roofTexture);
    			Flag f = (Flag)b;
    			f.setLocation();
    			f.draw(g2);
     		}
    		if (b.isObstacle())
    		{
    			Obstacle o = (Obstacle)b;
    			o.draw(g2);
    		}    	
    	}
    	paintWinner(g2, Simulator.whoCapturedFlag);
    }
}
 

/*
class SensCoverage extends SimDrawPanel
{
    private final static int clrSize     = 1023;
    private final static Color[]clrTable = new Color[clrSize + 1];
    //private final static GraphicsComponent gDisplay = new GraphicsComponent();

    private static String[] drop1Strings = { "Team1", "Team2", "Seeker1", "Seeker2", "Defender1", "Defender2" };
    private static String[] drop2Strings = { "Team1", "Team2", "Seeker1", "Seeker2", "Defender1", "Defender2" };

    private static JPanel dropDownP1 = new JPanel();//(new FlowLayout(FlowLayout.CENTER, 2, 2));
    private static JPanel dropDownP2 = new JPanel();//(new FlowLayout(FlowLayout.CENTER, 2, 2));

    
    private static JComboBox dropDown1 = new JComboBox(drop1Strings);
    private static JComboBox dropDown2 = new JComboBox(drop2Strings);

    //JButton testButton = new JButton("test");

    
    public SensCoverage()
    {
    	this.setBackground(Color.cyan);
    	this.setForeground(Color.cyan);
    	
    	JPanel dropDownP1 = new JPanel();
    	JPanel dropDownP2 = new JPanel();
    	
    	dropDownP1.setLayout(new FlowLayout());
    	dropDownP2.setLayout(new FlowLayout());
    	JComboBox dropDown1 = new JComboBox(drop1Strings);
    	JComboBox dropDown2 = new JComboBox(drop2Strings);
    	dropDownP1.add(dropDown1);
    	dropDownP2.add(dropDown2);
    	dropDownP1.setBorder(new TitledBorder("sdkjfhgdkjfh"));
    	
    	add(dropDownP1, BorderLayout.EAST);
    	add(dropDownP2, BorderLayout.SOUTH);
       // add(gDisplay);
        //setVisible(true);
    }
    
    public void simPaint( Graphics2D g2 )
    {
    	
    	PieChart[] slices = new PieChart[4];
        slices[0] = new PieChart(25, Color.red);
        slices[1] = new PieChart(35, Color.green);
        slices[2] = new PieChart(25, Color.pink);
        slices[3] = new PieChart(15, Color.blue);
        Rectangle r = new Rectangle();
        r.setBounds(30, 30, 50, 50);
        //PieChart.drawPie((Graphics2D)g2, r.getBounds(), slices);

        setVisible(true);
    	//gDisplay.paint(g2);

    }
    
    public void simPaintNoG()
    {
    	
    }
}
*/

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
	
	private final JSlider sldSpeed     = new JSlider( 100, 500, 100 );
	   
	private final Timer tmrSim;

    public BottomPanel( Timer tmr )
    {
        tmrSim = tmr;
        setBorder( BorderFactory.createEmptyBorder( 5, 2, 5, 2 ) );
        sldSpeed.setMajorTickSpacing( 50 );
        sldSpeed.setPaintTicks( true );

        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put( 100, new JLabel( "Fast" ) );
        labelTable.put( 250, new JLabel( "Medium" ) );
        labelTable.put( 500, new JLabel( "Slow" ) );

        sldSpeed.setLabelTable( labelTable );
        sldSpeed.setPaintLabels( true );
        sldSpeed.setInverted( true );

        sldSpeed.addChangeListener( new ChangeListener()
        {
            public void stateChanged( ChangeEvent e )
            {
                if ( !sldSpeed.getValueIsAdjusting() ) { tmrSim.setDelay( sldSpeed.getValue() ); }
            }
        } );

        add( sldSpeed );
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
                    	if (b.isFlag())
                    	{
                    		Flag f = (Flag)b;
                    		f.stop();
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
                    	if (b.isFlag())
                    	{
                    		Flag f = (Flag)b;
                    		f.start(false);
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