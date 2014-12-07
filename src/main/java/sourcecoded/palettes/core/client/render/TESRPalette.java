package sourcecoded.palettes.core.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
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

        boolean disableLight = te == null || te.getWorldObj() == null;
        RenderHelper.disableStandardItemLighting();

        int l = 240;

        if (!disableLight) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            if (Minecraft.isAmbientOcclusionEnabled())
                GL11.glShadeModel(GL11.GL_SMOOTH);
            else
                GL11.glShadeModel(GL11.GL_FLAT);

            l = te.getWorldObj().getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0);
            int i1 = l % 65536;
            int j1 = l / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
        }

        if (te != null && te.texNames != null) {
            for (int i = 0; i < 6; i ++) {
                CompiledPalette palette = CachedPalettes.getPaletteForName(te.texNames[i]);

                ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];

                if (palette != null) {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, palette.getGL());
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    draw(x, y, z, tess, ForgeDirection.VALID_DIRECTIONS[i], l);
                } else {
                    this.bindTexture(baseLocation);
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    draw(x, y, z, tess, ForgeDirection.VALID_DIRECTIONS[i], l);
                }
            }
        } else if (te != null) {
            for (int i = 0; i < 6; i++) {
                this.bindTexture(baseLocation);
                GL11.glColor4f(1F, 1F, 1F, 1F);
                draw(x, y, z, tess, ForgeDirection.VALID_DIRECTIONS[i], l);
            }
        }
        bindTexture(TextureMap.locationBlocksTexture);

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    void draw(double x, double y, double z, Tessellator tess, ForgeDirection direction, int b) {
        tess.startDrawingQuads();
        tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        TessUtils.drawFace(direction, tess, x, y, z, x + 1, y + 1, z + 1, 0, 0, 1, 1);
        tess.draw();
    }

}
