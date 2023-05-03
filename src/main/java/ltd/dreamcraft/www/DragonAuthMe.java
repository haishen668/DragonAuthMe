package ltd.dreamcraft.www;

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.network.PacketSender;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.bukkit.Bukkit.getConsoleSender;

public class DragonAuthMe extends JavaPlugin implements Listener {

    private AuthMeApi authMeApi;
    private Set<String> safeUsers = new HashSet<>();
    private String guiName;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        guiName = getConfig().getString("guiName");
        getConsoleSender().sendMessage("§3 ____                                 _         _   _                           ");
        getConsoleSender().sendMessage("§3|  _ \\ _ __ __ _  __ _  ___  _ __    / \\  _   _| |_| |__  _ _                 ");
        getConsoleSender().sendMessage("§3| | | | '__/ _` |/ _` |/ _ \\| '_ \\  / _ \\| | | | __| '_ \\| '_ ` _ \\ / _ \\ ");
        getConsoleSender().sendMessage("§3| |_| | | | (_| | (_| | (_) | | | |/ ___ \\ |_| | |_| | | | | | | | |  __/      ");
        getConsoleSender().sendMessage("§3|____/|_|  \\__,_|\\__, |\\___/|_| |_/_/   \\_\\__,_|\\__|_| |_|_| |_| |_|\\___|");
        getConsoleSender().sendMessage("§3                 |___/                                ");
        getConsoleSender().sendMessage("§b|> §e欢迎使用DragonAuthMe龙核附属登录插件!");
        getConsoleSender().sendMessage("§b|> §e${project.version}");
        Plugin authMePlugin = getServer().getPluginManager().getPlugin("AuthMe");
        Plugin dragonCorePlugin = getServer().getPluginManager().getPlugin("DragonCore");
        if (authMePlugin != null && authMePlugin.isEnabled()) {
            // AuthMe前置插件都存在并启用，初始化 AuthMe API
            authMeApi = AuthMeApi.getInstance();
        } else {
            getLogger().warning("AuthMe 插件未找到或未启用。DragonAuthMe 将不会启用。");
            getServer().getPluginManager().disablePlugin(this);
        }
        if (dragonCorePlugin == null || !dragonCorePlugin.isEnabled()) {
            getLogger().severe("DragonCore 插件未找到或未启用。DragonAuthMe 将不会启用。");
            getServer().getPluginManager().disablePlugin(this);
        }
        getConsoleSender().sendMessage("§b|> §e前置插件已启用,插件将正常运行");
    }

    @Override
    public void onDisable() {
        // 清理插件时添加代码
        getLogger().info(getName() + "已经卸载");
    }
    private void sendRunFunction(Player player, String funcName) {
        PacketSender.sendRunFunction(player, guiName, "方法.执行方法(\"" + funcName + "\")", false);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void handleCustomPacketEvent(CustomPacketEvent event) {
        if (!event.getIdentifier().equals("DragonAuthMe")) {
            return;
        }

        Player player = event.getPlayer();
        List<String> data = event.getData();
        String action = data.get(0);

        if ("prelogin".equals(action)) {
            if (authMeApi.isAuthenticated(player)) {
                player.closeInventory();
                return;
            }

            safeUsers.add(player.getName());
            sendRunFunction(player, authMeApi.isRegistered(player.getName()) ? "登录模式" : "注册模式");
            return;
        }

        String password = data.get(1);

        if (password == null || password.length() < 4 || password.length() > 30) {
            sendRunFunction(player, "密码位数错误");
            return;
        }

        switch (action) {
            case "login":
                if (!authMeApi.checkPassword(player.getName(), password)) {
                    sendRunFunction(player, "密码错误");
                    return;
                }

                authMeApi.forceLogin(player);
                break;
            case "register":
                authMeApi.registerPlayer(player.getName(), password);
                getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 2L);
                break;
            case "changePassword":
                String oldPassword = data.get(1);
                String newPassword = data.get(2);
                if (!authMeApi.checkPassword(player.getName(), oldPassword)) {
                    sendRunFunction(player, "旧密码错误");
                    return;
                }
                if (newPassword == null || newPassword.length() < 4 || newPassword.length() > 30) {
                    sendRunFunction(player, "密码位数错误");
                    return;
                }
                authMeApi.changePassword(player.getName(), newPassword);
                player.sendMessage("§a密码修改成功!");
                getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 2L);
                break;
            default:
                player.sendMessage("§c未知操作!");
                break;
        }

        player.closeInventory();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 获取玩家对象
        Player player = event.getPlayer();

        // 使用延时任务打开 GUI
        new BukkitRunnable() {
            @Override
            public void run() {
                openGui(player);
            }
        }.runTaskLater(this, 10);
    }

    // 创建一个打开 GUI 的方法
    private void openGui(Player player) {
        // 检查玩家是否在线
        if (!player.isOnline()) {
            return;
        }

        // 检查玩家是否已通过身份验证
        if (authMeApi.isAuthenticated(player)) {
            return;
        }

        // 发送打开 GUI 的数据包
        PacketSender.sendOpenGui(player, getConfig().getString("guiName"));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!safeUsers.contains(player.getName())) {
                    openGui(player);
                }
            }
        }.runTaskLater(this, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 从 safeUsers 集合中移除离开的玩家
        safeUsers.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        // 获取玩家对象
        Player player = event.getPlayer();
        // 如果玩家已通过身份验证，则不执行任何操作
        if (authMeApi.isAuthenticated(player)) {
            return;
        }
        // 打开玩家的 GUI
        openGui(player);
    }
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        // 从 safeUsers 集合中移除被踢出的玩家
        safeUsers.remove(event.getPlayer().getName());
    }
}
