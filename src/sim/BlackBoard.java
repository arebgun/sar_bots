package sim;/**
 * @(#) BlackBoard.java
 */

import agent.Agent;

public class BlackBoard
{
    private static BlackBoard blackBoardInstance;

    private BlackBoard() {}

    public static BlackBoard getInstance()
    {
        if ( blackBoardInstance == null )
        {
            blackBoardInstance = new BlackBoard();
        }

        return blackBoardInstance;
    }

    public void agentMoved( Agent agent )
    {
        // TODO: To change body of method.
    }

    @SuppressWarnings( { "CloneDoesntCallSuperClone" } )
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}
