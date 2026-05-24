package top.itsglobally.CircleFramework.listener.premade.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import top.itsglobally.CircleFramework.util.LuckPermsUtil;
import top.itsglobally.CircleFramework.util.MsgUtil;


/***
 * 這個Listener會將所有聊天格式都變成
 * Player: Message
 */
public class WithoutPrefixChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Component comp = Component.text(event.getPlayer().getName())
                .append(MsgUtil.color(" &r» "));


        String msg = LegacyComponentSerializer.legacySection().serialize(comp) + event.getMessage();

        event.setFormat(msg.replace("%", "%%"));
    }
}
