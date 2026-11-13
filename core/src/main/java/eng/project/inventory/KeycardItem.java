package eng.project.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class KeycardItem extends Item {
    public KeycardItem(String name, TextureRegion icon) {
        super(name, icon);
    }

    @Override
    public boolean Use() {
        return false;
    }

    @Override
    public void Recharge() {
        // doesnt need to recharge
    }

    @Override
    public boolean isUsable() {
        return false;
    }
}
