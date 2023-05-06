package ltd.dreamcraft.www.tools;

import java.util.ArrayList;
import java.util.List;

public class Lang {
    public static String error(String s) {
        String error = "§f[§eDragonAuthMe§f] §f[§cERROR§f] §f- §7" + s;
        return error.replaceAll("&", "§");
    }

    public static String success(String s) {
        String success = "§f[§eDragonAuthMe§f] §f[§aSUCCESS§f] §f- §7" + s;
        return success.replaceAll("&", "§");
    }

    public static String warn(String s) {
        String warn = "§f[§eDragonAuthMe§f] §f[§eWARN§f] §f- §7" + s;
        return warn.replaceAll("&", "§");
    }

    public static String prefix(String s) {
        return "§f[§eDragonAuthMe§f] " + s.replaceAll("&", "§");
    }

    public static String ChatColor(String message) {
        return message.replaceAll("&", "§");
    }

    public static List<String> ChatColor(List<String> message) {
        List<String> list = new ArrayList();

        for(String s : message) {
            list.add(s.replaceAll("&", "§"));
        }

        return list;
    }
}