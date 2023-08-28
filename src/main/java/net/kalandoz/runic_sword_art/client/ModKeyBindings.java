package net.kalandoz.runic_sword_art.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

public class ModKeyBindings {
    public static final String KEY_CATEGORY_TUTORIAL = "key.runic_sword_art.tutorial";
    public static final String PRIMARY_KEY = "key.runic_sword_art.primary_key";
    public static final String SECONDARY_KEY = "key.runic_sword_art.secondary_key";
    public static final String COMPOSITE_KEY = "key.runic_sword_art.composite_key";

    public static KeyBinding primaryKey = new KeyBinding(PRIMARY_KEY, KeyEvent.VK_U, KEY_CATEGORY_TUTORIAL);
    public static KeyBinding secondaryKey = new KeyBinding(SECONDARY_KEY, KeyEvent.VK_I, KEY_CATEGORY_TUTORIAL);
    public static KeyBinding compositeKey = new KeyBinding(COMPOSITE_KEY, KeyEvent.VK_O, KEY_CATEGORY_TUTORIAL);

    public static void register(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(primaryKey);
        ClientRegistry.registerKeyBinding(secondaryKey);
        ClientRegistry.registerKeyBinding(compositeKey);
    }
}