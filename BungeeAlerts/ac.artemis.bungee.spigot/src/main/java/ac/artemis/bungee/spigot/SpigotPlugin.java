package ac.artemis.bungee.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlugin extends JavaPlugin {

    private ArtemisProvider artemisProvider;

    @Override
    public void onEnable() {
        artemisProvider = new ArtemisProvider(this);
        artemisProvider.init();
    }

    @Override
    public void onDisable() {
        artemisProvider.end();
        artemisProvider = null;
    }
}
