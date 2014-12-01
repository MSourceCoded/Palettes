package sourcecoded.palettes.core.client;

import net.minecraft.client.renderer.texture.DynamicTexture;

import java.awt.image.BufferedImage;

public class CompiledPalette {

    private String name;
    private BufferedImage image;
    private int texID;
    private DynamicTexture texture;

    public CompiledPalette(String name, BufferedImage image) {
        this.name = name;
        this.image = image;

        texture = new DynamicTexture(image);
        texID = texture.getGlTextureId();
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getGL() {
        return texID;
    }

    public void dispose() {
        texture.deleteGlTexture();
    }

}
