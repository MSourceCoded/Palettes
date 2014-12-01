package sourcecoded.palettes.core.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import sourcecoded.palettes.core.client.CachedPalettes;
import sourcecoded.palettes.core.client.CompiledPalette;
import sourcecoded.palettes.core.client.gui.element.GuiRGBSliders;
import sourcecoded.palettes.core.common.SavedPalettes;
import sourcecoded.palettes.lib.network.NetworkHandler;
import sourcecoded.palettes.lib.network.message.MessageSaveImage;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class GuiPaletteDraw extends GuiScreen {

    PaletteBlock[][] blocks = new PaletteBlock[16][16];

    int squareDimension;
    float subsection;

    GuiRGBSliders[] sliders = new GuiRGBSliders[3];

    GuiTextField nameField;

    public static final int SAVE_BUTTON = 1;
    public static final int LOAD_BUTTON = 2;

    @SuppressWarnings("unchecked")
    public void initGui() {
        int nameWidth = 150;
        nameField = new GuiTextField(this.fontRendererObj, (this.width / 2 - nameWidth / 2), this.height - 22, nameWidth, 18);
        nameField.setText("Untitled Sprite");

        int maxSpace = Math.min(this.height - 22, this.width - 120);
        int padding = 20;
        squareDimension = maxSpace - 2 * padding;

        subsection = squareDimension / 16;

        sliders[0] = new GuiRGBSliders(100, width - 85, height / 2, 75, 10, 0);
        sliders[1] = new GuiRGBSliders(101, width - 85, height / 2 + 15, 75, 10, 1);
        sliders[2] = new GuiRGBSliders(102, width - 85, height / 2 + 30, 75, 10, 2);

        this.buttonList.add(sliders[0]);
        this.buttonList.add(sliders[1]);
        this.buttonList.add(sliders[2]);

        this.buttonList.add(new GuiButton(SAVE_BUTTON, width / 2 + nameWidth / 2 + 2, height - 23, 50, 20, "Save"));
        this.buttonList.add(new GuiButton(LOAD_BUTTON, width / 2 - nameWidth / 2 - 52, height - 23, 50, 20, "Load"));
    }

    public void setColor(float[] rgb) {
        sliders[0].setValue(sliders[0].maxValue * (double)rgb[0]);
        sliders[1].setValue(sliders[0].maxValue * (double)rgb[1]);
        sliders[2].setValue(sliders[0].maxValue * (double)rgb[2]);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == SAVE_BUTTON) {
            NetworkHandler.wrapper.sendToServer(new MessageSaveImage(convertToImage(), nameField.getText()));
        } else if (button.id == LOAD_BUTTON) {
            this.mc.displayGuiScreen(new GuiPaletteLoad(this));
        }
    }

    public void loadServer() {
        CompiledPalette palette = CachedPalettes.getPaletteForName(nameField.getText());
        if (palette != null)
            loadFromImage(palette.getImage());
    }

    public void loadClient() {
        BufferedImage image = SavedPalettes.loadPalette(nameField.getText());
        loadFromImage(image);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void keyTyped(char letter, int keyCode) {
        if (this.nameField.textboxKeyTyped(letter, keyCode)) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.mc.displayGuiScreen(null);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        this.nameField.mouseClicked(mouseX, mouseY, mouseEvent);

        super.mouseClicked(mouseX, mouseY, mouseEvent);
    }

    public void drawScreen(int mx, int my, float ptt) {
        this.drawDefaultBackground();
        nameField.drawTextBox();

        float[] currentColor = new float[3];
        currentColor[0] = (float) sliders[0].sliderValue;
        currentColor[1] = (float) sliders[1].sliderValue;
        currentColor[2] = (float) sliders[2].sliderValue;

        glDisable(GL_TEXTURE_2D);

        glColor3f(currentColor[0], currentColor[1], currentColor[2]);
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertex(width - 87, height / 2 - 10, 0);
        tess.addVertex(width - 8, height / 2 - 10, 0);
        tess.addVertex(width - 8, height / 2 - 20, 0);
        tess.addVertex(width - 87, height / 2 - 20, 0);
        tess.draw();

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks.length; y++) {
                if (blocks[x][y] == null)
                    blocks[x][y] = new PaletteBlock(x, y, this);
                PaletteBlock block = blocks[x][y];

                block.draw(mx, my, ptt, currentColor);
            }
        }

        glEnable(GL_TEXTURE_2D);

        super.drawScreen(mx, my, ptt);
    }

    public BufferedImage convertToImage() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                image.setRGB(x, y, blocks[x][y].getColor());
            }
        }
        return image;
    }

    public void loadFromImage(BufferedImage image) {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                blocks[x][y].updateColor(image.getRGB(x, y));
            }
        }
    }
}