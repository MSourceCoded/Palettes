package sourcecoded.palettes.lib.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import sourcecoded.palettes.lib.network.message.*;

import java.io.IOException;

public class NetworkHandler {

    public static SimpleNetworkWrapper wrapper;

    public static void initNetwork() {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("sc|palettes");

        /* Sending to server */
        wrapper.registerMessage(MessageSaveImage.class, MessageSaveImage.class, 0, Side.SERVER);
        wrapper.registerMessage(MessageRequestPalette.class, MessageRequestPalette.class, 1, Side.SERVER);
        wrapper.registerMessage(MessageRequestList.class, MessageRequestList.class, 2, Side.SERVER);
        wrapper.registerMessage(MessageWritePalette.class, MessageWritePalette.class, 3, Side.SERVER);

        /* Sending to clients */
        wrapper.registerMessage(MessageUpdatePalette.class, MessageUpdatePalette.class, 10, Side.CLIENT);
        wrapper.registerMessage(MessageUpdateList.class, MessageUpdateList.class, 11, Side.CLIENT);
    }

    public static void writeNBT(ByteBuf target, NBTTagCompound tag) throws IOException {
        if (tag == null)
            target.writeShort(-1);
        else{
            byte[] abyte = CompressedStreamTools.compress(tag);
            target.writeShort((short)abyte.length);
            target.writeBytes(abyte);
        }
    }

    public static NBTTagCompound readNBT(ByteBuf dat) throws IOException {
        short short1 = dat.readShort();

        if (short1 < 0)
            return null;
        else {
            byte[] abyte = new byte[short1];
            dat.readBytes(abyte);
            //return CompressedStreamTools.decompress(abyte);
            return CompressedStreamTools.func_152457_a(abyte, NBTSizeTracker.field_152451_a);
        }
    }

}
