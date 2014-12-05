package sourcecoded.palettes.core.client.gui.element;

import cpw.mods.fml.client.config.GuiSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class GuiRGBSliders extends GuiSlider {

    int type = 0;

    public GuiRGBSliders(int id, int xPos, int yPos, int width, int height, int type, ISlider parent) {
        super(id, xPos, yPos, width, height, "", "", 0, 256 * 6 - 5, 0, false, false, parent);
        this.type = type;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);

            float r = 0;
            float g = 0;
            float b = 0;

            if (type == 0)
                r = (float) sliderValue;
            else if (type == 1)
                g = (float) sliderValue;
            else if (type == 2)
                b = (float) sliderValue;

            GL11.glColor3f(r, g, b);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            tess.addVertex(this.xPosition - 2, this.yPosition, 0);
            tess.addVertex(this.xPosition - 2, this.yPosition + height, 0);
            tess.addVertex(this.xPosition + width + 2, this.yPosition + height, 0);
            tess.addVertex(this.xPosition + width + 2, this.yPosition, 0);
            tess.draw();

            this.mouseDragged(mc, mouseX, mouseY);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (par2 - (this.xPosition + 4)) / (float)(this.width - 8);
                updateSlider();
            }

            GL11.glColor4f(0.25F, 0.25F, 0.25F, 1F);

            double posX = this.xPosition + (this.sliderValue * width);
            double posY = this.yPosition;

            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            tess.addVertex(posX - 2, posY, 0);
            tess.addVertex(posX - 2, posY + height, 0);
            tess.addVertex(posX + 2, posY + height, 0);
            tess.addVertex(posX + 2, posY, 0);
            tess.draw();
        }
    }

}
