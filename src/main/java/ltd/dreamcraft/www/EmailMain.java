package ltd.dreamcraft.www;

import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.tools.JDBCUtil;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bukkit.Bukkit.getConsoleSender;

public class EmailMain {
    private String emailAddress;
    private String date;
    private String playerName;

    public EmailMain(String playerName) {
        this.playerName = playerName;
        if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
            Connection con = JDBCUtil.getConnection();
            if (con != null) {
                String sql = "select * from " + ConfigManager.getMySQLtableName() + " where PlayerName='" + playerName + "'";
                try {
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        this.emailAddress = rs.getString("EmailAddress");
                        this.date = rs.getString("Date");
                    }
                               JDBCUtil.close(rs, null, con, pst);
                             } catch (Exception e) {
                               e.printStackTrace();
                             }
                       }
                 } else {
                   File file = new File("plugins/DragonAuthMe/PlayerData/" + playerName + ".yml");
                   YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                   this.emailAddress = yamlConfiguration.getString("EmailAddress");
                   this.date = yamlConfiguration.getString("Date");
                 }
           }
       public String getEmailAddress() {
             return this.emailAddress;
           }
       public void setEmailAddress(final String emailAddress) {
             this.emailAddress = emailAddress;
           if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
               (new BukkitRunnable() {
                   public void run() {
                       Connection con = JDBCUtil.getConnection();
                       if (con != null) {
                           String sql = "UPDATE " + ConfigManager.getMySQLtableName() + " SET EmailAddress='" + emailAddress + "' WHERE PlayerName='" + EmailMain.this.playerName + "'";
                           try {
                               PreparedStatement pst3 = con.prepareStatement(sql);
                               pst3.execute();
                               JDBCUtil.close(null, null, con, pst3);
                                           } catch (Exception e) {
                                             e.printStackTrace();
                                           }
                                     }
                               }
                         }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
                 } else {
                   File file = new File("plugins/DragonAuthMe/PlayerData/" + this.playerName + ".yml");
                   YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                   yamlConfiguration.set("EmailAddress", emailAddress);
                   try {
                         yamlConfiguration.save(file);
                       } catch (IOException ioException) {
                         ioException.printStackTrace();
                       }
                 }
           }

       public String getDate() {
             return this.date;
           }

       public void setDate(final String date) {
           if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
               (new BukkitRunnable() {
                   public void run() {
                       Connection con = JDBCUtil.getConnection();
                       if (con != null) {
                           String sql = "UPDATE " + ConfigManager.getMySQLtableName() + " SET Date='" + date + "' WHERE PlayerName='" + EmailMain.this.playerName + "'";
                           try {
                               PreparedStatement pst3 = con.prepareStatement(sql);
                               pst3.execute();
                               JDBCUtil.close(null, null, con, pst3);
                                           } catch (Exception e) {
                                             e.printStackTrace();
                                           }
                                     }
                               }
                         }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
                 } else {
                   File file = new File("plugins/DragonAuthMe/PlayerData/" + this.playerName + ".yml");
                   YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                   yamlConfiguration.set("Date", date);
                   try {
                         yamlConfiguration.save(file);
                       } catch (IOException ioException) {
                         ioException.printStackTrace();
                       }
                   this.date = date;
                 }
           }
       public boolean hasEmailAddress() {
             return (this.emailAddress != null);
           }
       public static void addEmail(final CommandSender sender, final String playerName, final String emailAddress) {
           if (isEmailUsed(emailAddress)) {
               sender.sendMessage(Lang.error("该邮箱已经被其他玩家绑定..."));
               return;
           }
             final List list = new ArrayList();
           if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
               (new BukkitRunnable() {
                   public void run() {
                       try {
                           Connection con = JDBCUtil.getConnection();
                           Statement st = con.createStatement();
                           String sql1 = "SELECT PlayerName FROM " + ConfigManager.getMySQLtableName();
                           ResultSet rs1 = st.executeQuery(sql1);
                           while (rs1.next()) {
                               list.add(rs1.getString(1));
                                           }
                                       if (list.contains(playerName)) {
                                             sender.sendMessage(Lang.error("该玩家文件已经存在,请使用指令编辑..."));
                                           } else {
                                             sender.sendMessage(Lang.success("添加成功..."));
                                             String sql2 = "insert into " + ConfigManager.getMySQLtableName() + "(PlayerName, EmailAddress, Date) values(?,?,?)";
                                             PreparedStatement ps = con.prepareStatement(sql2);
                                             Date date = new Date();
                                             ps.setString(1, playerName);
                                             ps.setString(2, emailAddress);
                                             ps.setString(3, date.toString());
                                             ps.executeUpdate();
                                             ps.close();
                                           }
                                     } catch (Exception e) {
                                       e.printStackTrace();
                                     }
                               }
                         }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
                 } else {
                   File file = new File("plugins/DragonAuthMe/PlayerData/" + playerName + ".yml");
                   if (file.exists()) {
                         sender.sendMessage(Lang.error("该玩家文件已经存在,请使用指令编辑..."));
                       } else {
                         try {
                               file.createNewFile();
                               Date date = new Date();
                               YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                               yamlConfiguration.set("EmailAddress", emailAddress);
                               yamlConfiguration.set("Date", date.toString());
                               yamlConfiguration.save(file);
                               sender.sendMessage(Lang.success("添加成功..."));
                             } catch (IOException ioException) {
                               ioException.printStackTrace();
                             }
                       }
                 }
           }
    public static void addEmail(final Player player, final String emailAddress) {
        String playerName = player.getName();
           if (isEmailUsed(emailAddress)) {
            DragonData.sendMessage(player,"该邮箱已经被其他玩家绑定...");
            return;
        }
        final List list = new ArrayList();
        if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
            (new BukkitRunnable() {
                public void run() {
                    try {
                        Connection con = JDBCUtil.getConnection();
                        Statement st = con.createStatement();
                        String sql1 = "SELECT PlayerName FROM " + ConfigManager.getMySQLtableName();
                        ResultSet rs1 = st.executeQuery(sql1);
                        while (rs1.next()) {
                            list.add(rs1.getString(1));
                        }
                        if (list.contains(playerName)) {
                            getConsoleSender().sendMessage(Lang.error("该玩家文件已经存在,请使用指令编辑..."));
                        } else {
                            getConsoleSender().sendMessage(Lang.success("添加成功..."));
                            String sql2 = "insert into " + ConfigManager.getMySQLtableName() + "(PlayerName, EmailAddress, Date) values(?,?,?)";
                            PreparedStatement ps = con.prepareStatement(sql2);
                            Date date = new Date();
                            ps.setString(1, playerName);
                            ps.setString(2, emailAddress);
                            ps.setString(3, date.toString());
                            ps.executeUpdate();
                            ps.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
        } else {
            File file = new File("plugins/DragonAuthMe/PlayerData/" + playerName + ".yml");
            if (file.exists()) {
                getConsoleSender().sendMessage(Lang.error("该玩家文件已经存在,请使用指令编辑..."));
            } else {
                try {
                    file.createNewFile();
                    Date date = new Date();
                    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                    yamlConfiguration.set("EmailAddress", emailAddress);
                    yamlConfiguration.set("Date", date.toString());
                    yamlConfiguration.save(file);
                    getConsoleSender().sendMessage(Lang.success("添加成功..."));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
       public static void deleteEmail(final CommandSender sender, final String playerName) {
             final List list = new ArrayList();
           if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
               (new BukkitRunnable() {
                   public void run() {
                       try {
                           Connection con = JDBCUtil.getConnection();
                           Statement st = con.createStatement();
                           String sql1 = "SELECT PlayerName FROM " + ConfigManager.getMySQLtableName();
                           ResultSet rs1 = st.executeQuery(sql1);
                           while (rs1.next()) {
                               list.add(rs1.getString(1));
                                           }
                                       if (list.contains(playerName)) {
                                             String sql2 = "delete from " + ConfigManager.getMySQLtableName() + " where PlayerName='" + playerName + "'";
                                             Statement st1 = con.createStatement();
                                             st1.executeUpdate(sql2);
                                             st1.close();
                                             sender.sendMessage(Lang.success("删除成功..."));
                                             JDBCUtil.close(rs1, st, con, null);
                                           } else {
                                             sender.sendMessage(Lang.error("该玩家数据不存在..."));
                                           }
                                     } catch (Exception e) {
                                       e.printStackTrace();
                                     }
                               }
                         }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
                 } else {
                   File file = new File("plugins/DragonAuthMe/PlayerData/" + playerName + ".yml");
                   if (file.exists()) {
                         file.delete();
                         sender.sendMessage(Lang.success("删除成功..."));
                       } else {
                         sender.sendMessage(Lang.error("该玩家数据不存在"));
                       }
                 }
           }
       public static void editEmail(final CommandSender sender, final String playerName, final String emailAddress) {
           if (isEmailUsed(emailAddress)) {
               sender.sendMessage(Lang.error("该新邮箱已经被其他玩家绑定..."));
               return;
           }
           final List list = new ArrayList();
           if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
               (new BukkitRunnable() {
                   public void run() {
                       try {
                           Connection con = JDBCUtil.getConnection();
                           Statement st = con.createStatement();
                           String sql1 = "SELECT PlayerName FROM " + ConfigManager.getMySQLtableName();
                           ResultSet rs1 = st.executeQuery(sql1);
                           while (rs1.next()) {
                               list.add(rs1.getString(1));
                                           }
                                       if (list.contains(playerName)) {
                                             EmailMain emailMain = new EmailMain(playerName);
                                             emailMain.setEmailAddress(emailAddress);
                                             Date date = new Date();
                                             emailMain.setDate(date.toString());
                                             sender.sendMessage(Lang.success("修改成功..."));
                                             JDBCUtil.close(rs1, st, con, null);
                                           }

                                     } catch (Exception e) {
                                       sender.sendMessage(Lang.error("该玩家数据不存在..."));
                                     }
                               }
                         }).runTaskAsynchronously((Plugin) DragonAuthMe.in());
                 } else {
                   File file = new File("plugins/DragonAuthMe/PlayerData/" + playerName + ".yml");
                   if (file.exists()) {
                         EmailMain emailMain = new EmailMain(playerName);
                         emailMain.setEmailAddress(emailAddress);
                         Date date = new Date();
                         emailMain.setDate(date.toString());
                         sender.sendMessage(Lang.success("修改成功..."));
                       } else {
                         sender.sendMessage(Lang.error("该玩家数据不存在"));
                       }
                 }
           }

    // 检查邮箱是否已被其他玩家绑定
    public static boolean isEmailUsed(String emailAddress) {
        if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
            try {
                Connection con = JDBCUtil.getConnection();
                String sql = "SELECT * FROM " + ConfigManager.getMySQLtableName() + " WHERE EmailAddress='" + emailAddress + "'";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                boolean isUsed = rs.next();
                JDBCUtil.close(rs, null, con, pst);
                return isUsed;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File folder = new File("plugins/DragonAuthMe/PlayerData/");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                    if (emailAddress.equals(yamlConfiguration.getString("EmailAddress"))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    //方法接收一个玩家名（String playerName）作为参数，并返回该玩家的邮箱地址。如果找不到该玩家或该玩家未绑定邮箱，返回 null。
    public static String getPlayerEmail(String playerName) {
        String emailAddress = null;

        if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
            try {
                Connection con = JDBCUtil.getConnection();
                String sql = "SELECT * FROM " + ConfigManager.getMySQLtableName() + " WHERE PlayerName='" + playerName + "'";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    emailAddress = rs.getString("EmailAddress");
                }
                JDBCUtil.close(rs, null, con, pst);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File file = new File("plugins/DragonAuthMe/PlayerData/" + playerName + ".yml");
            if (file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                emailAddress = yamlConfiguration.getString("EmailAddress");
            }
        }

        return emailAddress;
    }

    /**
     * 获取绑定某个邮箱的玩家名字
     *
     * @param emailAddress 玩家邮箱
     * @return 方法接收一个邮箱地址作为参数，并返回匹配的玩家ID（如果有） 如果没有返回null.
     */
    public static String findPlayerIDByEmail(String emailAddress) {
        String playerID = null;

        // 检查数据库中的数据
        if (ConfigManager.getStorageType().equalsIgnoreCase("MySQL")) {
            try {
                Connection con = JDBCUtil.getConnection();
                String sql = "SELECT PlayerName FROM " + ConfigManager.getMySQLtableName() + " WHERE EmailAddress=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, emailAddress);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    playerID = rs.getString("PlayerName");
                }

                JDBCUtil.close(rs, null, con, pst);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 获取玩家数据文件夹路径
            File playerDataFolder = new File("plugins/DragonAuthMe/PlayerData/");

            // 遍历玩家数据文件夹下的所有文件
            File[] playerFiles = playerDataFolder.listFiles();
            if (playerFiles != null) {
                for (File file : playerFiles) {
                    String playerName = file.getName().replace(".yml", "");
                    String playerEmail = getPlayerEmail(playerName);

                    // 检查文件中的邮箱地址是否匹配
                    if (playerEmail != null && playerEmail.equalsIgnoreCase(emailAddress)) {
                        playerID = playerName;
                        break; // 找到匹配的玩家ID后立即返回
                    }
                }
            }
        }

        return playerID;
    }

    public static String getPlayerEmailByPlayer(Player player) {
        String playerName = player.getName();
        return getPlayerEmail(playerName);
    }

    public static void forcePlayerToRegister(Player player, String password) {
        String command = "register " + password + " " + password;
        Bukkit.dispatchCommand(player, command);
    }




}