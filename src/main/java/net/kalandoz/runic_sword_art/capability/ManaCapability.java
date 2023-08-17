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

public class ManaCapability implements IManaCapability {

private float mana = 250.0F;
private final float MAX_MANA = 250.0F;
private final float MIN_MANA = 0.0F;

@Override
public void consume(float points) {
        this.mana = Math.max(mana - points, MIN_MANA);
        }

@Override
public void fill(float points) {
        this.mana =  Math.min(mana + points, MAX_MANA);
        }

@Override
public void set(float points) {
        this.mana = points;
        }

@Override
public float getMana() {
        return this.mana;
        }

@SubscribeEvent
public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event)
        {
        PlayerEntity player = event.getPlayer();
        IManaCapability mana = (IManaCapability) player.getCapability(ModCapabilities.MANA_CAP, null);

        String component = String.format("Hello there, you have §7%d§r mana left.", (int) mana.getMana());
        ITextComponent message = new StringTextComponent(component);
        Minecraft.getInstance().player.sendMessage(message, null);
        }

@SubscribeEvent
public void onPlayerSleep(PlayerSleepInBedEvent event)
        {
        PlayerEntity player = event.getPlayer();

        if (player.world.isRemote) return;

        IManaCapability mana = (IManaCapability) player.getCapability(ModCapabilities.MANA_CAP, null);

        mana.fill(50);

        String component = String.format("You refreshed yourself in the bed. You received 50 mana, you have §7%d§r mana left.", (int) mana.getMana());
        ITextComponent message = new StringTextComponent(component);
        Minecraft.getInstance().player.sendMessage(message, null);
        }

@SubscribeEvent
public void onPlayerFalls(LivingFallEvent event)
        {
        Entity entity = event.getEntity();

        if (entity.world.isRemote || !(entity instanceof PlayerEntity) || event.getDistance() < 3) return;

        PlayerEntity player = (PlayerEntity) entity;
        IManaCapability mana = (IManaCapability) player.getCapability(ModCapabilities.MANA_CAP, null);

        float points = mana.getMana();
        float cost = event.getDistance() * 2;

        if (points > cost)
        {
        mana.consume(cost);

        String component = String.format("You absorbed fall damage. It costed §7%d§r mana, you have §7%d§r mana left.", (int) cost, (int) mana.getMana());
        ITextComponent message = new StringTextComponent(component);
        Minecraft.getInstance().player.sendMessage(message, null);

        event.setCanceled(true);
        }
        }

@SubscribeEvent
public void onPlayerClone(PlayerEvent.Clone event)
        {
        PlayerEntity player = event.getPlayer();
        IManaCapability mana = (IManaCapability) player.getCapability(ModCapabilities.MANA_CAP, null);
        IManaCapability oldMana = (IManaCapability) event.getOriginal().getCapability(ModCapabilities.MANA_CAP, null);

        mana.set(oldMana.getMana());
        }
        }
