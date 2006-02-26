package env;

/**
 * @(#) Environment.java
 */

import sim.Simulator;

public class Environment
{
    private static Environment environmentInstance;
    private static Simulator sim;

    private Environment() {}

    public static Environment getInstance()
    {
        if ( environmentInstance == null )
        {
            environmentInstance = new Environment();
            sim = Simulator.getSimulator();
        }

        return environmentInstance;
    }

    public void update()
    {
        //To change body of created methods use File | Settings | File Templates.
    }

    @SuppressWarnings( { "CloneDoesntCallSuperClone" } )
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}
