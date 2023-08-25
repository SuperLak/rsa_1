package net.kalandoz.runic_sword_art.client.networking.packet;

import net.kalandoz.runic_sword_art.utils.ManaUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaC2SPacket {

    public int key;

    public ManaC2SPacket() {

    }

    public ManaC2SPacket(int key) {
        this.key = key;
    }

    public static void encode(ManaC2SPacket packet, PacketBuffer buffer) {
        buffer.writeInt(packet.key);
    }

    public static ManaC2SPacket decode(PacketBuffer buffer) {
        return new ManaC2SPacket(buffer.readInt());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayerEntity playerIn = context.getSender();
            if (playerIn != null) {
                System.out.println("Pre mana message");
                ManaUtils.printTotal(null, playerIn);
                ManaUtils.fillMana(null, playerIn, 20);
                System.out.println("Post mana message");
            }
        });
        return true;
    }
}