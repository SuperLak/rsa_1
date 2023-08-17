package net.kalandoz.runic_sword_art.capability;

public interface IManaCapability {

    void consume(float points);
    void fill(float points);
    void set(float points);

    float getMana();

}
