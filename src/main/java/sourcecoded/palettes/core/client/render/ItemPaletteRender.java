package sourcecoded.palettes.core.client.render;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;
import sourcecoded.palettes.core.common.TilePalette;

public class ItemPaletteRender implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return (helper == ItemRendererHelper.EQUIPPED_BLOCK) || (helper == ItemRendererHelper.INVENTORY_BLOCK && type == ItemRenderType.INVENTORY) || helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.BLOCK_3D;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        TilePalette palette = new TilePalette();
        if (item.stackTagCompound == null)
            item.stackTagCompound = new NBTTagCompound();

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            String tex = item.stackTagCompound.getString(dir.name().toLowerCase() + "Tex");
            palette.injectTexture(dir, tex);
        }

        TileEntityRendererDispatcher.instance.getSpecialRenderer(palette).renderTileEntityAt(palette, 0, 0, 0, 0);
    }

}
