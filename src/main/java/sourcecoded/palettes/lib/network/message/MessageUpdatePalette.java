package sourcecoded.palettes.lib.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import sourcecoded.palettes.core.client.CachedPalettes;
import sourcecoded.palettes.core.client.CompiledPalette;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessageUpdatePalette implements IMessage, IMessageHandler<MessageUpdatePalette, IMessage> {

    String name;
    BufferedImage image;

    public MessageUpdatePalette() {}
    public MessageUpdatePalette(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] nameData = new byte[buf.readShort()];
        buf.readBytes(nameData);
        name = new String(nameData);

        int width = buf.readShort();
        int height = buf.readShort();

        byte[] data = new byte[buf.readShort()];
        buf.readBytes(data);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(name.getBytes().length);
        buf.writeBytes(name.getBytes());

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "PNG", byteArray);
            byte[] data = byteArray.toByteArray();
            buf.writeShort(image.getWidth());
            buf.writeShort(image.getHeight());

            buf.writeShort(data.length);
            buf.writeBytes(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IMessage onMessage(MessageUpdatePalette message, MessageContext ctx) {
        CachedPalettes.putPalette(new CompiledPalette(message.name, message.image));
        return null;
    }
}
