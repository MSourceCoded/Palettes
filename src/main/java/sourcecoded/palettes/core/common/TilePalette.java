package sourcecoded.palettes.core.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TilePalette extends TileEntity {

    public String[] texNames = new String[6];

    public boolean canUpdate() {
        return false;
    }

    public void injectTexture(ForgeDirection dir, String name) {
        texNames[dir.ordinal()] = name;
        update();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            String tex = texNames[dir.ordinal()];
            if (tex != null && !tex.equals(""))
                nbt.setString(dir.name().toLowerCase() + "Tex", tex);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            texNames[dir.ordinal()] = nbt.getString(dir.name().toLowerCase() + "Tex");
        }
    }

    //DERIVE

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
        markDirty();
    }

    public void update() {
        if (worldObj != null) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }
    }
}
