package ltd.dreamcraft.www.Manager;

import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.tools.JDBCUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class DataManager {
    static File file = null;

    public static HashMap<String, String> chat = new HashMap<>();

    public void set() {
        setConfig();
        setPlayerData();
    }

    private void setConfig() {
        File file = new File("plugins/DragonAuthMe/config.yml");
        if (!file.exists())
            DragonAuthMe.in().saveDefaultConfig();
    }

    public static FileConfiguration getConfig() {
        return DragonAuthMe.getPlugin(DragonAuthMe.class).getConfig();
    }

    private void setPlayerData() {
        if (ConfigManager.getStorgeType().equalsIgnoreCase("MySQL")) {
            (new BukkitRunnable() {
                public void run() {
                    Connection con = JDBCUtil.getConnection();
                    String sql = "CREATE TABLE " + ConfigManager.getMySQLtableName() + " (ID INT PRIMARY KEY AUTO_INCREMENT, PlayerName VARCHAR(200), EmailAdress VARCHAR(200), Date VARCHAR(200))";
                    if (con != null)
                        try {
                            DatabaseMetaData meta = con.getMetaData();
                            ResultSet resultSet = meta.getTables(null, null, ConfigManager.getMySQLtableName(), new String[] { "TABLE" });
                            boolean b = resultSet.next();
                            Statement statement = con.createStatement();
                            if (!b) {
                                statement.executeUpdate(sql);
                                JDBCUtil.close(resultSet, statement, con, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
        } else {
            File file = new File("plugins/DragonAuthMe/PlayerData");
            if (!file.exists())
                file.mkdir();
        }
    }
}