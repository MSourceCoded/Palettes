package sourcecoded.palettes.lib.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import sourcecoded.palettes.core.common.SavedPalettes;

import java.awt.image.BufferedImage;

public class MessageRequestPalette implements IMessage, IMessageHandler<MessageRequestPalette, IMessage> {

    String name;

    public MessageRequestPalette() {}
    public MessageRequestPalette(String name) {
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] nameData = new byte[buf.readShort()];
        buf.readBytes(nameData);
        name = new String(nameData);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(name.getBytes().length);
        buf.writeBytes(name.getBytes());
    }

    @Override
    public IMessage onMessage(MessageRequestPalette message, MessageContext ctx) {
        BufferedImage image = SavedPalettes.loadPalette(message.name);
        if (image != null) {
            return new MessageUpdatePalette(message.name, image);
        }
        return null;
    }
}
