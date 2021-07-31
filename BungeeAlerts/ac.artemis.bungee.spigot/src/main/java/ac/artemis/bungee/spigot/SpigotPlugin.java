package ac.artemis.bungee.spigot;

import ac.artemis.bungee.spigot.messaging.BungeeNameGetter;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

public final class SpigotPlugin extends JavaPlugin {

    private ArtemisProvider artemisProvider;
    private BungeeNameGetter bungeeNameGetter;

    @Override
    public void onEnable() {
        artemisProvider = new ArtemisProvider(this);
        artemisProvider.init();

        if (SpigotConfig.bungee) {
            bungeeNameGetter = new BungeeNameGetter(this);
            bungeeNameGetter.init();
        }
    }

    @Override
    public void onDisable() {
        artemisProvider.end();
        artemisProvider = null;

        if (SpigotConfig.bungee) {
            bungeeNameGetter.end();
            bungeeNameGetter = null;
        }
    }

    public ArtemisProvider getArtemisProvider() {
        return artemisProvider;
    }
}
