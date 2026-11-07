package eng.project.inventory;

public class Inventory {
    public static final int size = 5;

    private final ItemStack[] slots = new ItemStack[size];
    private int selectedIndex = 0;

    public boolean add(Item item) {
        for (ItemStack stack : slots) {
            if (stack != null && stack.getItem().canStackWith(item) && !stack.isFull()) {
                stack.increase();
                return true;
            }
        }

        for (int i = 0; i < size; i++) {
            if (slots[i] == null) {
                slots[i] = new ItemStack(item, 1);
                return true;
            }
        }
        return false;
    }

    public void useSelected() {
        ItemStack stack = slots[selectedIndex];
        if (stack == null) {
            return;
        }
        if (stack.getItem().use()) {
            stack.decrease();
            if (stack.isEmpty()) {
                slots[selectedIndex] = null;
            }
        }
    }

    public void select(int index) {
        if (index >= 0 && index < size) {
            selectedIndex = index;
        }
    }

    public void scroll(int step) {
        selectedIndex = Math.floorMod(selectedIndex + step, size);
    }

    public ItemStack get(int index) {
        return slots[index];
    }

    public ItemStack getSelectedStack() {
        return slots[selectedIndex];
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}

