package net.kalandoz.runic_sword_art.client.networking.packet;

import net.kalandoz.runic_sword_art.item.ModItems;
import net.kalandoz.runic_sword_art.utils.RunicUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
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
            ServerPlayerEntity player = context.getSender();
            ServerWorld worldIn = Objects.requireNonNull(context.getSender()).getServerWorld();
            if (player != null) {
                System.out.println("Player is valid!");
                boolean p = player.getHeldItemMainhand().getItem() == ModItems.FLAME_SWORD.get();
                System.out.println("Item is valid: " + p);
                if (player.getHeldItemMainhand().getItem() == ModItems.FLAME_SWORD.get()) {
                    // sending confirmation message
                    System.out.println("Activating Burst Key!");
                    // activating flame burst
                    burnNearbyBlocks(worldIn, player.getPositionVec(), player, 4);
                }
            }
        });
        return true;
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