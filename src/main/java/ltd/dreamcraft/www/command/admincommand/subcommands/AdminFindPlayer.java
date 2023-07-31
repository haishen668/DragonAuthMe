package ltd.dreamcraft.www.command.admincommand.subcommands;

import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.CommandSender;

public class AdminFindPlayer extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Admin")) {
            if (strings[0].equalsIgnoreCase("Find")) {
                if (strings.length == 2) {
                    String playerName = EmailMain.findPlayerIDByEmail(strings[1]);
                    if (playerName == null) {
                        sender.sendMessage(Lang.error("这个邮箱没有被任何玩家绑定"));
                    } else {
                        sender.sendMessage(Lang.success("绑定邮箱 &e" + strings[1] + " &7的玩家是 &a" + playerName));
                    }
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMeAdmin Find <邮箱地址>"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }
        return true;
    }
}


