package agent.comm;

/*
 * Class Name:    agent.comm.None
 * Last Modified: 4/2/2006 3:5
 *
 * @author Anton Rebgun
 * @author Dimitri Zarzhitsky
 *
 * Source code may be freely copied and reused.
 * Please copy credits, and send any bug fixes to the authors.
 *
 * Copyright (c) 2006, University of Wyoming. All Rights Reserved.
 */

import config.ConfigAgent;

/**
 * Implements CommModule abstract class.
 * Does not do anything :)
 */
public class None extends CommModule
{
    public None( ConfigAgent config )
    {
        super( config );
    }
}
