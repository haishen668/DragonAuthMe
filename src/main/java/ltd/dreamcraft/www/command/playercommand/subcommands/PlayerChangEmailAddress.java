package ltd.dreamcraft.www.command.playercommand.subcommands;

import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.Manager.ConfigManager;
import ltd.dreamcraft.www.Manager.DataManager;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.CreateCode;
import ltd.dreamcraft.www.tools.Lang;
import ltd.dreamcraft.www.tools.SendEmail;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public class PlayerChangEmailAddress extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Player")) {
            if (strings[0].equalsIgnoreCase("ChangeEmail")) {
                if (strings.length == 2) {
                    EmailMain emailMain = new EmailMain(sender.getName());
                    if (emailMain.hasEmailAddress()) {
                        Pattern pattern = Pattern.compile(ConfigManager.getSettingRegx());
                        if (pattern.matcher(strings[1]).matches()) {
                            String code = CreateCode.getCode();
                            SendEmail.send(emailMain.getEmailAddress(), code);
                            DataManager.chat.put(sender.getName(), code + "-ChangeEmail-" + strings[1]);
                            sender.sendMessage(Lang.success("验证码已经成功发送至你的原邮箱..."));
                            sender.sendMessage(Lang.success("请打开聊天栏输入您的验证码 输入 cancel 取消"));
                        } else {
                            sender.sendMessage(Lang.error("邮箱地址不合法,请重新输入..."));
                        }
                    } else {
                        sender.sendMessage(Lang.warn("你还没有绑定过邮箱,无法修改..."));
                    }
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMe ChangeEmail <新的邮箱地址>"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return true;
    }
}
