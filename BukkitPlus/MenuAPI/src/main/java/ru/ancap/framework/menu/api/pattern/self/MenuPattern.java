package ru.ancap.framework.menu.api.pattern.self;

public interface MenuPattern {

    int rows();
    int[] slotsOf(char ch);

    default int slotOf(char ch) {
        int[] slots = this.slotsOf(ch);
        if (slots.length != 1) {
            throw new IllegalStateException("Pattern should contain only 1 slot with char \""+ch+"\"!");
        }
        return slots[0];
    }

}
