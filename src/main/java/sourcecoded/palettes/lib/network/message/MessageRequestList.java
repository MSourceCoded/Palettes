package sourcecoded.palettes.lib.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import sourcecoded.palettes.core.common.SavedPalettes;

import java.awt.image.BufferedImage;

public class MessageRequestList implements IMessage, IMessageHandler<MessageRequestList, IMessage> {

    public MessageRequestList() {}

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(MessageRequestList message, MessageContext ctx) {
        return new MessageUpdateList(SavedPalettes.getPalettes());
    }
}
