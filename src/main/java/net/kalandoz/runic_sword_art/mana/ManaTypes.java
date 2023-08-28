package net.kalandoz.runic_sword_art.mana;

public class ManaTypes {
    public static Mana getGray(int mana, int max) {
        return new Mana("Gray", mana, 0xA4A4A4, max);
    }

    public static Mana getLife(int mana, int max) {
        return new Mana("Life", mana, 0x80FF53, max);
    }

    public static Mana getEnhancement(int mana, int max) {
        return new Mana("Enhancement", mana, 0xFF66E2, max);
    }
}
