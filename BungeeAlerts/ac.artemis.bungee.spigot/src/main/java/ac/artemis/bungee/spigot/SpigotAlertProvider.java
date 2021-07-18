package ac.artemis.bungee.spigot;

import ac.artemis.anticheat.api.ArtemisServerClient;
import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.anticheat.api.alert.Severity;
import ac.artemis.anticheat.api.listener.VerboseListener;
import ac.artemis.bungee.spigot.utils.Chat;
import ac.artemis.bungeealerts.BungeeAlert;
import ac.artemis.bungeealerts.BungeeProvider;
import ac.artemis.bungeealerts.pipeline.AlertPipeline;
import ac.artemis.bungeealerts.provider.AlertProvider;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SpigotAlertProvider implements AlertProvider {
    private final Cache<UUID, Severity> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    private final ArtemisProvider provider;

    public SpigotAlertProvider(ArtemisProvider provider) {
        this.provider = provider;
    }

    @Override
    public void receiveAlert(BungeeAlert alert) {
        if (provider.getName().equalsIgnoreCase(alert.getServer()))
            return;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            final UUID uuid = onlinePlayer.getUniqueId();
            final Severity severity;

            if (cache.asMap().containsKey(uuid)) {
                severity = cache.getIfPresent(uuid);
            } else {
                severity = ArtemisServerClient.getAPI().getAlertType(uuid);
                cache.put(uuid, severity == null ? Severity.NONE : severity);
            }

            final BungeeAlert.MessageAlert messageAlert = alert.getAlert();

            if (severity.ordinal() >= messageAlert.getSeverity().ordinal()) {
                onlinePlayer.sendMessage(Chat.translate("&7(" + alert.getServer() + ") " + alert.getAlert().toMinecraftMessage()));
            }
        }
    }
}
