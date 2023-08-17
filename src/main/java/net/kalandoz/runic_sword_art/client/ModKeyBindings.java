package net.kalandoz.runic_sword_art.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

public class ModKeyBindings {
    public static final String KEY_CATEGORY_TUTORIAL = "key.runic_sword_art.tutorial";
    public static final String MANA_KEY = "key.runic_sword_art.mana_key";
    public static final String PROJECTILE_KEY = "key.runic_sword_art.projectile_key";
    public static final String BURST_KEY = "key.runic_sword_art.burst_key";

    public static KeyBinding manaKey = new KeyBinding(MANA_KEY, KeyEvent.VK_U, KEY_CATEGORY_TUTORIAL);
    public static KeyBinding projectileKey = new KeyBinding(PROJECTILE_KEY, KeyEvent.VK_I, KEY_CATEGORY_TUTORIAL);
    public static KeyBinding burstKey = new KeyBinding(BURST_KEY, KeyEvent.VK_O, KEY_CATEGORY_TUTORIAL);

    public static void register(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(manaKey);
        ClientRegistry.registerKeyBinding(projectileKey);
        ClientRegistry.registerKeyBinding(burstKey);
    }
}