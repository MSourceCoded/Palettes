package sourcecoded.palettes.lib;

import sourcecoded.core.SourceCodedCore;

import java.io.File;

public class PaletteFileHandler {

    public static File saveDir;

    public static void init() {
        saveDir = new File(SourceCodedCore.getForgeRoot(), "palettes");
        saveDir.mkdirs();
    }

}
