package net.kalandoz.runic_sword_art.capability.attunement;

import net.kalandoz.runic_sword_art.mana.Mana;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IAttunement {

    void setPrimary(int mana, int max);

    void setSecondary(int mana, int max);
    void setLevel(int points);
    Mana getManaPrimary();
    Mana getManaSecondary();
    int getLevel();

    void primaryAbility(NetworkEvent.Context context);
    void secondaryAbility(NetworkEvent.Context context);
    void compositeAbility(NetworkEvent.Context context);
}
