package ui;

import agent.Agent;
import env.Environment;
import sim.Simulator;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.*;
import static java.lang.Math.max;

public class GUI
{
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private static GUI guiInstance;
    private static JFrame main;

    private static JTabbedPane jtViewSwitcher;
    private RescueArea area;
    private SidePanel side;
    private BottomPanel bottom;


    private final Timer tmrSim = new javax.swing.Timer( 0, new ActionListener()
    {
        public void actionPerformed( ActionEvent e )
        {
            Simulator.step();
        }
    } );

    // properties specific to the GUI (dumped as a serialized object)
    /* zoom
       refresh rate
       window size
       position
    */

    private GUI()
    {
        // Set window decorations (minimize, maximize, close, etc. buttons)
        JFrame.setDefaultLookAndFeelDecorated( true );

        // Set application icon, if not found system default will be used
        URL iconURL = ClassLoader.getSystemClassLoader().getResource( "images/bot_16.gif" );
        Image icon = null;
        if ( iconURL != null ) { icon = new ImageIcon( iconURL.getPath() ).getImage(); }

        // Set main window location (center of the screen)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locX = (int) ( screenSize.getWidth() / 2 - DEFAULT_WIDTH / 2 );
        int locY = (int) ( screenSize.getHeight() / 2 - DEFAULT_HEIGHT / 2 );

        // Create main window, size and position it on the screen
        main = new JFrame( "Search and Rescue Bots" );
        main.setIconImage( icon );
        main.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        main.setMinimumSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );
        main.setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );
        main.setLocation( locX, locY );
        main.setLayout( new GridBagLayout() );


        main.addComponentListener( new ComponentAdapter()
        {
            public void componentResized( ComponentEvent e )
            {
                Dimension dim = jtViewSwitcher.getSize();
                //Environment.scaleRescueArea(this, Environment.optimalZoom( dim.width, dim.height ) );
                int zoom = (int) max( dim.width / 100., dim.height / 100. );

                area.setPreferredSize( new Dimension( zoom * 100, zoom * 100 ) );
            }
        } );

    }

    public static GUI getInstance()
    {
        if ( guiInstance == null ) { guiInstance = new GUI(); }

        return guiInstance;
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
                area.repaint();
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
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets( 2, 2, 2, 2 );

        area = new RescueArea( tmrSim );
        //Environment.scaleRescueArea(area, DEFAULT_ZOOM);

        JScrollPane scrlPane = new JScrollPane( area );
        /*
            scrlPane.setMinimumSize(area.getSize());
            scrlPane.setSize(area.getSize());
            scrlPane.setPreferredSize(area.getSize());
        */

        jtViewSwitcher = new JTabbedPane( JTabbedPane.BOTTOM );
        jtViewSwitcher.addTab( "Environment", scrlPane );
        jtViewSwitcher.addTab( "Sensor Coverage", null );

        //jtViewSwitcher.setMinimumSize(area.getSize());
        //jtViewSwitcher.setSize(area.getSize());
        //jtViewSwitcher.setPreferredSize(area.getSize());

        setGrigBagConstraints( c, 0, 0, 1, 1, 1, 1 );
        main.add( jtViewSwitcher, c );

        side = new SidePanel( tmrSim );
        c.fill = GridBagConstraints.VERTICAL;
        setGrigBagConstraints( c, 1, 0, 1, 1, 0, 0 );
        main.add( side, c );

        bottom = new BottomPanel( tmrSim );
        c.fill = GridBagConstraints.HORIZONTAL;
        setGrigBagConstraints( c, 0, 1, 2, 1, 0, 0 );
        main.add( bottom, c );
    }

    private void setGrigBagConstraints( GridBagConstraints c, int x, int y, int w, int h, double wx, double wy )
    {
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.gridheight = h;
        c.weightx = wx;
        c.weighty = wy;
    }

    @SuppressWarnings( { "CloneDoesntCallSuperClone" } )
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}

class RescueArea extends JPanel
{
    private static final Color clrAgentSensor = new Color( 0, 255, 170, 255 );
    private final Timer tmrSim;

    public RescueArea( Timer tmr )
    {
        tmrSim = tmr;
        setBackground( Color.WHITE );
    }

    public void paint( Graphics g )
    {
        super.paint( g );
        int dX = getSize().width, dY = getSize().height;
        Graphics2D g2 = (Graphics2D) g;

        // decorative border
        g2.setColor( Color.RED );
        g2.drawRect( 0, 0, dX - 1, dY - 1 );

        Environment.scaleGraphics( g2, dX, dY );
	paintGrid( g2 );
        paintEnvironment( g2 );
        paintAgents( g2 );
    }

    private void paintGrid( Graphics2D g2 )
    {
        g2.setColor( Color.GRAY );
	g2.setStroke( new BasicStroke( 0.3f ) );
        Iterator<Rectangle2D> iter = Environment.gridIterator();

        while ( iter.hasNext() )
        {
            g2.draw( iter.next() );
        }
    }	

    private void paintEnvironment( Graphics2D g2 )
    {
        g2.setColor( Color.BLACK );

        Iterator<Polygon> iter = Environment.buildingsIterator();

        while ( iter.hasNext() )
        {
            g2.fillPolygon( iter.next() );
        }
    }


    private void paintAgents( Graphics2D g2 )
    {
        Iterator<Agent> iter = Simulator.agentsIterator();

        while ( iter.hasNext() )
        {
            Agent agent = iter.next();
            g2.setColor( clrAgentSensor );
            g2.fill( agent.getSensorView() );
            g2.setColor( Color.BLUE );
            g2.fill( agent.getBodyArea() );
        }
    }
}

class SidePanel extends JPanel
{
    private final JLabel lblStep           = new JLabel( "Step: " );
    private final JLabel lblNumFiresActive = new JLabel( "# of Active Fires: " );
    private final JLabel lblNumFiresFound  = new JLabel( "# of Fires Found: " );

    private final JButton btnStartStop = new JButton( "Start" );
    private final JButton btnStep = new JButton( "Step" );
    private final JButton btnScreenshot = new JButton( "Screenshot" );
    private final JButton btnSave = new JButton( "Save" );

    private final Timer tmrSim;

    public SidePanel( Timer tmr )
    {
        tmrSim = tmr;
        setBorder( BorderFactory.createEmptyBorder( 30, 20, 30, 20 ) );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );

	JPanel jpStats = new JPanel( );
	jpStats.setLayout( new BoxLayout(jpStats, BoxLayout.Y_AXIS) );
	jpStats.setBorder( BorderFactory.createTitledBorder("Statistics") );
	jpStats.add( lblStep );
	jpStats.add( lblNumFiresActive );
	jpStats.add( lblNumFiresFound );
	add( jpStats );

        addConfiguredButton( btnStartStop, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if ( tmrSim.isRunning() )
                {
                    tmrSim.stop();
                    btnStartStop.setText( "Start" );
                    btnStep.setEnabled( true );
                }
                else
                {
                    btnStep.setEnabled( false );
                    btnStartStop.setText( "Stop" );
                    tmrSim.start();
                }
            }
        } );

        addConfiguredButton( btnStep, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Simulator.step();
            }
        } );

        addConfiguredButton( btnScreenshot, null );
        addConfiguredButton( btnSave, null );
    }

    public void paint( Graphics g )
    {
        super.paint( g );

        int dY = getSize().height;
        int dX = getSize().width;

        g.setColor( Color.BLACK );
        g.drawRect( 0, 0, dX - 1, dY - 1 );
    }

    private void addConfiguredButton( JButton button, ActionListener action )
    {
        Component buttonGlue = Box.createRigidArea( new Dimension( 0, 5 ) );
        Dimension buttonSize = new Dimension( 105, 25 );

        button.addActionListener( action );
        button.setPreferredSize( buttonSize );
        button.setMaximumSize( buttonSize );
        add( buttonGlue );
        add( button );
    }
}

class BottomPanel extends JPanel
{
    private final JSlider sldSpeed = new JSlider( 100, 5100, 100 );
    private final Timer tmrSim;

    public BottomPanel( Timer tmr )
    {
        tmrSim = tmr;
        sldSpeed.setMajorTickSpacing( 1000 );
        sldSpeed.setPaintTicks( true );

        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put( 100, new JLabel( "Fast" ) );
        labelTable.put( 2500, new JLabel( "Medium" ) );
        labelTable.put( 5000, new JLabel( "Slow" ) );

        sldSpeed.setLabelTable( labelTable );
        sldSpeed.setPaintLabels( true );
        sldSpeed.setInverted( true );

        sldSpeed.addChangeListener( new ChangeListener()
        {
            public void stateChanged( ChangeEvent e )
            {
                if ( !sldSpeed.getValueIsAdjusting() )
                {
                    tmrSim.setDelay( sldSpeed.getValue() );
                }
            }
        } );

        add( sldSpeed );
    }

    public void paint( Graphics g )
    {
        super.paint( g );
        int dX = getSize().width, dY = getSize().height;
        Graphics2D g2 = (Graphics2D) g;

        // decorative border
        g2.setColor( Color.BLACK );
        g2.drawRect( 0, 0, dX - 1, dY - 1 );
    }

}
