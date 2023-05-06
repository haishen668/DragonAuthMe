package ltd.dreamcraft.www.tools;

import ltd.dreamcraft.www.DragonData;
import ltd.dreamcraft.www.Manager.ConfigManager;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;


//该方法可以 生成一个随机验证码并且发送邮件给制定邮箱，并且返回一个String的验证码
public class GetVerificationCode {
    public static String get(Player player, String emailAddress) {
        Pattern pattern = Pattern.compile(ConfigManager.getSettingRegx());
        //判断邮箱内容是否符合表达式
        if (pattern.matcher(emailAddress).matches()) {
            String code = CreateCode.getCode();
            SendEmail.send(emailAddress, code);
            DragonData.sendMessage(player.getPlayer(),"验证码已发送");
            //发送倒计时变量给客户端
            DragonData.sendCodeCountdown(player.getPlayer(),60);
            return code;
        } else {
            //不符合邮箱表达式
            DragonData.sendMessage(player.getPlayer(),"邮箱地址不合法");
        }
        return null;
    }

}