package sourcecoded.palettes.core.client.gui;

import cpw.mods.fml.client.config.GuiSlider;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sourcecoded.palettes.core.client.CachedPalettes;
import sourcecoded.palettes.core.client.CompiledPalette;
import sourcecoded.palettes.core.client.gui.element.GuiRGBSliders;
import sourcecoded.palettes.core.common.SavedPalettes;
import sourcecoded.palettes.lib.ColourUtils;
import sourcecoded.palettes.lib.network.NetworkHandler;
import sourcecoded.palettes.lib.network.message.MessageSaveImage;
import sourcecoded.palettes.shell.Palettes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.lwjgl.opengl.GL11.*;

public class GuiPaletteDraw extends GuiScreen implements GuiSlider.ISlider {

    PaletteBlock[][] blocks = new PaletteBlock[16][16];

    int squareDimension;
    float subsection;

    GuiRGBSliders[] sliders = new GuiRGBSliders[3];

    GuiTextField nameField;
    GuiTextField hexField;

    public static final int SAVE_BUTTON = 1;
    public static final int LOAD_BUTTON = 2;
    public static final int INCREASE_DIMENSION_BUTTON = 3;
    public static final int DECREASE_DIMENSION_BUTTON = 4;
    public static final int FILL_BUTTON = 5;

    int dimension = 16;

    boolean click = false;

    @SuppressWarnings("unchecked")
    public void initGui() {
        int nameWidth = 150;
        nameField = new GuiTextField(this.fontRendererObj, (this.width / 2 - nameWidth / 2), this.height - 22, nameWidth, 18);
        nameField.setText("Untitled Sprite");

        hexField = new GuiTextField(this.fontRendererObj, this.width - 86, this.height / 2 - 40, 77, 15);

        int maxSpace = Math.min(this.height - 22, this.width - 120);
        int padding = 20;
        squareDimension = maxSpace - 2 * padding;

        subsection = squareDimension / dimension;

        sliders[0] = new GuiRGBSliders(100, width - 85, height / 2, 75, 10, 0, this);
        sliders[1] = new GuiRGBSliders(101, width - 85, height / 2 + 15, 75, 10, 1, this);
        sliders[2] = new GuiRGBSliders(102, width - 85, height / 2 + 30, 75, 10, 2, this);

        this.buttonList.add(sliders[0]);
        this.buttonList.add(sliders[1]);
        this.buttonList.add(sliders[2]);

        this.buttonList.add(new GuiButton(SAVE_BUTTON, width / 2 + nameWidth / 2 + 2, height - 23, 50, 20, "Save"));
        this.buttonList.add(new GuiButton(LOAD_BUTTON, width / 2 - nameWidth / 2 - 52, height - 23, 50, 20, "Load"));
        this.buttonList.add(new GuiButton(DECREASE_DIMENSION_BUTTON, width - 87, height / 2 + 45, 15, 20, "<"));
        this.buttonList.add(new GuiButton(INCREASE_DIMENSION_BUTTON, width - 22, height / 2 + 45, 15, 20, ">"));
        this.buttonList.add(new GuiButton(FILL_BUTTON, width - 87, height / 2 + 70, 80, 20, "Fill"));

        click = false;
    }

    public void setColor(float[] rgb) {
        sliders[0].setValue(sliders[0].maxValue * (double) rgb[0]);
        sliders[1].setValue(sliders[1].maxValue * (double) rgb[1]);
        sliders[2].setValue(sliders[2].maxValue * (double) rgb[2]);

        for (int i = 0; i < 3; i++) {
            sliders[i].setValue(sliders[i].maxValue * (double) rgb[i]);
            sliders[i].updateSlider();
        }
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == SAVE_BUTTON) {
            NetworkHandler.wrapper.sendToServer(new MessageSaveImage(convertToImage(), nameField.getText()));
        } else if (button.id == LOAD_BUTTON) {
            this.mc.displayGuiScreen(new GuiPaletteLoad(this));
        } else if (button.id == INCREASE_DIMENSION_BUTTON) {
            int val = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 16 : 1;
            dimension = Math.min(64, dimension + val);
            reloadCanvas();
        } else if (button.id == DECREASE_DIMENSION_BUTTON) {
            int val = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 16 : 1;
            dimension = Math.max(1, dimension - val);
            reloadCanvas();
        } else if (button.id == FILL_BUTTON) {
            for (PaletteBlock[] blockL : blocks) {
                for (PaletteBlock block : blockL)
                    block.fill(ColourUtils.rgbToInt_F((float) sliders[0].sliderValue, (float) sliders[1].sliderValue, (float) sliders[2].sliderValue));
            }
        }
    }

    public void reloadCanvas() {
        PaletteBlock[][] oldBlocks = blocks;
        blocks = new PaletteBlock[dimension][dimension];

        int itMax = Math.min(oldBlocks.length, blocks.length);
        for (int x = 0; x < itMax; x++) {
            for (int y = 0; y < itMax; y++) {
                blocks[x][y] = oldBlocks[x][y];
            }
        }

        subsection = squareDimension / dimension;
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
        if (this.hexField.textboxKeyTyped(letter, keyCode)) {
            updateFromHex();
            return;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.mc.displayGuiScreen(null);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        this.nameField.mouseClicked(mouseX, mouseY, mouseEvent);
        this.hexField.mouseClicked(mouseX, mouseY, mouseEvent);

        super.mouseClicked(mouseX, mouseY, mouseEvent);
    }

    public void drawScreen(int mx, int my, float ptt) {
        this.drawDefaultBackground();
        nameField.drawTextBox();
        hexField.drawTextBox();

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

                block.draw(mx, my, ptt, currentColor, click);
            }
        }
        glEnable(GL_TEXTURE_2D);

        drawCenteredString(fontRendererObj, dimension + "x" + dimension, width - 47, height / 2 + 51, ColourUtils.rgbToInt_F(0.6F, 0.6F, 0.6F));

        super.drawScreen(mx, my, ptt);

        if (!click)
            click = !Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2);
    }

    public BufferedImage convertToImage() {
        BufferedImage image = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                image.setRGB(x, y, blocks[x][y].getColor());
            }
        }
        return image;
    }

    public void loadFromImage(BufferedImage image) {
        if (image != null)
            for (int x = 0; x < blocks.length; x++) {
                for (int y = 0; y < blocks[x].length; y++) {
                    blocks[x][y].updateColor(image.getRGB(x, y));
                }
            }
    }

    @SuppressWarnings("unchecked")
    public void loadItem() throws IOException, IllegalAccessException, InvocationTargetException {
        BufferedImage image = null;
        ItemStack stack = Palettes.proxy.getClientPlayer().getHeldItem();

        if (stack != null) {
            if (stack.getItem() instanceof ItemBlock) {
                Block block = Block.getBlockFromItem(stack.getItem());
                IIcon icon = block.getIcon(0, stack.getItemDamage());
                TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();

                loadItemPost(map, icon);
            } else {
                Item item = stack.getItem();

                IIcon icon = item.getIconFromDamage(stack.getItemDamage());
                TextureMap map = (TextureMap) Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationItemsTexture);
                loadItemPost(map, icon);
            }
        }
    }

    void loadItemPost(TextureMap map, IIcon icon) throws InvocationTargetException, IllegalAccessException, IOException {
        Method convert = ReflectionHelper.findMethod(TextureMap.class, map, new String[] {"completeResourceLocation", "func_147634_a", "a"}, ResourceLocation.class, int.class);

        ResourceLocation finalized = (ResourceLocation) convert.invoke(map, new ResourceLocation(icon.getIconName()), 0);

        IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(finalized);
        loadFromImage(ImageIO.read(resource.getInputStream()));
    }

    void updateHex() {
        int currentInt = ColourUtils.rgbToInt_F((float) sliders[0].sliderValue, (float) sliders[1].sliderValue, (float) sliders[2].sliderValue);
        hexField.setText(Integer.toHexString(currentInt));
    }

    void updateFromHex() {
        try {
            int integer = Integer.parseInt(hexField.getText(), 16);

            float[] rgb = ColourUtils.intToRGB_F(integer);

            sliders[0].setValue(sliders[0].maxValue * (double) rgb[0]);
            sliders[1].setValue(sliders[1].maxValue * (double) rgb[1]);
            sliders[2].setValue(sliders[2].maxValue * (double) rgb[2]);

        } catch (NumberFormatException e) {
        }
    }

    @Override
    public void onChangeSliderValue(GuiSlider slider) {
        updateHex();
    }
}