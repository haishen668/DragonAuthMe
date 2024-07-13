package ltd.dreamcraft.www;

import eos.moe.dragoncore.network.PacketSender;
import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.Manager.DataManager;
import ltd.dreamcraft.www.command.admincommand.AdminCommand;
import ltd.dreamcraft.www.command.playercommand.PlayerCommand;
import ltd.dreamcraft.www.event.PlayerLoginEvent;
import ltd.dreamcraft.www.event.handleCustomPacketEvent;
import ltd.dreamcraft.www.tools.CheckPluginUpdate;
import ltd.dreamcraft.www.tools.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getConsoleSender;

public class DragonAuthMe extends JavaPlugin {
    public static DragonAuthMe in() {
        return getPlugin(DragonAuthMe.class);
    }

    public static String DragonAuthMeguiName;
//    private String code;

    @Override
    public void onEnable() {
        // 注册事件监听器 监听自定义龙核数据包事件
        getServer().getPluginManager().registerEvents(new handleCustomPacketEvent(), this);
        getConsoleSender().sendMessage("§3 ____                                 _         _   _                           ");
        getConsoleSender().sendMessage("§3|  _ \\ _ __ __ _  __ _  ___  _ __    / \\  _   _| |_| |__  _ _                 ");
        getConsoleSender().sendMessage("§3| | | | '__/ _` |/ _` |/ _ \\| '_ \\  / _ \\| | | | __| '_ \\| '_ ` _ \\ / _ \\ ");
        getConsoleSender().sendMessage("§3| |_| | | | (_| | (_| | (_) | | | |/ ___ \\ |_| | |_| | | | | | | | |  __/      ");
        getConsoleSender().sendMessage("§3|____/|_|  \\__,_|\\__, |\\___/|_| |_/_/   \\_\\__,_|\\__|_| |_|_| |_| |_|\\___|");
        getConsoleSender().sendMessage("§3                 |___/                                ");
        getConsoleSender().sendMessage("§b|> §e欢迎使用DragonAuthMe龙核附属登录插件!");
        getConsoleSender().sendMessage("§b|> §e当前版本为：" + getDescription().getVersion());
        //检查更新 如果配置文件设置了true则 检查插件是否需要更新 且 注册监听玩家登录事件，如果是op就发送更新提示
        if (ConfigManager.getCheckUpdate()) {
            CheckPluginUpdate.checkPluginUpdate("https://pluginapi.dreamcraft.ltd/api/plugins/");
        }
        getServer().getPluginManager().registerEvents(new PlayerLoginEvent(), this);
        //初始化配置文件
        DataManager d = new DataManager();
        d.set();
        //是否启用邮箱
        if (getConfig().getBoolean("Email.enable")) {
            DragonAuthMeguiName = "DragonAuthMePro";
            getConsoleSender().sendMessage("§b|> §e邮箱功能已启用");
        } else if (!getConfig().getBoolean("Email.enable")) {
            DragonAuthMeguiName = "DragonAuthMe";
            getConsoleSender().sendMessage("§b|> §e邮箱功能未启用");
        }
        Plugin authMePlugin = getServer().getPluginManager().getPlugin("AuthMe");
        Plugin dragonCorePlugin = getServer().getPluginManager().getPlugin("DragonCore");
        if (authMePlugin != null && authMePlugin.isEnabled()) {
            // AuthMe前置插件都存在并启用，初始化 AuthMe API
//            authMeApi = AuthMeApi.getInstance();
        } else {
            getLogger().warning("AuthMe 插件未找到或未启用。DragonAuthMe 将不会启用。");
            getServer().getPluginManager().disablePlugin(this);
        }
        if (dragonCorePlugin == null || !dragonCorePlugin.isEnabled()) {
            getLogger().severe("DragonCore 插件未找到或未启用。DragonAuthMe 将不会启用。");
            getServer().getPluginManager().disablePlugin(this);
        }
        getConsoleSender().sendMessage("§b|> §e前置插件已启用,插件将正常运行");
        //命令注册
        this.getCommand("DragonAuthMe").setExecutor(new PlayerCommand());
        this.getCommand("DragonAuthMeAdmin").setExecutor(new AdminCommand());
        //BStats插件统计
        int pluginId = 18458;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        // 清理插件时添加代码
        getLogger().info(getName() + "已经卸载");
    }

    //使玩家执行龙核方法
    public static void sendRunFunction(Player player, String guiName, String funcName) {
        PacketSender.sendRunFunction(player, guiName, "方法.执行方法(\"" + funcName + "\")", false);
    }

}
