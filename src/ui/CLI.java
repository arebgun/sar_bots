package ui;

import sim.Simulator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * Class Name:    ui.CLI
 * Last Modified: 5/1/2006 3:29
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

public class CLI
{
    private final Timer tmrSim;

    private static CLI cliInstance;
    private static JFrame main;

    // Tab scroll panes
    private SimScrollPane area;

    private int delay = 0;

    private CLI()
    {
        // Create main window
        main = new JFrame( "Search and Rescue Bots" );
        main.setLayout( new BorderLayout() );

        tmrSim = new Timer( 0, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Simulator.step();
                delay = tmrSim.getDelay() / 10;
            }
        } );
    }

    public static CLI getInstance()
    {
        if ( cliInstance == null ) { cliInstance = new CLI(); }
        return cliInstance;
    }

    public int getDelay()
    {
        return delay;
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

    private void build()
    {
        addComponents();
    }

    private void addComponents()
    {
        // Main simulator window scroll pane and rescue area
        area = new SimScrollPane( new JPanel(), new RescueArea() );
        main.add( area, BorderLayout.CENTER );
    }

    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}
