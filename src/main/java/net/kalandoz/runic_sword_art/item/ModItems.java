package net.kalandoz.runic_sword_art.item;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.item.custom.FlameSwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RunicSwordArt.MOD_ID);

    public static final RegistryObject<Item> FLAME_SWORD = ITEMS.register("flame_sword",
            () -> new FlameSwordItem(ModRunicTier.RUNIC, 3, -2.4f,
                    new Item.Properties().isImmuneToFire().group(ModItemGroup.RUNIC_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}