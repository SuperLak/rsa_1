package net.kalandoz.runic_sword_art.client.networking.packet;

import net.kalandoz.runic_sword_art.item.ModItems;
import net.kalandoz.runic_sword_art.utils.AoEUtils;
import net.kalandoz.runic_sword_art.utils.ManaUtils;
import net.kalandoz.runic_sword_art.utils.RunicUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class BurstC2SPacket {

    public int key;

    public BurstC2SPacket() {

    }

    public BurstC2SPacket(int key) {
        this.key = key;
    }

    public static void encode(BurstC2SPacket packet, PacketBuffer buffer) {
        buffer.writeInt(packet.key);
    }

    public static BurstC2SPacket decode(PacketBuffer buffer) {
        return new BurstC2SPacket(buffer.readInt());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayerEntity playerIn = context.getSender();
            ServerWorld worldIn = Objects.requireNonNull(context.getSender()).getServerWorld();
            if (playerIn != null) {
                if (playerIn.getHeldItemMainhand().getItem() == ModItems.FLAME_SWORD.get()) {
                    // only using power if player has enough mana
                    if (ManaUtils.consumeMana(null, playerIn, 40)) {
                        // lighting nearby enemies on fire
                        AoEUtils.applyFlameBurstToNearbyEnemies(playerIn, 5);
                        // spawning particles for flair
                        spawnParticles(playerIn);
                    }
                }
            }
        });
        return true;
    }

    private void burnNearbyBlocks(World world, Vector3d origin, double radius) {
        /**
         * Sets nearby surfaces on fire semi-randomly
         */
        // checking if world is remote to prevent effect from triggering twice
        if (!world.isRemote) {
            // causes all blocks on the x-axis to be considered as potential targets for fire
            for (int i = -(int) radius; i <= (int) radius; i++) {
                // causes all blocks on the z-axis to be considered as potential targets for fire
                for (int j = -(int) radius; j <= (int) radius; j++) {
                    /* stops fire from being set within a 3x3 centered on the player
                    if (i <= 1 && i >= -1 && j <= 1 && j >= -1) {
                        continue;
                    }
                     */
                    // creates an instance of BlockPos which will be used to
                    // calculate which surface is closest to the player
                    BlockPos pos = new BlockPos(origin).add(i, 0, j);
                    // creates an integer that holds the y-value of the surface closest to the player whilst still
                    // along the x-axis & z-axis according to lines 75 & 77
                    Integer y = RunicUtils.getNearestSurface(world, pos, Direction.UP, (int)radius, true, RunicUtils.SurfaceCriteria.NOT_AIR_TO_AIR);
                    // only runs the code if the previous line did not return null
                    // (meaning no valid surface was found within the predetermined radius)
                    if (y != null) {
                        // changes the instance of BlockPos to the nearest valid surface
                        pos = new BlockPos(pos.getX(), y, pos.getZ());
                        // creates a double to use in the following calculations to semi-randomize the placement of fire
                        double distance = origin.distanceTo(new Vector3d(origin.x + i, y, origin.z + j));

                        // Randomised with weighting so that the nearer the block the more likely it is to be set alight.
                        if (y != -1 && world.rand.nextInt((int) (distance * 2) + 1) < radius && distance < radius && distance > 1.5) {
                            // configures fire block (mandatory)
                            BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, pos);
                            // sets valid surface on fire
                            world.setBlockState(pos, blockstate, 11);
                        }
                    }
                }
            }
        }
    }

    private void spawnParticles(PlayerEntity player) {
        /**
         * Adds 18 flame particles in a wave-like motion
         */
        // creates an instance of BlockPos to later add the particles
        BlockPos pos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
        // displays the position of the player
        System.out.println("Player Position: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
        // causes each degree(360) to be determined for the adding of a particle
        for(int i = 0; i < 360; i++) {
            // only adds a particle every 20 degrees (total of 18 times)
            if(i % 10 == 0) {
                // adds the particle
                Minecraft.getInstance().particles.addParticle(ParticleTypes.FLAME,
                        player.getPosX(), player.getPosY(), player.getPosZ(),
                        Math.cos(i) * 0.25d, 0.15d, Math.sin(i) * 0.25d);
            }
        }
    }
}