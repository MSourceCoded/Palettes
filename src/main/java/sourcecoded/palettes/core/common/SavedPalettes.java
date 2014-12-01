package sourcecoded.palettes.core.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import sourcecoded.core.util.SourceLogger;
import sourcecoded.palettes.lib.PaletteFileHandler;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedPalettes {

    public static SourceLogger log = new SourceLogger("Palettes -- Save Handler");

    public static void savePalette(String name, BufferedImage palette) {
        try {
            File saveDir = new File(PaletteFileHandler.saveDir, name + ".png");
            saveDir.createNewFile();

            ImageIO.write(palette, "PNG", saveDir);
        } catch (IOException e) {
            log.error("Could not save palette: " + name);
            e.printStackTrace();
        }
    }

    public static List<String> getPalettes() {
        String[] list = PaletteFileHandler.saveDir.list();
        for (int i = 0; i < list.length; i++)
            list[i] = list[i].replace(".png", "");

        return Arrays.asList(list);
    }

    public static BufferedImage loadPalette(String name) {
        BufferedImage image = null;
        File file = new File(PaletteFileHandler.saveDir, name + ".png");

        if (file.exists()) {
            try {
                image = ImageIO.read(file);
                return image;
            } catch (IOException e) {
                log.error("Could not load palette: " + name);
                e.printStackTrace();
            }
        }
        return null;
    }

}
