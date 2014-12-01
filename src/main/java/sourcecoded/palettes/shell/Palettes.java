package sourcecoded.palettes.shell;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import sourcecoded.palettes.core.common.BlockPalette;
import sourcecoded.palettes.core.common.BlockPaletteEditor;
import sourcecoded.palettes.core.common.TilePalette;
import sourcecoded.palettes.lib.PaletteFileHandler;
import sourcecoded.palettes.lib.network.NetworkHandler;
import sourcecoded.palettes.shell.proxy.IProxy;

import static sourcecoded.palettes.lib.PalettesConstants.*;

@Mod(modid = MODID, version = VERSION, name = NAME, dependencies = "required-after:sourcecodedcore")
public class Palettes {

    @SidedProxy(serverSide = COMMON_PROXY, clientSide = CLIENT_PROXY)
    public static IProxy proxy;

    public static BlockPalette palette;
    public static BlockPaletteEditor paletteEditor;

    @Mod.Instance(MODID)
    public static Palettes instance;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        palette = new BlockPalette();
        paletteEditor = new BlockPaletteEditor();
        GameRegistry.registerBlock(palette, "palette");
        GameRegistry.registerBlock(paletteEditor, "paletteEditor");
        GameRegistry.registerTileEntity(TilePalette.class, "palette");

        GameRegistry.addShapedRecipe(new ItemStack(palette, 8), "sws", "wpw", "sws", 's', Items.stick, 'w', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 'p', Items.painting);

        proxy.initRenderers();
        proxy.initEventHook();

        PaletteFileHandler.init();
        NetworkHandler.initNetwork();
    }

}
