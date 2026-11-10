package eng.project.inventory;

import eng.project.entity.Player;
import eng.project.entity.geese.GeeseManager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.w3c.dom.Text;

public abstract class Item {
    private float rechargeTime;
    private ItemState state;

    private final String name;
    private final TextureRegion icon;

    public abstract void Use();
    /* If an item is in Recharging state, this should be called periodically to make it usable*/
    public abstract void Recharge();

    protected Item(String name, TextureRegion icon) {
        this.name = name;
        this.icon = icon;
    }

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

    public abstract boolean use();
}
