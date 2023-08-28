package net.kalandoz.runic_sword_art.client;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.client.gui.ModHudOverlay;
import net.kalandoz.runic_sword_art.client.networking.ModMessages;
import net.kalandoz.runic_sword_art.client.networking.packet.CompositeC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.PrimaryC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.SecondaryC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
            if (ModKeyBindings.primaryKey.isPressed()) {
                ModMessages.sendToServer(new PrimaryC2SPacket());
            }
            if (ModKeyBindings.secondaryKey.isPressed()) {
                ModMessages.sendToServer(new SecondaryC2SPacket());
            }
            if (ModKeyBindings.compositeKey.isPressed()) {
                    ModMessages.sendToServer(new CompositeC2SPacket());
            }
        }
    }

    @SubscribeEvent
    public static void renderGuiOverlays(RenderGameOverlayEvent event) {
        new ModHudOverlay(Minecraft.getInstance()).renderIngameGui(event.getMatrixStack(), event.getPartialTicks());
    }
}