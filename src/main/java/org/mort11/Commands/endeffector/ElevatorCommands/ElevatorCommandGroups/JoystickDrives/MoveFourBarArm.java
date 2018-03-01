package org.mort11.Commands.endeffector.ElevatorCommands.ElevatorCommandGroups.JoystickDrives;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.mort11.Commands.endeffector.ElevatorCommands.ElevatorPositions.MoveFourBarArmDown;
import org.mort11.Commands.endeffector.ElevatorCommands.ElevatorPositions.MoveFourBarArmUp;

public class MoveFourBarArm extends CommandGroup {
    public MoveFourBarArm(double speed) {
        addSequential(new MoveFourBarArmUp(speed));
        addSequential(new MoveFourBarArmDown(speed));
    }
}
