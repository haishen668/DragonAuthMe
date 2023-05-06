package ltd.dreamcraft.www;

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.network.PacketSender;
import fr.xephi.authme.api.v3.AuthMeApi;
import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.Manager.DataManager;
import ltd.dreamcraft.www.command.admincommand.AdminCommand;
import ltd.dreamcraft.www.command.playercommand.PlayerCommand;
import ltd.dreamcraft.www.event.PlayerRegisterEvent;
import ltd.dreamcraft.www.tools.GetVerificationCode;
import ltd.dreamcraft.www.tools.Lang;
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
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getConsoleSender;

public class DragonAuthMe extends JavaPlugin implements Listener {


    public static DragonAuthMe in() {
        return getPlugin(DragonAuthMe.class);
    }
    private AuthMeApi authMeApi;
    private Set<String> safeUsers = new HashSet<>();
    private String DragonAuthMeguiName;
    private String code;
    @Override
    public void onEnable() {
//        getConfig().options().copyDefaults(true);
//        saveConfig();
        //初始化配置文件
        DataManager d = new DataManager();
        d.set();
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        getConsoleSender().sendMessage("§3 ____                                 _         _   _                           ");
        getConsoleSender().sendMessage("§3|  _ \\ _ __ __ _  __ _  ___  _ __    / \\  _   _| |_| |__  _ _                 ");
        getConsoleSender().sendMessage("§3| | | | '__/ _` |/ _` |/ _ \\| '_ \\  / _ \\| | | | __| '_ \\| '_ ` _ \\ / _ \\ ");
        getConsoleSender().sendMessage("§3| |_| | | | (_| | (_| | (_) | | | |/ ___ \\ |_| | |_| | | | | | | | |  __/      ");
        getConsoleSender().sendMessage("§3|____/|_|  \\__,_|\\__, |\\___/|_| |_/_/   \\_\\__,_|\\__|_| |_|_| |_| |_|\\___|");
        getConsoleSender().sendMessage("§3                 |___/                                ");
        getConsoleSender().sendMessage("§b|> §e欢迎使用DragonAuthMe龙核附属登录插件!");
        getConsoleSender().sendMessage("§b|> §e当前版本为："+ getDescription().getVersion());
        //是否启用邮箱
        if (getConfig().getBoolean("Email.enable") == true){
            DragonAuthMeguiName = "DragonAuthMePro";
            getConsoleSender().sendMessage("§b|> §e邮箱功能已启用");
        } else if (getConfig().getBoolean("Email.enable") == false) {
            DragonAuthMeguiName = "DragonAuthMe";
            getConsoleSender().sendMessage("§b|> §e邮箱功能未启用");
        }
        //是否启用AuthMe的注册功能 参数默认为   true = !ConfigManager.getSettingAuthMeRegister()
        if (!ConfigManager.getSettingAuthMeRegister()){
            getServer().getPluginManager().registerEvents(new PlayerRegisterEvent(), this);
            getConsoleSender().sendMessage("§b|> §eAuthMe注册功能已关闭");
        }
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
        //命令注册
        this.getCommand("DragonAuthMe").setExecutor(new PlayerCommand());
        this.getCommand("DragonAuthMeAdmin").setExecutor(new AdminCommand());

    }

    @Override
    public void onDisable() {
        // 清理插件时添加代码
        getLogger().info(getName() + "已经卸载");
    }
    //使玩家执行龙核方法
    private void sendRunFunction(Player player, String guiName ,String funcName) {
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
            sendRunFunction(player, DragonAuthMeguiName ,authMeApi.isRegistered(player.getName()) ? "登录模式" : "注册模式");
            sendRunFunction(player,"DragonRecoverPsd","找回密码模式");
            return;
        }
        if ("returnLogin".equals(action)){
            openGui(player);
        }
        if ("sendMessage".equals(action))
        {
            String message = data.get(1);
            DragonData.sendMessage(player.getPlayer(),message);
            return;
        }
        if ("checkEmail".equals(action)){
            getConsoleSender().sendMessage("接收到数据包checkEmail");
            String playerEmailAddress = data.get(1);
            getConsoleSender().sendMessage("玩家的邮箱地址"+EmailMain.getPlayerEmail(player.getName()));
            if ((EmailMain.getPlayerEmail(player.getName()).equals(playerEmailAddress))){
                    //将生成的验证码存储在code变量中
                    code = GetVerificationCode.get(player,playerEmailAddress);
                return;
            }else {
                DragonData.sendMessage(player.getPlayer(),"绑定邮箱不正确");
            }
            if ("null".equals((EmailMain.getPlayerEmail(player.getName())))){
                DragonData.sendMessage(player.getPlayer(),"玩家未绑定邮箱");
            }
        }
        String password = data.get(1);
        if ("RecoverPsd".equals(action)){
            String playerCode = data.get(2);
            //如果code为空，发送一条信息告诉玩家，请先获取验证码  return
            if (code == null){
                DragonData.sendMessage(player.getPlayer(),"请先获取验证码");
                return;
            }

            if (code.equals(playerCode)) {
                //绑定邮箱的方法.
                DataManager.chat.put(player.getName(), code + "-ChangePassword-" + password);
                //邮箱绑定之后 再执行下列方法 调用AuthMe的api注册账号
                authMeApi.changePassword(player.getName(), password);
                player.sendMessage(Lang.success("新密码修改成功"));
                //把倒计时变量设置为0
                DragonData.sendCodeCountdown(player.getPlayer(),0);
                getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 60L);
                player.sendMessage(Lang.success("已为您自动登录"));
            }else{
                DragonData.sendMessage(player.getPlayer(),"验证码错误");
                return;
            }
        }
        if (password == null || password.length() < 4 || password.length() > 30) {
            sendRunFunction(player, DragonAuthMeguiName ,"密码位数错误");
            return;
        }

        if ("login".equals(action)){
            if (!authMeApi.checkPassword(player.getName(), password)) {
                sendRunFunction(player, DragonAuthMeguiName ,"密码错误");
                return;
            }
            authMeApi.forceLogin(player);
            player.closeInventory();
        }

        if ("changePassword".equals(action)){
            String oldPassword = data.get(1);
            String newPassword = data.get(2);
            if (!authMeApi.checkPassword(player.getName(), oldPassword)) {
                sendRunFunction(player, DragonAuthMeguiName ,"旧密码错误");
                return;
            }
            if (newPassword == null || newPassword.length() < 4 || newPassword.length() > 30) {
                sendRunFunction(player, DragonAuthMeguiName ,"密码位数错误");
                return;
            }
            authMeApi.changePassword(player.getName(), newPassword);
            player.sendMessage(Lang.success("密码修改成功"));
            getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 2L);
        }
        if ("registerWithoutEmail".equals(action)){
            //邮箱功能关闭则直接注册，跳过验证码的验证
            if (getConfig().getBoolean("Email.enable") == false){
                player.sendMessage(Lang.success("账号注册成功"));
                authMeApi.registerPlayer(player.getName(), password);
                getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 2L);
                return;
            }
        }
        String emailAddress = data.get(2);
        Pattern pattern = Pattern.compile(ConfigManager.getSettingRegx());
        if ("getCode".equals(action)) {
            //检查邮箱地址是否合法
            if (pattern.matcher(emailAddress).matches()){
            //将生成的验证码存储在code变量中
            getConsoleSender().sendMessage("邮箱地址" + emailAddress+"已经被获取，且action=getCode");
            code = GetVerificationCode.get(player,emailAddress);
            getConsoleSender().sendMessage("获取验证码方法执行，验证码为"+code);
            }else {
                DragonData.sendMessage(player.getPlayer(),"邮箱地址不合法");
            }
            return;
        }
        if ("register".equals(action)){
            String playerCode = data.get(3);
            //如果code为空，发送一条信息告诉玩家，请先获取验证码  return
            if (code == null){
                DragonData.sendMessage(player.getPlayer(),"请先获取验证码");
                return;
            }
            if (code.equals(playerCode)) {
                DataManager.chat.put(player.getName(), code + "-Bind-" + emailAddress);
                //绑定邮箱的方法.
                EmailMain.addEmail(player.getPlayer(),emailAddress);
                //邮箱绑定之后 再执行下列方法 调用AuthMe的api注册账号
                authMeApi.registerPlayer(player.getName(), password);
                player.sendMessage(Lang.success("账号注册成功"));
                //把倒计时变量设置为0
                DragonData.sendCodeCountdown(player.getPlayer(),0);
                getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 60L);
            }else{
                DragonData.sendMessage(player.getPlayer(),"验证码错误");
                return;
            }
        }

   /*
        switch (action) {
            case "login":
                if (!authMeApi.checkPassword(player.getName(), password)) {
                    sendRunFunction(player, "密码错误");
                    return;
                }

                authMeApi.forceLogin(player);
                break;

            case "register":
                //验证getCode中的code是否与玩家发送的数据包中的playerCode一样

                if (code.equals(playerCode)) {
                    //绑定邮箱的方法.
                    DataManager.chat.put(player.getName(), code + "-Bind-" + emailAddress);
                    //邮箱绑定之后 再执行下列方法 调用AuthMe的api注册账号
                    authMeApi.registerPlayer(player.getName(), password);
                    getServer().getScheduler().runTaskLater(this, () -> authMeApi.forceLogin(player), 2L);
                    break;
                }
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
*/
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
        PacketSender.sendOpenGui(player, DragonAuthMeguiName);
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
