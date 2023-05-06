package ltd.dreamcraft.www.command.admincommand.subcommands;

import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.CommandSender;

public class AdminViewEmail extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Admin")) {
            if (strings[0].equalsIgnoreCase("View")) {
                if (strings.length == 2) {
                    EmailMain emailMain = new EmailMain(strings[1]);
                    sender.sendMessage(Lang.success("玩家 " + strings[1] + " 的邮箱地址是 " + emailMain.getEmailAddress()));
                    sender.sendMessage(Lang.success("玩家 " + strings[1] + " 邮箱最后修改时间是 " + emailMain.getDate()));
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMeAdmin View <玩家名称>"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return true;
    }
}
