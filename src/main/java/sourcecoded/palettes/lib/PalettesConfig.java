package sourcecoded.palettes.lib;

import net.minecraftforge.common.config.Configuration;
import sourcecoded.core.configuration.VersionConfig;

public class PalettesConfig {

    private static VersionConfig config;

    public static void init(VersionConfig config) {
        PalettesConfig.config = config;
        startCategories();
        startProperties();
    }

    static void startCategories() {
        Categories[] cats = Categories.values();    //Meow :3
        for (Categories cat : cats)
            getRawConfig().addCustomCategoryComment(cat.getName(), cat.getComment());

        getConfig().saveConfig();
    }

    static void startProperties() {
        Properties[] props = Properties.values();
        for (Properties prop : props) {
            Object value = prop.getDefault();

            if (value instanceof Integer)
                getConfig().createProperty(prop.getCategory(), prop.getName(), (Integer)value);
            else if (value instanceof Double)
                getConfig().createProperty(prop.getCategory(), prop.getName(), (Double)value);
            else if (value instanceof String)
                getConfig().createProperty(prop.getCategory(), prop.getName(), (String)value);
            else if (value instanceof Boolean)
                getConfig().createProperty(prop.getCategory(), prop.getName(), (Boolean) value);

            if (prop.getComment() != null)
                getConfig().setComment(prop.getCategory(), prop.getName(), prop.getComment());
        }

        getConfig().saveConfig();
    }

    public static int getInteger(Properties prop) {
        return config.getInteger(prop.getCategory(), prop.getName());
    }

    public static double getDouble(Properties prop) {
        return config.getDouble(prop.getCategory(), prop.getName());
    }

    public static String getString(Properties prop) {
        return config.getString(prop.getCategory(), prop.getName());
    }

    public static Boolean getBoolean(Properties prop) {
        return config.getBool(prop.getCategory(), prop.getName());
    }

    public static VersionConfig getConfig() {
        return config;
    }

    public static Configuration getRawConfig() {
        return config.config;
    }

    public static enum Categories {
        ;

        private String categoryName, comment;

        Categories(String categoryName, String comment) {
            this.categoryName = categoryName;
            this.comment = comment;
        }

        public String getName() {
            return categoryName.toLowerCase();
        }

        public String getComment() {
            return comment;
        }

        public String toString() {
            return getName();
        }
    }

    public static enum Properties {
        ;

        String category, propertyName, comment;
        Object defaultValue;

        Properties(String category, String propertyName, String comment, Object def) {
            this.category = category;
            this.propertyName = propertyName;
            this.comment = comment;
            this.defaultValue = def;
        }

        Properties(Categories category, String propertyName, String comment, Object def) {
            this(category.getName(), propertyName, comment, def);
        }

        public String getCategory() {
            return category;
        }

        public String getName() {
            return propertyName.toLowerCase();
        }

        public String getComment() {
            return comment;
        }

        public Object getDefault() {
            return defaultValue;
        }

        public String toString() {
            return getName();
        }
    }

}
