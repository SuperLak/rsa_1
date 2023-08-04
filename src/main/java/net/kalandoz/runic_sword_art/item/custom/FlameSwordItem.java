package net.kalandoz.runic_sword_art.item.custom;

import mcp.MethodsReturnNonnullByDefault;
import net.kalandoz.runic_sword_art.utils.RunicUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class FlameSwordItem extends SwordItem {

    private int tickCounter = 0;

    public FlameSwordItem(IItemTier tier, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(tier, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        /*
        displays information about the item
         */
        if (Screen.hasShiftDown()) {
            // prompts user to hold shift
            tooltip.add(new TranslationTextComponent("tooltip.runic_sword_art.flame_sword_shift"));
        } else {
            // displays short summary of item's abilities
            tooltip.add(new TranslationTextComponent("tooltip.runic_sword_art.flame_sword"));
        }
        super.addInformation(stack, world, tooltip, flagIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // sets target on fire for a number of seconds
        target.setFire(5);
        stack.damageItem(1, attacker, (playerEntity) -> playerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (!worldIn.isRemote && player != null) {
            if (tickCounter == 40) {
                // removes previous fire resistance effect (so that the following effect can be applied)
                player.removePotionEffect(Effects.FIRE_RESISTANCE);
                // give the player fire resistance for 2 minutes
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 400));
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        // retrieves an instance of world from context to later check if world is remote
        // (to prevent code triggering twice)
        World world = context.getWorld();
        // retrieves an instance of playerEntity from context to later apply fire resistance
        PlayerEntity player = context.getPlayer();
        // retrieves an instance of itemStack from context to later consume durability
        ItemStack stack = context.getItem();
        if(!world.isRemote && player != null) {
            // gives the player fire resistance for 30 seconds
            player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600));
            // activates if the player isn't crouching
            if(!player.isCrouching()) {
                // sets nearby ground on fire semi-randomly
                lightGroundOnFire(context);
                // damages item (and breaks it if it would break)
                stack.damageItem(25, player, playerEntity -> playerEntity.sendBreakAnimation(playerEntity.getActiveHand()));
            }
        }
        return super.onItemUse(context);
    }

    public static void lightGroundOnFire(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockpos = context.getPos().offset(context.getFace());
        if (AbstractFireBlock.canLightBlock(world, blockpos, context.getPlacementHorizontalFacing())) {
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_FLINTANDSTEEL_USE,
                    SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, blockpos);
            world.setBlockState(blockpos, blockstate, 11);
        }
    }
}