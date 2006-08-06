package ui;

/*
 * Class Name:    ui.GUI
 * Last Modified: 5/1/2006 3:21
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import agent.Agent;
import env.Environment;
import sim.Simulator;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
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

public class GUI
{
    private static final int DEFAULT_WIDTH  = 580;
    private static final int DEFAULT_HEIGHT = 540;
    private static boolean showGrid         = true;
    private final Timer tmrSim;
    private final Timer tmrUpdate;

    private static GUI guiInstance;
    private static JFrame main;
    private static JTabbedPane jtViewSwitcher;

    // Tab scroll panes
    private SimScrollPane area;
    private SimScrollPane coverage;

    // Side and bottom control panels
    private SidePanel side;
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

        // Set main window location (center of the screen)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locX             = (int) ( screenSize.getWidth()  / 2 - DEFAULT_WIDTH  / 2 );
        int locY             = (int) ( screenSize.getHeight() / 2 - DEFAULT_HEIGHT / 2 );

        // Create main window, size and position it on the screen
        main = new JFrame( "Search and Rescue Bots" );
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

    public static void setShowGrid( boolean show )
    {
        showGrid = show;
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
                side.repaint();
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

        // Create and add side panel (buttons and stats)
        side = new SidePanel( tmrSim );
        main.add( side, BorderLayout.LINE_END );

        // Create and add bottom panel (spped slider)
        bottom = new BottomPanel( tmrSim );
        main.add( bottom, BorderLayout.PAGE_END );
    }

    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
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
    private final Font fontAgentID           = new Font( "Monospaced", Font.PLAIN, 4 );
    private final GradientPaint fireGradient = new GradientPaint(     0,     0, new Color( 255,   0,  0, 200 ),
                                                                  1.25f, 1.25f, new Color( 255, 110, 30, 255 ), true );
    private final String soilTextureFilename = "images/textures/Laramie.jpg";
    private final String roofTextureFilename = "images/textures/mixed_tile.jpg";
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
        paintEnvironment( g2 );
        paintAgents( g2 );
    }

    private void paintEnvironment( Graphics2D g2 )
    {
        g2.setPaint( soilTexture );
        g2.fill( Environment.unoccupiedArea() );

        if ( GUI.getShowGrid() ) { paintGrid( g2 ); }

        g2.setPaint( roofTexture );
        g2.fill( Environment.getBuildings() );

        g2.setPaint( fireGradient );
        g2.fill( Environment.getFires() );
    }

    private void paintAgents( Graphics2D g2 )
    {
        int delay = GUI.getInstance().getDelay();

        g2.setFont( fontAgentID );
        Iterator<Agent> iter = Simulator.agentsIterator();

        while ( iter.hasNext() )
        {
            Agent agent            = iter.next();
            Area sensView          = agent.getSensorView();
            Area agentBody         = agent.getBodyArea();
            Rectangle2D bodyBounds = agentBody.getBounds2D();

            g2.setColor( agent.getSensorColor() );
            g2.fill( sensView );

            g2.setColor( Color.BLUE );
            g2.fill( agentBody );

            g2.drawString( String.valueOf( agent.getID() ), (float) bodyBounds.getX(), (float) bodyBounds.getY() );

            agent.setSleepTime( delay );
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
        Iterator<Double> fractionIter  = Environment.sensCoverageFractionIterator();
        Iterator<Rectangle2D> gridIter = Environment.gridIterator();

        while ( gridIter.hasNext() )
        {
            g2.setColor( clrTable[(int) ( fractionIter.next() * clrSize )] );
            g2.fill( gridIter.next() );
        }

        if ( GUI.getShowGrid() ) { paintGrid( g2 ); }
    }
}

class SidePanel extends JPanel
{
    private final JLabel lblStep           = new JLabel( "Time: ?" );
    private final JLabel lblNumFiresActive = new JLabel( "# of Active Fires: ?" );
    private final JLabel lblNumFiresFound  = new JLabel( "# of Fires Found: ?" );
    private final JLabel lblPercentCovered = new JLabel( "% Covered: ?" );
    private final JLabel lblWellCovered    = new JLabel( "% Well Covered: ?" );

    private final JButton btnStartStop  = new JButton( "Start" );
    private final JButton btnStep       = new JButton( "Step" );
    private final JButton btnReset      = new JButton( "Reset " );
    private final JButton btnScreenshot = new JButton( "Screenshot" );
    private final JButton btnSave       = new JButton( "Save" );

    private final Timer tmrSim;

    public SidePanel( Timer tmr )
    {
        super( new BorderLayout() );

        tmrSim = tmr;
        setBorder( BorderFactory.createEmptyBorder( 10, 10, 20, 10 ) );

        JPanel jpStats = new JPanel();
        jpStats.setLayout( new BoxLayout( jpStats, BoxLayout.Y_AXIS ) );
        jpStats.setBorder( BorderFactory.createTitledBorder( "Statistics" ) );
        jpStats.add( lblStep );
        jpStats.add( lblNumFiresActive );
        jpStats.add( lblNumFiresFound );
        jpStats.add( lblPercentCovered );
        jpStats.add( lblWellCovered );
        add( jpStats, BorderLayout.PAGE_START );

        JPanel jpCtrl = new JPanel();
        jpCtrl.setLayout( new BoxLayout( jpCtrl, BoxLayout.Y_AXIS ) );

        addConfiguredButton( jpCtrl, btnStartStop, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( tmrSim.isRunning() )
                {
                    GUI.getInstance().getTmrUpdate().stop();
                    tmrSim.stop();
                    Iterator<Agent> it = Simulator.agentsIterator();

                    while(it.hasNext())
                    {
                        Agent agent = it.next();
                        agent.stop();
                    }

                    btnStartStop.setText( "Start" );
                    btnStep.setEnabled( true );
                }
                else
                {
                    btnStep.setEnabled( false );
                    btnStartStop.setText( "Stop" );
                    tmrSim.start();
                    GUI.getInstance().getTmrUpdate().start();
                    Iterator<Agent> it = Simulator.agentsIterator();

                    while(it.hasNext())
                    {
                        Agent agent = it.next();
                        agent.start(false);
                    }
                }
            }
        } );

        addConfiguredButton( jpCtrl, btnStep, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Simulator.step();
                Iterator<Agent> it = Simulator.agentsIterator();

                while(it.hasNext())
                {
                    Agent agent = it.next();
                    agent.start(true);
                }

                GUI.getInstance().update();
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
        lblStep.setText( "Time: " + Simulator.getTime() );
        lblNumFiresActive.setText( "# of Active Fires: " + Environment.getActiveFires() );
        lblNumFiresFound.setText ( "# of Fires Found: "  + Environment.getFoundFires() );
        lblPercentCovered.setText( "% Covered: "   + Environment.getTotalCoveragePercentage() );
        lblWellCovered.setText( "% Well Covered: " + Environment.getWellCoveredPercentage() );
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

class BottomPanel extends JPanel
{
    private final JSlider sldSpeed     = new JSlider( 100, 500, 100 );
    private final JCheckBox cbShowGrid = new JCheckBox( "Show Grid", true );
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

        cbShowGrid.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                GUI.setShowGrid( cbShowGrid.isSelected() );
                GUI.getSelectedTabView().repaint();
            }
        } );

        add( cbShowGrid );
    }
}
