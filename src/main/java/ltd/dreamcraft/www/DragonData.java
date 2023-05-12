package ltd.dreamcraft.www;

import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class DragonData {
    Map<String, String> data = new LinkedHashMap<>();

    public void add(String a, Object b) {
        this.data.put(a, String.valueOf(b));
    }

    public void remove(String key) {
        this.data.remove(key);
    }

    public void sendToPlayer(Player player) {
        PacketSender.sendSyncPlaceholder(player, this.data);
    }


    public static void sendMessage(Player player, String message) {
        DragonData dragonData = new DragonData();
        dragonData.add("dragonauthme_tip_time", 5);
        dragonData.add("dragonauthme_tip_broadcast", message);
        dragonData.sendToPlayer(player);
    }
    public static void sendCodeCountdown(Player player,Object time) {
        DragonData dragonData = new DragonData();
        dragonData.add("code_countdown", time);
        dragonData.sendToPlayer(player);

    }
}

