package entities.ore;

import java.util.List;

import org.joml.Vector3f;

import entities.Entity;
import entities.machine.Conveyor;
import models.TexturedModel;

public class Ore extends Entity {
  private final int value = 10;

  public Ore(
    TexturedModel model, 
    Vector3f position, 
    float rotX, 
    float rotY, 
    float rotZ, 
    float scale
  ) {
    super(model, position, rotX, rotY, rotZ, scale);
  }
  
  public void update(List<Conveyor> conveyors) {
    Conveyor below = findConveyor(conveyors);

    if (below == null) return;
    this.increasePosition(below.getDirX() * below.getSpeed(), 0, below.getDirZ() * below.getSpeed());
  }

  private Conveyor findConveyor(List<Conveyor> conveyors) {
    for (Conveyor c : conveyors) {
      if (isOnSameTile(c)) return c;
    }
    return null;
  }

  public int getValue() { return value; }
}
