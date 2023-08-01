package net.kalandoz.runic_sword_art.item.custom;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FlameSwordItem extends SwordItem {
    public FlameSwordItem(IItemTier tier, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(tier, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    @Override
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
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // sets target on fire for a number of seconds
        target.setFire(5);
        stack.damageItem(1, attacker, (playerEntity) -> playerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        return true;
    }

    private void lightGroundOnFire(ItemUseContext context) {
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

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack stack = context.getItem();

        // retrieves an instance of world from context to later check if world is remote
        // (to prevent code triggering twice)
        // retrieves an instance of playerplayerEntity from context to later apply fire resistance
        // retrieves an instance of itemStack from context to later consume durability
        if(!world.isRemote) {
            // gives the player fire resistance for 30 seconds
            playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600));
            // activates if the player isn't crouching
            if(!playerEntity.isCrouching()) {
                // sets nearby ground on fire semi-randomly
                // burnNearbyBlocks(world, playerEntity.getPositionVec(), playerEntity, 2);
                lightGroundOnFire(context);
                // damages item (and breaks it if it would break)
                stack.damageItem(25, playerEntity, player -> player.sendBreakAnimation(playerEntity.getActiveHand()));

            }
            // activates if the player is crouching
            else {
                // removes previous fire resistance effect (so that the following effect can be applied)
                playerEntity.removePotionEffect(Effects.FIRE_RESISTANCE);
                // give the player fire resistance for 2 minutes
                playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2400));
                // consumes 150 durability from the flame sword
                stack.damageItem(150, playerEntity, player -> player.sendBreakAnimation(playerEntity.getActiveHand()));
            }
        }
        super.onItemUse(context);
        return ActionResultType.SUCCESS;
    }
}
