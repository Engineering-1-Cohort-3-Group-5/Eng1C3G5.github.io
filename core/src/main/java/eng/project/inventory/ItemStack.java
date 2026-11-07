package eng.project.inventory;

public class ItemStack {
    private final Item item;
    private int count;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public boolean isFull() {
        return count >= item.getMaxStackSize();
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        count--;
    }

    public boolean isEmpty() {
        return count <= 0;
    }
}


