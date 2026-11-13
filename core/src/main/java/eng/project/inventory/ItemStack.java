package eng.project.inventory;

/** A number of identical items sharing one hotbar slot */
public class ItemStack {
    private final Item item;
    private int count;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    // Getters for the ItemStack class
    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    /** Is true once the stack has reached the item's maximum stack size */
    public boolean isFull() {
        return count >= item.getMaxStackSize();
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        count--;
    }

    /** Is true when the stack has no items left */
    public boolean isEmpty() {
        return count <= 0;
    }
}
