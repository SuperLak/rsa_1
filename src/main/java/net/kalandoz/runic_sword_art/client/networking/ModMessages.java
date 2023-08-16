package net.kalandoz.runic_sword_art.client.networking;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.client.networking.packet.BurstC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.ProjectileC2SPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static final String NETWORK_VERSION = "0.1.0";
    private static int packetId;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RunicSwordArt.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ProjectileC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ProjectileC2SPacket::decode)
                .encoder(ProjectileC2SPacket::encode)
                .consumer(ProjectileC2SPacket::handle)
                .add();

        net.messageBuilder(BurstC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BurstC2SPacket::decode)
                .encoder(BurstC2SPacket::encode)
                .consumer(BurstC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}