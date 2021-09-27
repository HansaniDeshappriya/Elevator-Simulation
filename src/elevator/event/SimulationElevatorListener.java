package elevator.event;

public interface SimulationElevatorListener extends ElevatorBellListener, 
ElevatorButtonListener, ElevatorDoorListener, MoveElevatorListener, 
ElevatorLightListener, MovingPersonListener {
}
