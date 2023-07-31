package ltd.dreamcraft.www.command.admincommand.subcommands;

import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.CommandSender;

public class AdminReloadConfig extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Admin")) {
            if (strings[0].equalsIgnoreCase("Reload")) {
                //重载的方法
                ConfigManager.reloadConfig();
                sender.sendMessage(Lang.success("配置文件 config.yml 重载成功..."));
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }
        return true;
    }
}
