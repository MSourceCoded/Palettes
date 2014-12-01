package sourcecoded.palettes.lib;

public class ColourUtils {

    public static int rgbToInt_F(float r, float g, float b) {
        return rgbToInt_I((int) (r * 255F), (int) (g * 255F), (int) (b * 255F));
    }

    public static int rgbToInt_I(int r, int g, int b) {
        int base = 0;
        base += r;
        base = base << 8;
        base += g;
        base = base << 8;
        base += b;

        return base & 0xFFFFFF;

    }

    public static int[] intToRGB_I(int base) {
        int[] ret = new int[3];
        ret[0] = base >> 16 & 0xFF;
        ret[1] = base >> 8 & 0xFF;
        ret[2] = base & 0xFF;

        return ret;
    }

    public static float[] intToRGB_F(int base) {
        float[] rgb = new float[3];
        int[] rgbI = intToRGB_I(base);
        for (int i = 0; i < 3; i ++)
            rgb[i] = ((float)rgbI[i] / 255F);

        return rgb;
    }
}
