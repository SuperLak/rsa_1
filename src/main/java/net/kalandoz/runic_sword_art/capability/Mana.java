package net.kalandoz.runic_sword_art.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Mana implements IMana {
        private int mana;
        private final int MAX_MANA = 250;
        private final int MIN_MANA = 0;

        public Mana() {
                mana = 0;
        }

        @Override
        public void consume(int points) {
                this.mana = Math.max(mana - points, MIN_MANA);
        }

        @Override
        public void fill(int points) {
        this.mana =  Math.min(mana + points, MAX_MANA);
        }

        @Override
        public void set(int points) {
        this.mana = points;
        }

        @Override
        public int getMana() {
        return this.mana;
        }

        @Override
        public int getMax() {
                return this.MAX_MANA;
        }
@SubscribeEvent
public void onPlayerClone(PlayerEvent.Clone event)
        {
        PlayerEntity player = event.getPlayer();
        IMana mana = (IMana) player.getCapability(ModCapabilities.MANA_CAP, null);
        IMana oldMana = (IMana) event.getOriginal().getCapability(ModCapabilities.MANA_CAP, null);

        mana.set(oldMana.getMana());
        }
        }
