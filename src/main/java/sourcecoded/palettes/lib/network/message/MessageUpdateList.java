package sourcecoded.palettes.lib.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import sourcecoded.palettes.core.client.CachedPalettes;

import java.util.ArrayList;
import java.util.List;

public class MessageUpdateList implements IMessage, IMessageHandler<MessageUpdateList, IMessage> {

    List<String> theList;

    public MessageUpdateList() {
        theList = new ArrayList<String>();
    }
    public MessageUpdateList(List<String> list) {
        theList = list;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readShort();
        for (int i = 0; i < length; i++) {
            byte[] data = new byte[buf.readShort()];
            buf.readBytes(data);
            theList.add(new String(data));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(theList.size());
        for (String s : theList) {
            buf.writeShort(s.getBytes().length);
            buf.writeBytes(s.getBytes());
        }
    }

    @Override
    public IMessage onMessage(MessageUpdateList message, MessageContext ctx) {
        CachedPalettes.validList = message.theList;
        return null;
    }
}
