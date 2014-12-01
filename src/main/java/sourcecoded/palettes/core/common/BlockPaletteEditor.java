package sourcecoded.palettes.core.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import sourcecoded.palettes.lib.PalettesConstants;
import sourcecoded.palettes.shell.Palettes;

public class BlockPaletteEditor extends Block {

    public BlockPaletteEditor() {
        super(Material.rock);
        this.setHardness(2F);
        this.setBlockName("paletteEditor");
        this.setCreativeTab(PaletteTabs.paletteMain);
        this.setBlockTextureName(PalettesConstants.MODID + ":editor");
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float xo, float yo, float zo) {
        if (world.isRemote) {
            Palettes.proxy.displayGUI(0, player);
        }

        return true;
    }
}
