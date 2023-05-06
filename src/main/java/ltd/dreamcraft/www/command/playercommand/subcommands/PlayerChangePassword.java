package ltd.dreamcraft.www.command.playercommand.subcommands;

import fr.xephi.authme.api.v3.AuthMeApi;
import ltd.dreamcraft.www.EmailMain;
import ltd.dreamcraft.www.Manager.DataManager;
import ltd.dreamcraft.www.command.SubCommand;
import ltd.dreamcraft.www.tools.CreateCode;
import ltd.dreamcraft.www.tools.Lang;
import ltd.dreamcraft.www.tools.SendEmail;
import org.bukkit.command.CommandSender;

public class PlayerChangePassword extends SubCommand {
    public boolean performCommand(CommandSender sender, String[] strings) {
        if (sender.hasPermission("DragonAuthMe.Player")) {
            if (strings[0].equalsIgnoreCase("ChangePassword")) {
                if (strings.length == 3) {
                    EmailMain emailMain = new EmailMain(sender.getName());
                    if (emailMain.hasEmailAddress()) {
                        AuthMeApi api = AuthMeApi.getInstance();
                        if (api.checkPassword(sender.getName(), strings[1])) {
                            String code = CreateCode.getCode();
                            SendEmail.send(emailMain.getEmailAddress(), code);
                            DataManager.chat.put(sender.getName(), code + "-ChangePassword-" + strings[2]);
                            sender.sendMessage(Lang.success("验证码已经成功发送至你的邮箱..."));
                            sender.sendMessage(Lang.success("请打开聊天栏输入您的验证码 输入 cancel 取消"));
                        } else {
                            sender.sendMessage(Lang.warn("原密码输入错误,请重新输入..."));
                        }
                    } else {
                        sender.sendMessage(Lang.warn("你还没有绑定过邮箱,无法修改..."));
                    }
                } else {
                    sender.sendMessage(Lang.warn("正确用法: /DragonAuthMe ChangePassword <原密码> <新密码>"));
                }
            }
        } else {
            sender.sendMessage(Lang.error("你没有权限执行这个指令..."));
        }

        return true;
    }
}