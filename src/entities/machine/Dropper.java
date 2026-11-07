package entities.machine;

import org.joml.Vector3f;

import entities.Entity;
import main.Settings;
import models.TexturedModel;

public class Dropper extends Entity {

  public Dropper(
    TexturedModel model,
    Vector3f position,
    float rotY
  ) {
    super(model, position, 0, rotY, 0, Settings.TILE_SIZE);
  }
}
