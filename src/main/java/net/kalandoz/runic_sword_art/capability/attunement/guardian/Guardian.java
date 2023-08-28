package net.kalandoz.runic_sword_art.capability.attunement.guardian;

import net.kalandoz.runic_sword_art.capability.attunement.Attunement;
import net.kalandoz.runic_sword_art.mana.ManaTypes;
import net.kalandoz.runic_sword_art.utils.AttunementUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.network.NetworkEvent;

public class Guardian extends Attunement {

    public Guardian(int max) {
        this.primaryMana = ManaTypes.getEnhancement(0,max/2);
        this.secondaryMana = ManaTypes.getLife(0,max/2);
        this.level = 0;
    }

    @Override
    public void setPrimary(int mana, int max) {
        this.primaryMana = ManaTypes.getEnhancement(mana, max);
    }

    @Override
    public void setSecondary(int mana, int max) {
        this.secondaryMana = ManaTypes.getLife(mana, max);
    }

    @Override
    public void primaryAbility(NetworkEvent.Context context) {
        ServerPlayerEntity playerIn = context.getSender();
        if (playerIn != null) {
            if (AttunementUtils.consumePrimaryMana(null, playerIn, 10)) {
                System.out.println("Activating primary!");
                playerIn.removePotionEffect(Effects.STRENGTH);
                playerIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10 * 20));
            }
        }
    }

    @Override
    public void secondaryAbility(NetworkEvent.Context context) {
        ServerPlayerEntity playerIn = context.getSender();
        if (playerIn != null) {
            if (AttunementUtils.consumeSecondaryMana(null, playerIn, 15)) {
                System.out.println("Activating secondary!");
                playerIn.removePotionEffect(Effects.REGENERATION);
                playerIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 10 * 20));
            }
        }
    }

    @Override
    public void compositeAbility(NetworkEvent.Context context) {
        ServerPlayerEntity playerIn = context.getSender();
        if (playerIn != null) {
            if (AttunementUtils.consumePrimaryMana(null, playerIn, 10) &&
                    AttunementUtils.consumeSecondaryMana(null, playerIn, 10)) {
                System.out.println("Activating composite!");
                playerIn.removePotionEffect(Effects.RESISTANCE);
                playerIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 10 * 20));
            }
        }
    }
}
