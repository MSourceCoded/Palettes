package sourcecoded.palettes.core.common;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sourcecoded.palettes.lib.PalettesConstants;

import java.util.Random;

public class BlockPalette extends Block implements ITileEntityProvider {

    public BlockPalette() {
        super(Material.rock);
        this.setHardness(2F);
        this.setBlockName("palette");
        this.setCreativeTab(PaletteTabs.paletteMain);
        this.setBlockTextureName(PalettesConstants.MODID + ":base");
    }

    public int getRenderType() {
        return -1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePalette();
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
        TilePalette tile = (TilePalette) world.getTileEntity(x, y, z);

        if (item.stackTagCompound == null)
            item.stackTagCompound = new NBTTagCompound();

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            String tex = item.stackTagCompound.getString(dir.name().toLowerCase() + "Tex");
            tile.injectTexture(dir, tex);
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        ItemStack stack = getDropStack(world, x, y, z);

        EntityItem entityitem = new EntityItem(world, (double)x, (double)y + 0.5D, (double)z, stack);
        entityitem.delayBeforeCanPickup = 0;
        world.spawnEntityInWorld(entityitem);

        super.breakBlock(world, x, y, z, block, meta);
    }

    public ItemStack getDropStack(World world, int x, int y, int z) {
        TilePalette tile = (TilePalette) world.getTileEntity(x, y, z);
        ItemStack stack = new ItemStack(this);
        stack.stackTagCompound = new NBTTagCompound();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            String tex = tile.texNames[dir.ordinal()];
            if (tex != null)
                stack.stackTagCompound.setString(dir.name().toLowerCase() + "Tex", tex);
        }
        return stack;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return getDropStack(world, x, y, z);
    }

    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }
}