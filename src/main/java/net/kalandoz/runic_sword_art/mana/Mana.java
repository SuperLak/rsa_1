package net.kalandoz.runic_sword_art.mana;

public class Mana {
    private String type;
    private int mana;
    private int maxMana;
    private int color;

    public Mana(String type, int mana, int color, int max) {
        this.type = type;
        this.mana = mana;
        this.color = color;
        this.maxMana = max;
    }

    public String getType() {
        return this.type;
    }

    public int getMana() {
        return this.mana;
    }

    public int getMaxMana() {
        return this.maxMana;
    }

    public int getColor() {
        return this.color;
    }

    public void consume(int points) {
        this.mana = Math.max(mana - points, 0);
    }

    public void fill(int points) {
        System.out.println("Filling mana!");
        this.mana =  Math.min(mana + points, this.maxMana);
    }

    public void set(int points) {
        this.mana = points;
    }

    public void setMax(int points) {
        this.maxMana = points;
    }
}
