package eng.project.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Item {
    private final String name;
    private final TextureRegion icon;
    protected Item(String name, TextureRegion icon) {
        this.name = name;
        this.icon = icon;
    }

    public abstract boolean Use();

    public abstract void Recharge();
    /* If an item is in Recharging state, this should be called periodically to make it usable*/

    public String getName() {
        return name;
    }

    public TextureRegion getIcon() {
        return icon;
    }

    public int getMaxStackSize() {
        return 10;
    }

    public boolean canStackWith(Item other) {
        return other != null && getClass() == other.getClass() && name.equals(other.name);
    }
}
