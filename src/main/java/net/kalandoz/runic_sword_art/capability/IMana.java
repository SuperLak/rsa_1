package net.kalandoz.runic_sword_art.capability;

public interface IMana {

    void consume(int points);
    void fill(int points);
    void set(int points);

    int getMana();

    int getMax();

}
