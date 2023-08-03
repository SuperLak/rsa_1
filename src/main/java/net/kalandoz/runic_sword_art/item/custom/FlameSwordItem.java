package net.kalandoz.runic_sword_art.item.custom;

import mcp.MethodsReturnNonnullByDefault;
import net.kalandoz.runic_sword_art.utils.RunicUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class FlameSwordItem extends SwordItem {
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
    @MethodsReturnNonnullByDefault
    public ActionResultType onItemUse(ItemUseContext context) {
        
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack stack = context.getItem();

        // retrieves an instance of world from context to later check if world is remote
        // (to prevent code triggering twice)
        // retrieves an instance of playerEntity from context to later apply fire resistance
        // retrieves an instance of itemStack from context to later consume durability
        if(!world.isRemote) {
            if (playerEntity != null) {
                // gives the player fire resistance for 30 seconds
                playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600));
                // activates if the player isn't crouching
                if (!playerEntity.isCrouching()) {
                    // sets nearby ground on fire semi-randomly
                    burnNearbyBlocks(world, playerEntity.getPositionVec(), playerEntity, 4);
                    // damages item (and breaks it if it would break)
                    stack.damageItem(25, playerEntity, player -> player.sendBreakAnimation(playerEntity.getActiveHand()));
                }
            }
        }
        super.onItemUse(context);
        return ActionResultType.SUCCESS;
    }

    private void burnNearbyBlocks(World world, Vector3d origin, @Nullable LivingEntity entity, double radius) {
        // checking if world is remote to prevent effect from triggering twice
        if (!world.isRemote) {

            // stops fire from being set within a 3x3 centered on the player
            for (int i = -(int) radius; i <= (int) radius; i++) {
                for (int j = -(int) radius; j <= (int) radius; j++) {
                    if (i <= 1 && i >= -1 && j <= 1 && j >= -1) {
                        continue;
                    }
                    if (Math.abs(i) + Math.abs(j) == radius * 2) {
                        continue;
                    }

                    BlockPos pos = new BlockPos(origin).add(i, 0, j);

                    Integer y = RunicUtils.getNearestSurface(world, pos, Direction.UP, (int)radius, true, RunicUtils.SurfaceCriteria.NOT_AIR_TO_AIR);

                    if (y != null) {

                        pos = new BlockPos(pos.getX(), y, pos.getZ());

                        double distance = origin.distanceTo(new Vector3d(origin.x + i, y, origin.z + j));

                        // Randomised with weighting so that the nearer the block the more likely it is to be set alight.
                        if (y != -1 && world.rand.nextInt((int) (distance * 2) + 1) < radius && distance < radius && distance > 1.5) {
                            if (entity != null) {
                                BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, pos);
                                world.setBlockState(pos, blockstate, 11);
                            }
                        }
                    }
                }
            }
        }
    }
}