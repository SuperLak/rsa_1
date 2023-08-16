package net.kalandoz.runic_sword_art.client;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.client.renderer.entity.FlameArcRenderer;
import net.kalandoz.runic_sword_art.world.entity.ModEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RunicSwordArt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

    @SubscribeEvent
    public static void clientSetupEvent(FMLClientSetupEvent event) {
        event.setPhase(EventPriority.HIGH);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.FLAME_ARC.get(), FlameArcRenderer::new);
    }
}
