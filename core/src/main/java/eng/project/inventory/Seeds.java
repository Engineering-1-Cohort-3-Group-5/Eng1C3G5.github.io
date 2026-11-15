package eng.project.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.entity.Player;
import eng.project.entity.food.FoodManager;

public class Seeds extends Item {
    private final FoodManager foodManager;
    private final Player player;

    public Seeds(FoodManager foodManager, Player player, String name, TextureRegion icon) {
        super(name, icon);
        this.foodManager = foodManager;
        this.player = player;
    }

    @Override
    public boolean Use() {
        foodManager.spawnFood(player.getX(), player.getY());
        return true;
    }
}
