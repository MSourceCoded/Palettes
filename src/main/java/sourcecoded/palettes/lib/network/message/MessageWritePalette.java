package sourcecoded.palettes.lib.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import sourcecoded.palettes.core.common.SavedPalettes;
import sourcecoded.palettes.lib.network.NetworkHandler;

import java.io.IOException;

public class MessageWritePalette implements IMessage, IMessageHandler<MessageWritePalette, IMessage> {

    NBTTagCompound compound;

    public MessageWritePalette() {}
    public MessageWritePalette(NBTTagCompound compound) {
        this.compound = compound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            compound = NetworkHandler.readNBT(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            NetworkHandler.writeNBT(buf, compound);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IMessage onMessage(MessageWritePalette message, MessageContext ctx) {
        ctx.getServerHandler().playerEntity.getCurrentEquippedItem().stackTagCompound = message.compound;
        return null;
    }
}
