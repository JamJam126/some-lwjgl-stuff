package entities.ore;

import org.joml.Vector3f;

import entities.Entity;
import models.TexturedModel;

public class Ore extends Entity {
  private static float SPEED = 0.01f;

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
  
  public void update() {
    this.increasePosition(SPEED, 0, 0);
  }
}
