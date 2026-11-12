package entities.machine;

import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import entities.Entity;
import entities.ore.Ore;
import main.Settings;
import models.TexturedModel;

public class Dropper extends Entity {

  private static final int SPAWN_INTERVAL = 120;
  private static final float CHUTE_END = 0.64f;

  private final TexturedModel oreModel;
  private final Random random = new Random();
  private int spawnTimer = 0;

  public Dropper(
    TexturedModel model,
    TexturedModel oreModel,
    Vector3f position,
    float rotY
  ) {
    super(model, position, 0, rotY, 0, Settings.TILE_SIZE);
    this.oreModel = oreModel;
  }

  public void update(List<Ore> ores) {
    spawnTimer++;
    if (spawnTimer >= SPAWN_INTERVAL) {
      spawnTimer = 0;
      ores.add(spawnOre());
    }
  }

  private Ore spawnOre() {
    double angle = Math.toRadians(getRotY());
    float offsetX = (float)   Math.cos(angle) * CHUTE_END;
    float offsetZ = (float) - Math.sin(angle) * CHUTE_END;

    Vector3f position = new Vector3f(
      getPosition().x + offsetX * Settings.TILE_SIZE,
      Settings.BELT_TOP * Settings.TILE_SIZE,
      getPosition().z + offsetZ * Settings.TILE_SIZE
    );

    float randomSpin = random.nextFloat() * 360;
    return new Ore(oreModel, position, 0, randomSpin, 0, Settings.TILE_SIZE);
  }
}
