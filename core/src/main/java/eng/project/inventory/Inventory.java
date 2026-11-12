package eng.project.inventory;

/** Fixed size hotbar, where each slot holds a stack of identical items or null when empty */
public class Inventory {
    public static final int size = 5;

    private final ItemStack[] slots = new ItemStack[size];
    private int selectedIndex = 0;

    /**
     * Adds one item first onto an existing stack of the same kind with space
     * But if not it goes into the next empty slot
     * @return false if the hotbar is full
     */
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

    /** Uses one item from the selected stack, removing the stack once it is empty */
    public void useSelected() {
        ItemStack stack = slots[selectedIndex];
        if (stack == null) {
            return;
        }
        if (stack.getItem().Use()) {
            stack.decrease();
            if (stack.isEmpty()) {
                slots[selectedIndex] = null;
            }
        }
    }

    /** Selects a slot and ignores indexes outside the hotbar */
    public void select(int index) {
        if (index >= 0 && index < size) {
            selectedIndex = index;
        }
    }

    /** Moves hte selected by step using mouse scroll */
    public void scroll(int step) {
        selectedIndex = Math.floorMod(selectedIndex + step, size);
    }

    // Getters for Inventory
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

