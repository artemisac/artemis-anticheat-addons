package ac.artemis.bungee.spigot.messaging;

import ac.artemis.bungee.spigot.SpigotPlugin;
import ac.artemis.bungee.spigot.utils.Tool;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeNameGetter implements PluginMessageListener, Listener, Tool {
    private final SpigotPlugin plugin;
    private boolean executed;
    private Player expected;

    public BungeeNameGetter(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    @Override
    public void end() {
        Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
        Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");

        this.executed = false;
        this.expected = null;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (expected != null || executed)
            return;

        final Player player = event.getPlayer();

        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

        this.expected = event.getPlayer();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        if (!event.getPlayer().equals(expected) || executed)
            return;

        this.expected = null;
        this.executed = false;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        if (!player.equals(expected))
            return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();
        if(!subchannel.equals("GetServer")){
            return;
        }

        final String name = in.readUTF();

        plugin.getArtemisProvider().setName(name);

        this.expected = null;
        this.executed = true;
    }
}
