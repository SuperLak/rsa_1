package net.kalandoz.runic_sword_art.client.networking;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.client.networking.packet.CompositeC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.PrimaryC2SPacket;
import net.kalandoz.runic_sword_art.client.networking.packet.SecondaryC2SPacket;
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

        net.messageBuilder(PrimaryC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PrimaryC2SPacket::decode)
                .encoder(PrimaryC2SPacket::encode)
                .consumer(PrimaryC2SPacket::handle)
                .add();

        net.messageBuilder(SecondaryC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SecondaryC2SPacket::decode)
                .encoder(SecondaryC2SPacket::encode)
                .consumer(SecondaryC2SPacket::handle)
                .add();

        net.messageBuilder(CompositeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CompositeC2SPacket::decode)
                .encoder(CompositeC2SPacket::encode)
                .consumer(CompositeC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}