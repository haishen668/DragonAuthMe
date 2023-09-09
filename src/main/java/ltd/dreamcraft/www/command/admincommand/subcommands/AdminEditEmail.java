package ltd.dreamcraft.www.command.admincommand.subcommands;

import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.CommandSender;

public class AdminEditEmail extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
            if (strings[0].equalsIgnoreCase("Edit")) {
                if (sender.hasPermission("DragonAuthMe.Edit") || sender.hasPermission("DragonAuthMe.Admin")) {
                    if (strings.length == 3) {
                        EmailMain.editEmail(sender, strings[1], strings[2]);
                    } else {
                        sender.sendMessage(Lang.warn("正确用法: /DragonAuthMeAdmin Edit <玩家名称> <邮箱地址>"));
                    }
                } else {
                    sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
                }
            }
        return true;
    }
}
