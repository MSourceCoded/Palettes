package sourcecoded.palettes.shell.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IProxy {

    @Override
    public void initRenderers() {

    }

    @Override
    public void initEventHook() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return null;
    }

    @Override
    public void displayGUI(int ID, EntityPlayer player) {
    }

    void registerEventHookForge(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    void registerEventHookFML(Object obj) {
        FMLCommonHandler.instance().bus().register(obj);
    }

}
