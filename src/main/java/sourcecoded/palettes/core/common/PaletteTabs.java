package sourcecoded.palettes.core.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class PaletteTabs {

    public static CreativeTabs paletteMain = new CreativeTabs("Palettes") {
        @Override
        public Item getTabIconItem() {
            return Items.painting;
        }
    };

}
