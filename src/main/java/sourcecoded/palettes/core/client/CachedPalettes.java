package sourcecoded.palettes.core.client;

import sourcecoded.palettes.core.common.TilePalette;
import sourcecoded.palettes.lib.network.NetworkHandler;
import sourcecoded.palettes.lib.network.message.MessageRequestPalette;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CachedPalettes {

    public static ArrayList<CompiledPalette> palettesList = new ArrayList<CompiledPalette>();
    private static ArrayList<String> requests = new ArrayList<String>();
    public static List<String> validList = new ArrayList<String>();

    public static CompiledPalette getPaletteForName(String name) {
        if (name == null) return null;

        for (CompiledPalette palette : palettesList) {
            if (palette.getName().equals(name))
                return palette;
        }

        if (!requests.contains(name)) {
            requests.add(name);
            NetworkHandler.wrapper.sendToServer(new MessageRequestPalette(name));
        }

        return null;
    }

    public static void putPalette(CompiledPalette palette) {
        for (Iterator<CompiledPalette> it = palettesList.iterator(); it.hasNext(); ) {
            CompiledPalette palette2 = it.next();
            if (palette2.getName().equals(palette.getName())) {
                palette2.dispose();
                it.remove();
            }
        }

        requests.remove(palette.getName());

        palettesList.add(palette);
    }

}
