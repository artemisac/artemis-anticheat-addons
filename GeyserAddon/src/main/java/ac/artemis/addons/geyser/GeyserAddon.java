package ac.artemis.addons.geyser;

import ac.artemis.anticheat.api.ArtemisServerClient;
import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.anticheat.api.listener.VerboseListener;
import cc.ghast.packet.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.FloodgateAPI;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GeyserAddon extends JavaPlugin {

    private final Set<UUID> uuidSet = new HashSet<>();

    @Override
    public void onEnable() {
        System.out.println(Chat.translate("&r[&bArtemis&r] &bInitializing Geyser addon!"));
        // Plugin startup logic
        ArtemisServerClient.getAPI().addVerboseListener(new VerboseListener() {
            @Override
            public void receive(Alert alert) {
                if (uuidSet.contains(alert.getUuid())) {
                    alert.setCancelled(true);
                }
            }
        });

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            void onJoin(final PlayerJoinEvent event) {
                if (FloodgateAPI.isBedrockPlayer(event.getPlayer())) {
                    System.out.println(Chat.translate("&r[&bArtemis&r] &bExempting player of username "
                            + event.getPlayer().getName() + " due to Geyser connection!"));
                    uuidSet.add(event.getPlayer().getUniqueId());
                }
            }

            @EventHandler
            void onLeave(final PlayerQuitEvent event) {
                uuidSet.remove(event.getPlayer().getUniqueId());
            }
        }, this);

        System.out.println(Chat.translate("&r[&bArtemis&r] &bSuccessfully initialized Geyser addon!"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
