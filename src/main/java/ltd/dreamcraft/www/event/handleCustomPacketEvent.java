package ltd.dreamcraft.www.event;

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.network.PacketSender;
import fr.xephi.authme.api.v3.AuthMeApi;
import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.DragonData;
import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.Manager.DataManager;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static ltd.dreamcraft.www.DragonAuthMe.DragonAuthMeguiName;
import static ltd.dreamcraft.www.DragonAuthMe.sendRunFunction;
import static ltd.dreamcraft.www.EmailMain.isEmailUsed;
import static org.bukkit.Bukkit.getConsoleSender;


/*
    处理龙核自定义数据包
    safeUsers 还未通过验证的玩家
    code 验证码
    emailAddress 邮箱地址
*/

public class handleCustomPacketEvent implements Listener {
    private final AuthMeApi authMeApi = AuthMeApi.getInstance();
    private Set<String> safeUsers = new HashSet<>();
    private String code;
    private String emailAddress;

    @EventHandler(priority = EventPriority.LOWEST)
    public void CustomPacketEvent(CustomPacketEvent event) {
        if (!event.getIdentifier().equals("DragonAuthMe")) {
            return;
        }

        Player player = event.getPlayer();
        List<String> data = event.getData();
        String action = data.get(0);

        //找回密码
        if ("DragonRecoverPsd".equals(action)) {
            if (authMeApi.isAuthenticated(player)) {
                player.closeInventory();
                return;
            }

            safeUsers.add(player.getName());
            sendRunFunction(player, "DragonRecoverPsd", "找回密码模式");
            return;
        }
        //绑定邮箱
        if ("DragonBindEmail".equals(action)) {
            String playerCode = data.get(2);
            if (isEmailUsed(emailAddress)) {
                DragonData.sendMessage(player, "该邮箱已经被其他玩家绑定...");
                return;
            }

            //如果code为空，发送一条信息告诉玩家，请先获取验证码  return
            if (code == null) {
                DragonData.sendMessage(player.getPlayer(), "请先获取验证码");
                return;
            }
            if (code.equals(playerCode)) {
//                DataManager.chat.put(player.getName(), code + "-Bind-" + emailAddress);
                //绑定邮箱的方法.
                EmailMain.addEmail(player.getPlayer(), emailAddress);
                //把倒计时变量设置为0
                DragonData.sendCodeCountdown(player.getPlayer(), 0);
                player.closeInventory();
                //发送一条title告诉玩家邮箱绑定成功
                player.sendTitle("§a恭喜你" + player.getName(), "§c邮箱绑定成功！", 10, 40, 10);
                return;
            } else {
                DragonData.sendMessage(player.getPlayer(), "验证码错误");
                return;
            }
        }
        if ("prelogin".equals(action)) {
            if (authMeApi.isAuthenticated(player)) {
                player.closeInventory();
                return;
            }

            safeUsers.add(player.getName());
            sendRunFunction(player, DragonAuthMeguiName, authMeApi.isRegistered(player.getName()) ? "登录模式" : "注册模式");
            return;
        }
        if ("returnLogin".equals(action)) {
            openGui(player);
            return;
        }
        if ("sendMessage".equals(action)) {
            if (data.size() > 1) {
                String message = data.get(1);
                DragonData.sendMessage(player.getPlayer(), message);
            }
            return;
        }
        if ("checkEmail".equals(action)) {
            String playerEmailAddress = data.get(1);
            getConsoleSender().sendMessage("玩家的邮箱地址" + EmailMain.getPlayerEmail(player.getName()));
            if ((EmailMain.getPlayerEmail(player.getName()).equals(playerEmailAddress))) {
                //将生成的验证码存储在code变量中
                code = GetVerificationCode.get(player, playerEmailAddress);
                return;
            } else {
                DragonData.sendMessage(player.getPlayer(), "绑定邮箱不正确");
            }
            if ("null".equals((EmailMain.getPlayerEmail(player.getName())))) {
                DragonData.sendMessage(player.getPlayer(), "玩家未绑定邮箱");
            }
            return;
        }
        //找回密码
        String password = data.get(1);
        if ("RecoverPsd".equals(action)) {
            String playerCode = data.get(2);
            //如果code为空，发送一条信息告诉玩家，请先获取验证码  return
            if (code == null) {
                DragonData.sendMessage(player.getPlayer(), "请先获取验证码");
                return;
            }

            if (code.equals(playerCode)) {
                //绑定邮箱的方法.
                DataManager.chat.put(player.getName(), code + "-ChangePassword-" + password);
                //邮箱绑定之后 再执行下列方法 调用AuthMe的api注册账号
                authMeApi.changePassword(player.getName(), password);
                player.sendMessage(Lang.success("新密码修改成功"));
                //把倒计时变量设置为0
                DragonData.sendCodeCountdown(player.getPlayer(), 0);
                DragonAuthMe.in().getServer().getScheduler().runTaskLater(DragonAuthMe.in(), () -> authMeApi.forceLogin(player), 60L);
                player.sendMessage(Lang.success("已为您自动登录"));
            } else {
                DragonData.sendMessage(player.getPlayer(), "验证码错误");
                return;
            }
        }
        if (password == null || password.length() < 4 || password.length() > 30) {
            sendRunFunction(player, DragonAuthMeguiName, "密码位数错误");
            return;
        }

        if ("login".equals(action)) {
            if (!authMeApi.checkPassword(player.getName(), password)) {
                sendRunFunction(player, DragonAuthMeguiName, "密码错误");
                return;
            }
            authMeApi.forceLogin(player);
            player.closeInventory();
            return;
        }

        if ("changePassword".equals(action)) {
            String oldPassword = data.get(1);
            String newPassword = data.get(2);
            if (!authMeApi.checkPassword(player.getName(), oldPassword)) {
                sendRunFunction(player, DragonAuthMeguiName, "旧密码错误");
                return;
            }
            if (newPassword == null || newPassword.length() < 4 || newPassword.length() > 30) {
                sendRunFunction(player, DragonAuthMeguiName, "密码位数错误");
                return;
            }
            authMeApi.changePassword(player.getName(), newPassword);
            player.sendMessage(Lang.success("密码修改成功"));
            DragonAuthMe.in().getServer().getScheduler().runTaskLater(DragonAuthMe.in(), () -> authMeApi.forceLogin(player), 2L);
            player.closeInventory();
        }
        if ("registerWithoutEmail".equals(action)) {
            //邮箱功能关闭则直接注册，跳过验证码的验证
            if (!DragonAuthMe.in().getConfig().getBoolean("Email.enable")) {
//                player.sendMessage(Lang.success("账号注册成功"));
                EmailMain.forcePlayerToRegister(player.getPlayer(), password);
                DragonAuthMe.in().getServer().getScheduler().runTaskLater(DragonAuthMe.in(), () -> authMeApi.forceLogin(player), 2L);
                player.closeInventory();
                return;
            }
        }
        Pattern pattern = Pattern.compile(ConfigManager.getSettingRegx());
        if ("getCode".equals(action)) {
            //检查邮箱地址是否合法
            emailAddress = data.get(2);
            if (pattern.matcher(emailAddress).matches()) {
                //将生成的验证码存储在code变量中
                code = GetVerificationCode.get(player, emailAddress);
            } else {
                DragonData.sendMessage(player.getPlayer(), "邮箱地址不合法");
            }
            return;
        }
        if ("register".equals(action)) {
            String playerCode = data.get(3);

            if (isEmailUsed(emailAddress)) {
                DragonData.sendMessage(player, "该邮箱已经被其他玩家绑定...");
                return;
            }

            //如果code为空，发送一条信息告诉玩家，请先获取验证码  return
            if (code == null) {
                DragonData.sendMessage(player.getPlayer(), "请先获取验证码");
                return;
            }
            if (code.equals(playerCode)) {
//                DataManager.chat.put(player.getName(), code + "-Bind-" + emailAddress);
                //绑定邮箱的方法.
                EmailMain.addEmail(player.getPlayer(), emailAddress);
                //邮箱绑定之后 再执行下列方法 调用AuthMe的api注册账号
                EmailMain.forcePlayerToRegister(player.getPlayer(), password);
//                player.sendMessage(Lang.success("账号注册成功"));
                //把倒计时变量设置为0
                DragonData.sendCodeCountdown(player.getPlayer(), 0);
                DragonAuthMe.in().getServer().getScheduler().runTaskLater(DragonAuthMe.in(), () -> authMeApi.forceLogin(player), 2L);
                player.closeInventory();
            } else {
                DragonData.sendMessage(player.getPlayer(), "验证码错误");
            }
            return;
        }
        player.closeInventory();
    }

    /*
      玩家加入事件
      当玩家加入时延时执行openGui方法打开龙核页面
     */
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
        }.runTaskLater(DragonAuthMe.in(), 10);
    }

    /*
      打开龙核Gui的方法
      发送数据包给龙核模组
     */
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
        }.runTaskLater(DragonAuthMe.in(), 20);
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
