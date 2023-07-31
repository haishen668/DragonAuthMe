package ltd.dreamcraft.www.Manager;

import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.tools.Lang;

import static org.bukkit.Bukkit.getConsoleSender;

public class ConfigManager {
    public static void reloadConfig() {
        DragonAuthMe.in().reloadConfig();
    }

    public static void saveConfig() {
        DragonAuthMe.getPlugin(DragonAuthMe.class).saveConfig();
        getConsoleSender().sendMessage(Lang.success("配置文件 config.yml 保存成功..."));
    }

    public static boolean getCheckUpdate() {
        return DataManager.getConfig().getBoolean("check-update");
    }

    public static String getMySQLURL() {
        return DataManager.getConfig().getString("Storage.MySQLIP");
    }

    public static String getMySQLuserName() {
        return DataManager.getConfig().getString("Storage.MySQLUserName");
    }

    public static String getMySQLpassWord() {
        return DataManager.getConfig().getString("Storage.MySQLPassWord");
    }

    public static String getMySQLdataBase() {
        return DataManager.getConfig().getString("Storage.MySQLDataBase");
    }

    public static String getMySQLtableName() {
        return DataManager.getConfig().getString("Storage.MySQLTableName");
    }

    public static String getStorageType() {
        return DataManager.getConfig().getString("Storage.Type");
    }

    public static String getEmailAddressIP() {
        return DataManager.getConfig().getString("Email.Address.IP");
    }

    public static int getEmailAddressPort() {
        return DataManager.getConfig().getInt("Email.Address.Port");
    }

    public static boolean getEmailAddressSSL() {
        return DataManager.getConfig().getBoolean("Email.Address.UseSSL");
    }

    public static String getEmailAuthAccount() {
        return DataManager.getConfig().getString("Email.Auth.Account");
    }

    public static String getEmailAuthPassword() {
        return DataManager.getConfig().getString("Email.Auth.Password");
    }

    public static String getContentEmailAddress() {
        return DataManager.getConfig().getString("Email.Content.SendEmailAddress");
    }

    public static String getContentEmailTitle() {
        return DataManager.getConfig().getString("Email.Content.SendEmailTitle");
    }

    public static String getHtmlFilePath() {
        return DragonAuthMe.in().getDataFolder() + "\\" + DataManager.getConfig().getString("Email.Content.SendEmailFile");
    }

    public static String getContentEmailText() {
        return DataManager.getConfig().getString("Email.Content.SendEmailText");
    }

    public static int getSettingCodeLength() {
        return DataManager.getConfig().getInt("Setting.CodeLength");
    }

    public static boolean getSettingForceBind() {
        return DataManager.getConfig().getBoolean("Setting.ForceBind");
    }

    public static boolean getSettingAuthMePassword() {
        return DataManager.getConfig().getBoolean("Setting.AuthMePassword");
    }
    public static boolean getSettingAuthMeRegister(){
        return DataManager.getConfig().getBoolean("Setting.AuthMeRegister");
    }

    public static String getSettingRegx() {
        return DataManager.getConfig().getString("Setting.Regx");
    }
}
