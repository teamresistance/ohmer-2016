package org.teamresistance.robostates.lifter;

import org.teamresistance.IO;
import org.teamresistance.util.state.State;
import org.teamresistance.util.state.StateTransition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftPortcullis extends State {
	
	@Override
	public void init() {
		
	}

	@Override
	public void onEntry(StateTransition e) {
		IO.lifterTiltSolenoid.set(false);
		if(!IO.bottomLifterSwitch.get()) {
			((MoveLifterDown)stateMachine.getState("MoveLifterDown")).setReturnState(getName());
			gotoState("MoveLifterDown");
		} else {
			gotoState("RaiseFlipper");
		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public void onExit(StateTransition e) {
	}

}
