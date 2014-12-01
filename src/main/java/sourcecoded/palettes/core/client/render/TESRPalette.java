package sourcecoded.palettes.core.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import sourcecoded.palettes.core.client.CachedPalettes;
import sourcecoded.palettes.core.client.CompiledPalette;
import sourcecoded.palettes.core.common.TilePalette;
import sourcecoded.palettes.lib.PalettesConstants;

public class TESRPalette extends TileEntitySpecialRenderer {

    ResourceLocation baseLocation = new ResourceLocation(PalettesConstants.MODID, "textures/blocks/base.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float ptt) {
        TilePalette te = (TilePalette) tile;
        GL11.glPushMatrix();
        Tessellator tess = Tessellator.instance;
        GL11.glDisable(GL11.GL_LIGHTING);
        if (te != null && te.texNames != null) {
            for (int i = 0; i < 6; i ++) {
                CompiledPalette palette = CachedPalettes.getPaletteForName(te.texNames[i]);

                if (palette != null) {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, palette.getGL());
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    draw(x, y, z, tess, ForgeDirection.VALID_DIRECTIONS[i]);
                } else {
                    this.bindTexture(baseLocation);
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    draw(x, y, z, tess, ForgeDirection.VALID_DIRECTIONS[i]);
                }
            }
        } else if (te != null) {
            for (int i = 0; i < 6; i++) {
                this.bindTexture(baseLocation);
                GL11.glColor3f(1F, 1F, 1F);
                draw(x, y, z, tess, ForgeDirection.VALID_DIRECTIONS[i]);
            }
        }
        bindTexture(TextureMap.locationBlocksTexture);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    void draw(double x, double y, double z, Tessellator tess, ForgeDirection direction) {
        tess.startDrawingQuads();
        tess.setBrightness(240);
        //TessUtils.drawCube(tess, x, y, z, 1D, 0, 0, 1, 1);
        TessUtils.drawFace(direction, tess, x, y, z, x + 1, y + 1, z + 1, 0, 0, 1, 1);
        tess.draw();
    }

}
