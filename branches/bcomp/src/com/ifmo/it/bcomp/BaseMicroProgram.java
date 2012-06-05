/**
* $Id$
*/

package com.ifmo.it.bcomp;

public class BaseMicroProgram implements MicroProgram
{
	private static final String[][] mp = new String[][]{
		{"label1", "8080", null},
		{null, "0808", null}
	};

	public String[][] getMicroProgram()
	{
		return mp;
	}	
}
