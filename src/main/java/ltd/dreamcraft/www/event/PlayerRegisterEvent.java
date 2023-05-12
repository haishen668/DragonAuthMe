package ltd.dreamcraft.www.event;

import fr.xephi.authme.events.AuthMeAsyncPreRegisterEvent;
import ltd.dreamcraft.www.tools.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


@Deprecated
public class PlayerRegisterEvent implements Listener {
    @EventHandler
    public void onPlayerRegister(AuthMeAsyncPreRegisterEvent event) {
        // 取消注册事件
        event.setCanRegister(false);
        // 给玩家发送一个错误信息
        event.getPlayer().sendMessage(Lang.error("AuthMe插件注册功能已禁用"));
    }
}
