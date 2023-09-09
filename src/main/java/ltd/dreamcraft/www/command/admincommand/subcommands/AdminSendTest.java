package ltd.dreamcraft.www.command.admincommand.subcommands;

import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import ltd.dreamcraft.www.tools.SendEmail;
import org.bukkit.command.CommandSender;

public class AdminSendTest extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
            if (strings[0].equalsIgnoreCase("SendTest")) {
                if (sender.hasPermission("DragonAuthMe.SendTest") || sender.hasPermission("DragonAuthMe.Admin")) {
                    if (strings.length == 2) {
                        SendEmail.send(strings[1], "XXXXXX");
                        sender.sendMessage(Lang.success("发送成功..."));
                    } else {
                        sender.sendMessage(Lang.warn("正确用法: /DragonAuthMeAdmin SendTest <邮箱地址>"));
                    }
                } else {
                    sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
                }
            }
        return true;
    }
}