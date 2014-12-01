package sourcecoded.palettes.shell.proxy;

import net.minecraft.entity.player.EntityPlayer;

public interface IProxy {

    public void initRenderers();

    public void initEventHook();

    public EntityPlayer getClientPlayer();

    public void displayGUI(int ID, EntityPlayer player);
}
