package net.kalandoz.runic_sword_art.client.networking.packet;

import net.kalandoz.runic_sword_art.utils.AttunementUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PrimaryC2SPacket {

    public int key;

    public PrimaryC2SPacket() {

    }

    public PrimaryC2SPacket(int key) {
        this.key = key;
    }

    public static void encode(PrimaryC2SPacket packet, PacketBuffer buffer) {
        buffer.writeInt(packet.key);
    }

    public static PrimaryC2SPacket decode(PacketBuffer buffer) {
        return new PrimaryC2SPacket(buffer.readInt());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            System.out.println("Pre primary message");
            AttunementUtils.primaryAbility(context);
            System.out.println("Post primary message");
        });
        return true;
    }
}