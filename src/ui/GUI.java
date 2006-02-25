package ui;/**
 * @(#) GUI.java
 */

import sim.Simulator;

public class GUI
{
    private static GUI guiInstance;
    private static Simulator sim;

    private GUI()
    {
        // TODO: To change body of method.
    }

    public static GUI getInstance()
    {
        if ( guiInstance == null )
        {
            guiInstance = new GUI();
            sim = Simulator.getSimulator();
        }

        return guiInstance;
    }

    public void update()
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void show()
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    @SuppressWarnings( { "CloneDoesntCallSuperClone" } )
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}
