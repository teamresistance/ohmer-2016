package org.teamresistance.robostates.lifter;

import org.teamresistance.IO;
import org.teamresistance.JoystickIO;
import org.teamresistance.util.state.ReturnState;
import org.teamresistance.util.state.StateTransition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LowerFlipper extends ReturnState {

	@Override
	public void onEntry(StateTransition e) {
		SmartDashboard.putBoolean("LowerFlipper", true);
		IO.flipperSolenoid.set(true);
	}

	@Override
	public void update() {
		if(IO.topFlipperSwitch.get()) {
			gotoState("DriveThroughDrawbridge");
		} else if(JoystickIO.btnCancel.isDown()) {
			gotoState("TeleopLifterIdle");
		}
	}

	@Override
	public void onExit(StateTransition e) {
		IO.lifterTiltSolenoid.set(false);
	}

}
