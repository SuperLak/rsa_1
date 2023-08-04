package net.kalandoz.runic_sword_art.client;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.client.networking.ModMessages;
import net.kalandoz.runic_sword_art.client.networking.packet.BurstC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.FlameC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicSwordArt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world != null) {
            onInput(mc, event.getKey(), event.getAction());
        }
    }
    public static void onInput(Minecraft mc, int key, int action) {
        if (mc.currentScreen == null) {
            if (ModKeyBindings.burstKey.isPressed()){
                System.out.println("Pressed Burst Key!");
                ModMessages.sendToServer(new BurstC2SPacket());
            }
        }
    }
}