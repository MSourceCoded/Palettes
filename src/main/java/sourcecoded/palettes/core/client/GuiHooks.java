package sourcecoded.palettes.core.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import sourcecoded.palettes.core.client.gui.GuiPaletteDraw;

import static sourcecoded.palettes.lib.client.GuiHookRef.BUTTON_ID;
import static sourcecoded.palettes.lib.client.GuiHookRef.BUTTON_TEXT;

public enum GuiHooks {
    INSTANCE;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void guiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu)
            event.buttonList.add(new GuiButton(BUTTON_ID, event.gui.width - 25, event.gui.height - 25, 20, 20, BUTTON_TEXT));
    }

    @SubscribeEvent
    public void guiAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiIngameMenu)
            if (event.button.id == BUTTON_ID) {
                event.gui.mc.displayGuiScreen(new GuiPaletteDraw());
                event.setCanceled(true);
            }
    }

}
