package sourcecoded.palettes.core.client.gui;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sourcecoded.palettes.lib.ColourUtils;

public class PaletteBlock {

    private int pixelData = 0;
    private int x = 0;
    private int y = 0;

    private GuiPaletteDraw parent;

    public PaletteBlock(int x, int y, GuiPaletteDraw parent) {
        this.x = x;
        this.y = y;

        this.parent = parent;
    }

    public void updateColor(int newColor) {
        this.pixelData = newColor;
    }

    public int getColor() {
        return pixelData;
    }

    public void draw(int mx, int my, float ptt, float[] color) {
        float[] rgb = ColourUtils.intToRGB_F(pixelData);

        float xStart = parent.width / 2 + (parent.subsection * x) - (parent.squareDimension / 2);
        float yStart = parent.height / 2 + (parent.subsection * y) - (parent.squareDimension / 2);

        boolean changed = false;

        if (mx > xStart && mx < xStart + parent.subsection && my > yStart && my < yStart + parent.subsection) {
            if (Mouse.isButtonDown(0)) {
                rgb = color;
                changed = true;
            } else if (Mouse.isButtonDown(2)) {
                parent.setColor(rgb);
            } else {
                rgb[0] = (float) Math.min(1F, rgb[0] + 0.3);
                rgb[1] = (float) Math.min(1F, rgb[1] + 0.3);
                rgb[2] = (float) Math.min(1F, rgb[2] + 0.3);
            }
        }

        GL11.glColor3f(rgb[0], rgb[1], rgb[2]);

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();

        tess.addVertex(xStart, yStart, 0);
        tess.addVertex(xStart, yStart + parent.subsection, 0);
        tess.addVertex(xStart + parent.subsection, yStart + parent.subsection, 0);
        tess.addVertex(xStart + parent.subsection, yStart, 0);

        tess.draw();

        if (changed)
            pixelData = ColourUtils.rgbToInt_F(rgb[0], rgb[1], rgb[2]);
    }

}
