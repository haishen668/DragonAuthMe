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

public class PlayerBindEmailAddress extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Player")) {
            if (strings[0].equalsIgnoreCase("Bind")) {
                if (strings.length == 2) {
                    EmailMain emailMain = new EmailMain(sender.getName());
                    if (emailMain.hasEmailAddress()) {
                        sender.sendMessage(Lang.warn("你已经绑定过邮箱了,无需再次绑定..."));
                    } else {
                        Pattern pattern = Pattern.compile(ConfigManager.getSettingRegx());
                        if (pattern.matcher(strings[1]).matches()) {
                            String code = CreateCode.getCode();
                            SendEmail.send(strings[1], code);
                            DataManager.chat.put(sender.getName(), code + "-Bind-" + strings[1]);
                            sender.sendMessage(Lang.success("验证码已经成功发送至你的邮箱..."));
                            sender.sendMessage(Lang.success("请打开聊天栏输入您的验证码, 输入 cancel 取消"));
                        } else {
                            sender.sendMessage(Lang.error("邮箱地址不合法,请重新输入..."));
                        }
                    }
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMe Bind <邮箱地址>"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return true;
    }
}
