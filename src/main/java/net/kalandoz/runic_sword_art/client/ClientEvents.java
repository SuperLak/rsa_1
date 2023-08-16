package net.kalandoz.runic_sword_art.client;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.client.networking.ModMessages;
import net.kalandoz.runic_sword_art.client.networking.packet.BurstC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.ProjectileC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicSwordArt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static int projectileCooldown;
    private static int burstCooldown;
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world != null) {
            onInput(mc, event.getKey(), event.getAction());
        }
    }
    public static void onInput(Minecraft mc, int key, int action) {
        System.out.println("Pressed Projectile Key! Previous cooldown: " + projectileCooldown);
        if (ModKeyBindings.projectileKey.isPressed() && projectileCooldown <= 0){
            projectileCooldown = 25;
            System.out.println("Current cooldown: " + projectileCooldown);
            ModMessages.sendToServer(new ProjectileC2SPacket());
        }
        if (mc.currentScreen == null && burstCooldown <= 0) {
            System.out.println("Pressed Burst Key! Previous cooldown: " + burstCooldown);
            if (ModKeyBindings.burstKey.isPressed()){
                burstCooldown = 50;
                System.out.println("Current cooldown: " + burstCooldown);
                ModMessages.sendToServer(new BurstC2SPacket());
            }
        }
    }

    public static void decrementAllCooldowns() {
        projectileCooldown--;
        burstCooldown--;
    }
}