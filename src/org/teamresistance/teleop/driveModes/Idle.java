package org.teamresistance.teleop.driveModes;

import org.teamresistance.IO;
import org.teamresistance.util.state.State;
import org.teamresistance.util.state.StateTransition;

public class Idle extends State {

	@Override
	public void onEntry(StateTransition e) {
		IO.robotDrive.tankDrive(0.0, 0.0);
	}

}
