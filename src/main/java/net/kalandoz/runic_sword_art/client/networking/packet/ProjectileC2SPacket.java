package net.kalandoz.runic_sword_art.client.networking.packet;

import net.kalandoz.runic_sword_art.item.ModItems;
import net.kalandoz.runic_sword_art.world.entity.ModEntityType;
import net.kalandoz.runic_sword_art.world.entity.projectile.FlameArcEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class ProjectileC2SPacket {

    Random rand = new Random();

    public int key;

    public ProjectileC2SPacket() {

    }

    public ProjectileC2SPacket(int key) {
        this.key = key;
    }

    public static void encode(ProjectileC2SPacket packet, PacketBuffer buffer) {
        buffer.writeInt(packet.key);
    }

    public static ProjectileC2SPacket decode(PacketBuffer buffer) {
        return new ProjectileC2SPacket(buffer.readInt());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayerEntity playerIn = context.getSender();
            ServerWorld worldIn = Objects.requireNonNull(context.getSender()).getServerWorld();
            if (playerIn != null) {
                if (playerIn.getHeldItemMainhand().getItem() == ModItems.FLAME_SWORD.get()) {
                    // sending confirmation message
                    System.out.println("Activating Projectile Key!");
                    // test
                    boolean flag = playerIn.abilities.isCreativeMode;
                    // f = velocity
                    float f = 1.0f;
                    // if (!((double)f < 0.1D)) {
                    if (!worldIn.isRemote) {
                        Vector3d vec = playerIn.getPositionVec();
                        FlameArcEntity arc = new FlameArcEntity(worldIn, playerIn, 0, 0, 0);
                        arc.setFire(100);
                        arc.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        // Cannot be picked up due to lack of actual item ;)
                        // This adds entity (VERY IMPORTANT)
                        worldIn.addEntity(arc);
                        // Plays Shoot Bow Sound when fired
                        worldIn.playSound((PlayerEntity) playerIn, playerIn.getPosX(), playerIn.getPosY(),
                                playerIn.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT,
                                SoundCategory.PLAYERS, 1.0F,
                                1.0F / (rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    }
                }
            }
        });
        return true;
    }
}
