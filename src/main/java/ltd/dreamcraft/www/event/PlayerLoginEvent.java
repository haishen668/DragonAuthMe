package ltd.dreamcraft.www.event;

import eos.moe.dragoncore.network.PacketSender;
import fr.xephi.authme.events.LoginEvent;
import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.tools.CheckPluginUpdate;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLoginEvent implements Listener {
    //监听玩家登录事件，实现强制绑定邮箱和告诉管理员是否需要更新，给予一个权限可以忽视强制绑定邮箱
    @EventHandler
    public void afterPlayerLogin(LoginEvent event) {
        //检测玩家是否是op，如果是就发送更新提示
        if (event.getPlayer().isOp() && ConfigManager.getAdminUpdatePrompts()) {
            String[] data = CheckPluginUpdate.checkPluginUpdate("https://pluginapi.dreamcraft.ltd/api/plugins/").split("&");
            if (data.length > 1) {
                event.getPlayer().sendMessage(Lang.prefix("&c发现新版本: " + data[0] + "&7(&a可在配置文件中关闭更新检查&7)"));
                event.getPlayer().sendMessage(Lang.prefix("&c新版本下载地址: " + data[1]));
                String[] broadcasts = data[2].split("\n");
                event.getPlayer().sendMessage(Lang.prefix("┏━━━━━━━━━ 更新日志 ━━━━━━━━━"));
                for (String message : broadcasts) {
                    event.getPlayer().sendMessage(Lang.prefix("┃ " + message.replaceAll("&", "§")));
                }
                event.getPlayer().sendMessage(Lang.prefix("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
            }
        }
        //如果邮箱功能打开且邮箱强制绑定功能也打开那么就绑定邮箱
        if (ConfigManager.getEmailEnable() && ConfigManager.getForceBindEmail()) {
            //如果有权限就不需要绑定邮箱，没有权限就打开龙核gui
            if (!event.getPlayer().hasPermission("DragonAuthMe.ignore")) {
                if (!EmailMain.isPlayerBoundToEmail(event.getPlayer().getName())) {
                    PacketSender.sendOpenGui(event.getPlayer(), "DragonBindEmail");
                }
            }
        }
    }
}
