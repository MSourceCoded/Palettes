package sourcecoded.palettes.core.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sourcecoded.palettes.lib.ColourUtils;

public class GuiPaletteLoad extends GuiScreen {

    GuiPaletteDraw parentGUI;

    public GuiPaletteLoad(GuiPaletteDraw parent) {
        this.parentGUI = parent;
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, 90, 20, "Client"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 10, this.height / 2 + 30, 90, 20, "Server"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 2 + 55, 90, 20, "Held Item"));
    }

    public void keyTyped(char letter, int keyCode) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.mc.displayGuiScreen(parentGUI);
        }
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            parentGUI.loadClient();
        } else if (button.id == 1) {
            parentGUI.loadServer();
        } else if (button.id == 2) {
            try {
                parentGUI.loadItem();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mc.displayGuiScreen(parentGUI);
    }

    public void drawScreen(int mx, int my, float ptt) {
        this.drawDefaultBackground();

        float s = 0.75F;
        float sI = (float) Math.pow(s, -1);

        this.drawCenteredString(fontRendererObj, "Would you like to load from the Server or Client?", this.width / 2, this.height / 2, ColourUtils.rgbToInt_F(1F, 1F, 1F));
        GL11.glTranslatef(this.width / 2, this.height / 2 + 15, 0);
        GL11.glScalef(s, s, s);
        this.drawCenteredString(fontRendererObj, "(Client for loading your own textures, Server for textures already created)", 0, 0, ColourUtils.rgbToInt_F(0.5F, 0.5F, 0.5F));
        GL11.glScalef(sI, sI, sI);
        GL11.glTranslatef(-(this.width / 2), -(this.height / 2 + 15), 0);

        super.drawScreen(mx, my, ptt);
    }
}
