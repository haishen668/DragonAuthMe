package ltd.dreamcraft.www.command.admincommand.subcommands;

import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.CommandSender;

public class AdminDeleteEmail extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Admin")) {
            if (strings[0].equalsIgnoreCase("Delete")) {
                if (strings.length == 2) {
                    EmailMain.deleteEmail(sender, strings[1]);
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMeAdmin Delete <玩家名称>"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return true;
    }
}
