package net.kalandoz.runic_sword_art.capability.attunement;

import net.kalandoz.runic_sword_art.capability.attunement.IAttunement;
import net.kalandoz.runic_sword_art.mana.Mana;
import net.kalandoz.runic_sword_art.mana.ManaTypes;
import net.minecraftforge.fml.network.NetworkEvent;

public abstract class Attunement implements IAttunement {
    protected Mana primaryMana;
    protected Mana secondaryMana;
    // numbers correspond with advancement levels (ex. 0 = quartz)
    protected int level;

    protected Attunement() {
    }

    @Override
    public void setPrimary(int mana, int max) {
        this.primaryMana = ManaTypes.getGray(mana, max);
    }

    @Override
    public void setSecondary(int mana, int max) {
        this.secondaryMana = ManaTypes.getGray(mana, max);
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public Mana getManaPrimary() {
        return this.primaryMana;
    }

    @Override
    public Mana getManaSecondary() {
        return this.secondaryMana;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    public void primaryAbility(NetworkEvent.Context context) {
        System.out.println("Unable to Override Attunement Class");
    }
    public abstract void secondaryAbility(NetworkEvent.Context context);
    public abstract void compositeAbility(NetworkEvent.Context context);
}
