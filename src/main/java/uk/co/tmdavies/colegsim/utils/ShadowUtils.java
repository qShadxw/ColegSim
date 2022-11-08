package uk.co.tmdavies.colegsim.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShadowUtils {

    public static String Prefix = "&8[&cColeg&fSim&8]";
    private static String primary = "&c";
    private static String secondary = "&f";
    private static String divider = "&8";
    private static String error = "&4";
    private static final Pattern TIME_PRECISE = Pattern.compile("(\\d+)([dhmst]|ms)");

    /**
     *
     * Colours the text if there are colour codes involved.
     * Includes custom colour codes.
     * <p></p>
     * <p>%p - Primary Colour</p>
     * <p>%s - Secondary Colour</p>
     * <p>%d - Divider Colour</p>
     * <p>%e - Error Colour</p>
     *
     * @param message Given String.
     * @return Colourised String.
     */
    public static String Colour(String message) {

        message = message.replace("%prefix%", Prefix)
                .replace("%p", primary)
                .replace("%s", secondary)
                .replace("%d", divider)
                .replace("%e", error);

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    public static String Chat(String message) {

        return Colour(Prefix + " " + secondary + message);

    }

    public static String Error(String message) {

        return Chat(error + message);

    }

    public static String colourDefault(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    public static String getPrimaryColour() {

        return primary;

    }

    public static String getSecondaryColour() {

        return secondary;

    }

    public static String getDividerColour() {

        return divider;

    }

    public static String getErrorColour() {

        return error;

    }

    public static void setPrimaryColour(String primaryColour) {

        primary = primaryColour;

    }

    public static void setSecondaryColour(String secondaryColour) {

        secondary = secondaryColour;

    }

    public static void setDividerColour(String dividerColour) {

        divider = dividerColour;

    }

    public static void setErrorColour(String errorColour) {

        error = errorColour;

    }

    public static String hexColour(String message) {

        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})#");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder builder = new StringBuilder(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(builder, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                    + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                    + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5)
            );
        }

        return ShadowUtils.Colour(matcher.appendTail(builder).toString());

    }

    public static long parseTime(String timeStr) {

        Matcher matcher = TIME_PRECISE.matcher(timeStr);
        long time = 0;

        while (matcher.find()) {

            time += Long.parseLong(matcher.group(1)) * switch (matcher.group(2)) {

                case "d" -> 86400000L;
                case "h" -> 3600000L;
                case "m" -> 60000L;
                default -> 1000L; // seconds
                case "t" -> 50L;
                case "ms" -> 1L;

            };

        }

        return Math.max(time, 1);
    }
}
