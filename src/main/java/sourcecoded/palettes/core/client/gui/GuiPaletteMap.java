package sourcecoded.palettes.core.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;
import sourcecoded.palettes.core.client.CachedPalettes;
import sourcecoded.palettes.lib.ColourUtils;
import sourcecoded.palettes.lib.network.NetworkHandler;
import sourcecoded.palettes.lib.network.message.MessageRequestList;
import sourcecoded.palettes.lib.network.message.MessageWritePalette;
import sourcecoded.palettes.shell.Palettes;

public class GuiPaletteMap extends GuiScreen {

    GuiTextField[] sides;

    boolean canContinue = false;
    String error = "";

    public void initGui() {
        sides = new GuiTextField[6];
        for (int i = 0; i < sides.length; i++) {
            sides[i] = new GuiTextField(fontRendererObj, this.width / 2 - 50, this.height / 2 - 50 + (20 * i), 100, 15);
        }

        NetworkHandler.wrapper.sendToServer(new MessageRequestList());

        this.buttonList.add(new GuiButton(0, this.width / 2 - 60, this.height / 2 + 75, 120, 20, "Apply to current Item"));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void keyTyped(char letter, int keyCode) {
        for (GuiTextField field : sides) {
            if (field.textboxKeyTyped(letter, keyCode)) return;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.mc.displayGuiScreen(null);
        }
    }

    protected void actionPerformed(GuiButton button) {
        ItemStack currentEquip = Palettes.proxy.getClientPlayer().getCurrentEquippedItem();
        if (button.id == 0) {
            if (canContinue) {
                if (currentEquip != null && Block.getBlockFromItem(currentEquip.getItem()) == Palettes.palette) {
                    NBTTagCompound compound = new NBTTagCompound();
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        compound.setString(dir.name().toLowerCase() + "Tex", sides[dir.ordinal()].getText());
                    }

                    NetworkHandler.wrapper.sendToServer(new MessageWritePalette(compound));

                    error = "";
                } else {
                    error = "Invalid Item in Hand";
                }
            } else {
                error = "Invalid Palette Names";
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        for (GuiTextField field : sides) {
            field.mouseClicked(mouseX, mouseY, mouseEvent);
        }

        super.mouseClicked(mouseX, mouseY, mouseEvent);
    }

    public void drawScreen(int mx, int my, float ptt) {
        super.drawDefaultBackground();
        canContinue = true;

        for (int i = 0; i < 6; i++) {
            sides[i].drawTextBox();
            int color = ColourUtils.rgbToInt_F(0.5F, 1F, 0.5F);
            if (!sides[i].getText().equals("") && !CachedPalettes.validList.contains(sides[i].getText())) {
                color = ColourUtils.rgbToInt_F(1F, 0.5F, 0.5F);
                canContinue = false;
            }

            this.drawString(fontRendererObj, ForgeDirection.VALID_DIRECTIONS[i].name(), this.width / 2 + 55, sides[i].yPosition + 4, color);
        }

        this.drawCenteredString(fontRendererObj, error, this.width / 2, this.height / 2 + 100, ColourUtils.rgbToInt_F(1F, 0.5F, 0.5F));

        super.drawScreen(mx, my, ptt);
    }


}
