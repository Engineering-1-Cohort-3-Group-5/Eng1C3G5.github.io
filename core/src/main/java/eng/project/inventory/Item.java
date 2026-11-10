package eng.project.inventory;

import eng.project.entity.Player;
import eng.project.entity.geese.GeeseManager;

public abstract class Item {
    private float rechargeTime;
    private ItemState state;
    public abstract void Use();
    /* If an item is in Recharging state, this should be called periodically to make it usable*/
    public abstract void Recharge();
}
