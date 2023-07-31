package ltd.dreamcraft.www.command.admincommand;

import ltd.dreamcraft.www.DragonAuthMe;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.command.admincommand.subcommands.*;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements TabExecutor {
    private final List<SubCommand> subcommands = new ArrayList();

    public void help(CommandSender sender) {
/*
        sender.sendMessage("§c§l§oDragon§f§l§oAuthMe §f| §d管理员命令§6§l>>>");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMeAdmin Add <玩家名称> <邮箱地址> §c§o- §f§o为一个玩家添加邮箱地址");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMeAdmin Edit <玩家名称> <邮箱地址> §c§o- §f§o修改一个玩家的邮箱地址");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMeAdmin Delete <玩家名称> §c§o- §f§o删除一个玩家的邮箱地址");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMeAdmin View <玩家名称> §c§o- §f§o查看某个玩家的邮箱地址");
        sender.sendMessage("§b§k|§b- §7§o/DragonAuthMeAdmin SendTest <邮箱地址> §c§o- §f§o发送一封测试邮件");
        sender.sendMessage("§b§k|§b- §7§o[§c§o!§7§o] Add 和 Delete 会直接操纵玩家数据,慎用...");
*/
        sender.sendMessage("  §f§lDragonAuthMeAdmin[Help] " + DragonAuthMe.in().getDescription().getVersion());
        sender.sendMessage("");
        sender.sendMessage("  §7[命令]: §f/DragonAuthMe §7[...]");
        sender.sendMessage("§7     -§f Add <玩家名称> <邮箱地址>");
        sender.sendMessage("§7       添加玩家邮箱");
        sender.sendMessage("§7     -§f Edit <玩家名称> <邮箱地址>");
        sender.sendMessage("§7       修改玩家邮箱");
        sender.sendMessage("§7     -§f Delete <玩家名称>");
        sender.sendMessage("§7       删除玩家邮箱");
        sender.sendMessage("§7     -§f View <玩家名称>");
        sender.sendMessage("§7       查看玩家邮箱地址");
        sender.sendMessage("§7     -§f Find <邮箱地址>");
        sender.sendMessage("§7       查询绑定邮箱的玩家");
        sender.sendMessage("§7     -§f SendTest <邮箱地址>");
        sender.sendMessage("§7       发送一封测试邮件");
        sender.sendMessage("§7     -§f Reload");
        sender.sendMessage("§7       重新加载配置文件");
    }

    public AdminCommand() {
        this.subcommands.add(new AdminAddEmail());
        this.subcommands.add(new AdminDeleteEmail());
        this.subcommands.add(new AdminEditEmail());
        this.subcommands.add(new AdminViewEmail());
        this.subcommands.add(new AdminFindPlayer());
        this.subcommands.add(new AdminSendTest());
        this.subcommands.add(new AdminReloadConfig());
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            for(SubCommand sub : this.subcommands) {
                sub.performCommand(commandSender, strings);
            }
        } else if (commandSender.hasPermission("DragonAuthMe.Admin")) {
            this.help(commandSender);
        } else {
            commandSender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return false;
    }

    //命令自动补全
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            List<String> list = new ArrayList();
            list.add("Add");
            list.add("Edit");
            list.add("Delete");
            list.add("View");
            list.add("Bind");
            list.add("SendTest");
            list.add("Reload");
            return list;
        } else {
            return null;
        }
    }

}