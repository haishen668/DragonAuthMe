package ltd.dreamcraft.www.command.playercommand;

import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.command.playercommand.subcommands.PlayerView;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements TabExecutor {
    private final List<SubCommand> subcommands = new ArrayList();

    public void help(CommandSender sender) {
/*
        sender.sendMessage("§c§l§oDragon§f§l§oAuthMe §f| §d玩家命令§6§l>>>");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMe Bind <邮箱地址> §c§o- §f§o绑定邮箱地址");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMe ChangeEmail <邮箱地址> §c§o- §f§o修改邮箱地址");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMe ChangePassword <原密码> <新密码> §c§o- §f§o修改新密码");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMe View §c§o- §f§o查看自己的邮箱地址");
*/
        sender.sendMessage("  §f§lDragonAuthMe[Help] " + DragonAuthMe.in().getDescription().getVersion());
        sender.sendMessage("");
        sender.sendMessage("  §7[命令]: §f/DragonAuthMe §7[...]");
        sender.sendMessage("§7     -§f View");
        sender.sendMessage("§7       查看自己的邮箱地址");
    }

    public PlayerCommand() {
//        this.subcommands.add(new PlayerBindEmailAddress());
//        this.subcommands.add(new PlayerChangEmailAddress());
//        this.subcommands.add(new PlayerChangePassword());
        this.subcommands.add(new PlayerView());
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            for(SubCommand sub : this.subcommands) {
                sub.performCommand(commandSender, strings);
            }
        } else if (commandSender.hasPermission("DragonAuthMe.Player")) {
            this.help(commandSender);
        } else {
            commandSender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return false;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            List<String> list = new ArrayList();
//            list.add("ChangeEmail");
//            list.add("ChangePassword");
//            list.add("Bind");
            list.add("View");
            return list;
        } else {
            return null;
        }
    }
}