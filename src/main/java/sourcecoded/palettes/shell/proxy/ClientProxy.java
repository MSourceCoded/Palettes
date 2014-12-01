package sourcecoded.palettes.shell.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import sourcecoded.palettes.core.client.GuiHooks;
import sourcecoded.palettes.core.client.gui.GuiPaletteMap;
import sourcecoded.palettes.core.client.render.ItemPaletteRender;
import sourcecoded.palettes.core.client.render.TESRPalette;
import sourcecoded.palettes.core.common.TilePalette;
import sourcecoded.palettes.shell.Palettes;

public class ClientProxy extends CommonProxy {

    @Override
    public void initRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TilePalette.class, new TESRPalette());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Palettes.palette), new ItemPaletteRender());
    }

    @Override
    public void initEventHook() {
        registerEventHookForge(GuiHooks.INSTANCE);
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void displayGUI(int ID, EntityPlayer player) {
        if (ID == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiPaletteMap());
        }
    }

}
