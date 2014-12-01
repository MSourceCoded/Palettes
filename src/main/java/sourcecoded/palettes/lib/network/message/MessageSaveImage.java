package sourcecoded.palettes.lib.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import sourcecoded.palettes.core.common.SavedPalettes;
import sourcecoded.palettes.lib.network.NetworkHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessageSaveImage implements IMessage, IMessageHandler<MessageSaveImage, IMessage> {

    BufferedImage image;
    String name;

    public MessageSaveImage() {}
    public MessageSaveImage(BufferedImage image, String name) {
        this.image = image;
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
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

        byte[] nameData = new byte[buf.readShort()];
        buf.readBytes(nameData);

        name = new String(nameData);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "PNG", byteArray);
            byte[] data = byteArray.toByteArray();
            buf.writeShort(image.getWidth());
            buf.writeShort(image.getHeight());

            buf.writeShort(data.length);
            buf.writeBytes(data);

            buf.writeShort(name.getBytes().length);
            buf.writeBytes(name.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IMessage onMessage(MessageSaveImage message, MessageContext ctx) {
        SavedPalettes.savePalette(message.name, message.image);
        NetworkHandler.wrapper.sendToAll(new MessageUpdatePalette(message.name, message.image));
        return null;
    }
}
