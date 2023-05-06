package ltd.dreamcraft.www.command.playercommand.subcommands;

import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.CommandSender;

public class PlayerView extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Player")) {
            if (strings[0].equalsIgnoreCase("View")) {
                if (strings.length == 1) {
                    EmailMain emailMain = new EmailMain(sender.getName());
                    if (emailMain.hasEmailAddress()) {
                        sender.sendMessage(Lang.success("你的邮箱地址为: " + emailMain.getEmailAddress()));
                    } else {
                        sender.sendMessage(Lang.error("你还没有绑定过邮箱..."));
                    }
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMe View"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return true;
    }
}
