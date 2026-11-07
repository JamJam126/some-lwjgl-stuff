package entities.ore;

import java.util.List;

import org.joml.Vector3f;

import entities.Entity;
import entities.machine.Conveyor;
import main.Settings;
import models.TexturedModel;

public class Ore extends Entity {
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
    int col = Math.round(getPosition().x / Settings.TILE_SIZE);
    int row = Math.round(getPosition().z / Settings.TILE_SIZE);

    for (Conveyor c : conveyors) {
      int cCol = Math.round(c.getPosition().x / Settings.TILE_SIZE);
      int cRow = Math.round(c.getPosition().z / Settings.TILE_SIZE);

      if (cCol == col && cRow == row) return  c;
    }
    return null;
  }
  
}
