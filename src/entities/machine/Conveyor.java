package entities.machine;

import org.joml.Vector3f;

import entities.Entity;
import main.Settings;
import models.TexturedModel;

public class Conveyor extends Entity {

  private final int dirX;
  private final int dirZ;

  public Conveyor(
    TexturedModel model, 
    Vector3f position, 
    float rotY
  ) {
    super(model, position, 0, rotY, 0, Settings.TILE_SIZE);

    // TURN THE ROTATION INTO 0, 1, 2, OR 3 QUATER TURNS
    int quarterTurns = Math.floorMod(Math.round(rotY / 90f), 4);
    
    switch (quarterTurns) {
      case 0:  dirX =  1; dirZ =  0; break;  // 0   -> +x
      case 1:  dirX =  0; dirZ = -1; break;  // 90  -> -z
      case 2:  dirX = -1; dirZ =  0; break;  // 180 -> -x
      default: dirX =  0; dirZ =  1; break;  // 270 -> +z
    }
  }
  
  public int getDirX() { return dirX; }
  public int getDirZ() { return dirZ; }

}
