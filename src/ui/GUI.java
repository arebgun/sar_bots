package ui;

import agent.Agent;
import env.Environment;
import sim.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Iterator;

public class GUI
{
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private static GUI guiInstance;
    private static JFrame main;

    private RescueArea area;
    private SidePanel side;

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
                main.repaint();
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

        area = new RescueArea();
        setGrigBagConstraints( c, 0, 0, 1, 1, 1, 1 );
        main.add( area, c );

        side = new SidePanel();
        setGrigBagConstraints( c, 1, 0, 1, 1, 0, 0 );
        main.add( side, c );
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

    public RescueArea()
    {
        super();
        setBackground( Color.WHITE );
    }

    public void paint( Graphics g )
    {
        super.paint( g );

        int dX = getSize().width;
        int dY = getSize().height;

        Graphics2D g2 = (Graphics2D) g;

        // decorative border
        g2.setColor( Color.BLACK );
        g2.drawRect( 0, 0, dX - 1, dY - 1 );

        Environment.scaleGraphics( g2, dX, dY );
        paintEnvironment( g2 );
        paintAgents( g2 );

        /*
          g.setColor( Color.GRAY );
          g.fillRect( 50, 50, 60, 60 );

          g.setColor( new Color( 255, 204, 102, 125 ) );
          g.fillOval( 98, 98, 50, 50 );

          g.setColor( Color.RED );
          g.fillOval( 120, 120, 6, 6 );
      */
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

    private final Component buttonGlue = Box.createRigidArea( new Dimension( 0, 5 ) );
    private final Dimension buttonSize = new Dimension( 105, 25 );

    public SidePanel()
    {
        super();
        setBorder( BorderFactory.createEmptyBorder( 30, 20, 30, 20 ) );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );

        // TODO-DIMZAR-20060320: need to decide how the GUI events will be implemented
        addNewButton( new AbstractAction( "Start" )
        {
            public void actionPerformed( ActionEvent e )
            {
                new javax.swing.Timer( 100, new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        Simulator.step();
                    }
                } ).start();
            }
        } );
        /*
          addNewButton( "Step" );
          addNewButton( "Save" );
          addNewButton( "Pict" );
      */
    }

    public void paint( Graphics g )
    {
        super.paint( g );

        int dY = getSize().height;
        int dX = getSize().width;

        g.setColor( Color.BLACK );
        g.drawRect( 0, 0, dX - 1, dY - 1 );
    }

    private void addNewButton( Action action )
    {
        JButton button = new JButton( action );
        button.setPreferredSize( buttonSize );
        button.setMaximumSize( buttonSize );
        add( buttonGlue );
        add( button );
    }
}
