package org.teamresistance.robostates.lifter;

import org.teamresistance.Constants;
import org.teamresistance.IO;
import org.teamresistance.JoystickIO;
import org.teamresistance.Robot;
import org.teamresistance.util.Time;
import org.teamresistance.util.state.State;
import org.teamresistance.util.state.StateTransition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LowerDrawbridge extends State {
	
	private double startTime;
	private boolean delay = false;
	private double delayStart;
	private boolean driveBack = false;

	@Override
	public void onEntry(StateTransition e) {
		startTime = Time.getTime();
		SmartDashboard.putBoolean("LowerDrawbridge", true);
		IO.lifterTiltSolenoid.set(true);
		IO.flipperSolenoid.set(false);
		if(!IO.topLifterSwitch.get()) {
			SmartDashboard.putBoolean("Lower Lifter", true);
			MoveLifter moveLifter = ((MoveLifter)stateMachine.getState("MoveLifter"));
			moveLifter.setReturnState(getName());
			moveLifter.moveUp(); // feels dirty, but works
			gotoState("MoveLifter");
			return;
		}
		IO.robotDrive.arcadeDrive(Constants.DRAWBRIDGE_MOVE_SPEED, 0);
		IO.lifterMotor.set(Constants.DRAWBRIDGE_LOWER_SPEED);
	}

	@Override
	public void update() {
		if(IO.middleLifterSwitch.get() && !delay) {
			IO.robotDrive.arcadeDrive(0, 0);
			delay = true;
			delayStart = Time.getTime();
		}
		
		if(Time.getTime() - startTime < Constants.DRAWBRIDGE_FORWARD_TIME) {
			IO.robotDrive.arcadeDrive(Constants.DRAWBRIDGE_FORWARD_SPEED, 0);
//			startTime = Time.getTime();
			driveBack = true;
		} else if(Time.getTime() - startTime < Constants.DRAWBRIGDE_BACKUP_TIME + Constants.DRAWBRIDGE_FORWARD_TIME){
			IO.robotDrive.arcadeDrive(Constants.DRAWBRIDGE_MOVE_SPEED, 0);	
		} else if(Time.getTime() - startTime < Constants.DRAWBRIDGE_FORWARD_START_TIME) {
			IO.robotDrive.arcadeDrive(0,0);
		} else if(Time.getTime() - startTime < Constants.DRAWBRIDGE_FORWARD_START_TIME + Constants.DRAWBRIDGE_FIRST_FORWARD_TIME) {
			IO.robotDrive.arcadeDrive(Constants.DRAWBRIDGE_FORWARD_SPEED, 0);	
		} else {
			IO.robotDrive.arcadeDrive(0, 0);
		}
		
		if(IO.bottomLifterSwitch.get()) {
			IO.lifterMotor.set(0);
			Robot.teleop.exitIdleDrive();
			gotoState("TeleopLifterIdle");
		} else if(delay && Time.getTime() - delayStart <= Constants.DRAWBRIDGE_LOWER_DELAY) {
			IO.lifterMotor.set(Constants.LIFTER_DOWN_SPEED);
		} else if(delay) {
			gotoState("LowerFlipper");
			IO.lifterMotor.set(0);
		} else if(JoystickIO.btnCancel.isDown()) {
			gotoState("TeleopLifterIdle");
		}
	}

	@Override
	public void onExit(StateTransition e) {
		IO.robotDrive.arcadeDrive(0, 0);
		delay = false;
		driveBack = false;
	}
}
