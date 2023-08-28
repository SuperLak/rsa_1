package net.kalandoz.runic_sword_art.item.custom;

import net.kalandoz.runic_sword_art.utils.AttunementUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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
        if (attacker instanceof PlayerEntity) {
            if (AttunementUtils.consumePrimaryMana(null, (PlayerEntity) attacker, 5)) {
                target.setFire(5);

            } else {
                    stack.damageItem(1, attacker, (playerEntity) -> playerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            }
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // retrieves an instance of player for later applications
        PlayerEntity player = worldIn.getClosestPlayer(entityIn, 1);
        // fills the player's mana in 3 minutes
        if (tickCounter % 20 == 0 && isSelected) {
            AttunementUtils.fillAllManaProportionally(null, player, 300);
        }
        // activates when tickCounter reaches 40
        // (every 2 seconds)
        if (tickCounter == 40) {
            if (!worldIn.isRemote && player != null && isSelected) {
                // removes previous fire resistance effect (so that the following effect can be applied)
                player.removePotionEffect(Effects.FIRE_RESISTANCE);
                // give the player fire resistance for 2 minutes
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 400));
                // heals 1 durability from item
                stack.setDamage(stack.getDamage()-1);
                // resets tickCounter to 0
                tickCounter = 0;
            }
        } else {
            // increases tickCounter by 1
            tickCounter++;
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        /**
         * Acts as flint & steel when used on the ground
         */
        // retrieves an instance of world from context to later check if world is remote
        // (to prevent code triggering twice)
        World world = context.getWorld();
        // retrieves an instance of playerEntity from context to later apply fire resistance
        PlayerEntity player = context.getPlayer();
        // retrieves an instance of itemStack from context to later consume durability
        ItemStack stack = context.getItem();
        if(!world.isRemote && player != null) {
            // activates if the player isn't crouching
            if(!player.isCrouching()) {
                // sets nearby ground on fire semi-randomly
                lightGroundOnFire(context);
                // damages item (and breaks it if it would break)
                AttunementUtils.consumeSecondaryMana(null, player, 10);
            }
        }
        return super.onItemUse(context);
    }

    public static void lightGroundOnFire(ItemUseContext context) {
        /**
         * Sets ground on fire after making proper checks
         */
        // retrieves instance of player to play flint & steel sound later on
        PlayerEntity player = context.getPlayer();
        // retrieves instance of world to configure fire blockstate & later place fire
        World world = context.getWorld();
        BlockPos blockpos = context.getPos().offset(context.getFace());
        // checks if target block can be lit on fire
        if (AbstractFireBlock.canLightBlock(world, blockpos, context.getPlacementHorizontalFacing())) {
            // plays prerecorded sound of flint & steel being used
            world.playSound(player, blockpos, SoundEvents.ITEM_FLINTANDSTEEL_USE,
                    SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            // configures fire block (mandatory)
            BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, blockpos);
            // setting ground on fire
            world.setBlockState(blockpos, blockstate, 11);
        }
    }
}