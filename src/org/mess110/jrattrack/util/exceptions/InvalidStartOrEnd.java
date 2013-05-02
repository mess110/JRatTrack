package org.mess110.jrattrack.util.exceptions;

public class InvalidStartOrEnd extends JRatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4710932949093662349L;

	public InvalidStartOrEnd() {
		super("start and end must be valid numbers >= 0. the unit is expressed in seconds");
	}

}
