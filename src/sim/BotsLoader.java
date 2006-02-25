package sim;

/**
 * Author: Anton Rebgun
 * Date:   Feb 24, 2006
 * Time:   10:08:33 PM
 */
public class BotsLoader
{
    private static final Simulator simulator = Simulator.getSimulator();

    public static void main( String[] arg )
    {
        simulator.initialize();
        simulator.gui.show();
    }
}
