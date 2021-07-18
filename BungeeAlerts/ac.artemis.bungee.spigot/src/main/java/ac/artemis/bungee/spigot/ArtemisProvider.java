package ac.artemis.bungee.spigot;

import ac.artemis.anticheat.api.ArtemisServerClient;
import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.anticheat.api.listener.VerboseListener;
import ac.artemis.bungee.spigot.utils.Configuration;
import ac.artemis.bungeealerts.BungeeAlert;
import ac.artemis.bungeealerts.BungeeProvider;
import ac.artemis.bungeealerts.pipeline.AlertPipeline;
import ac.artemis.bungeealerts.pipeline.impl.RedisAlertPipeline;
import ac.artemis.bungeealerts.provider.AlertProvider;
import ac.artemis.bungeealerts.utils.Config;

public class ArtemisProvider implements BungeeProvider {
    private final SpigotPlugin spigotPlugin;

    public ArtemisProvider(SpigotPlugin spigotPlugin) {
        this.spigotPlugin = spigotPlugin;
    }

    private Configuration configuration;
    private VerboseListener verboseListener;
    private AlertProvider provider;
    private AlertPipeline pipeline;
    private String name;

    public void init() {
        // Plugin startup logic
        this.configuration = new Configuration("redis.yml", spigotPlugin);
        this.provider = new SpigotAlertProvider(this);
        this.pipeline = new RedisAlertPipeline(this);
        pipeline.start();

        name = this.configuration.getString("name");

        this.verboseListener = new VerboseListener() {
            @Override
            public void receive(Alert alert) {
                pipeline.shareAlert(new BungeeAlert(name, alert));
            }
        };

        ArtemisServerClient.getAPI().addVerboseListener(verboseListener);
    }

    public void end() {
        pipeline.stop();
        // Plugin shutdown logic
        if (ArtemisServerClient.getAPI() == null)
            return;

        ArtemisServerClient.getAPI().removeVerboseListener(verboseListener);
    }

    @Override
    public AlertProvider provider() {
        return provider;
    }

    @Override
    public Config getConfig(String name) {
        return configuration;
    }

    @Override
    public String getName() {
        return name;
    }
}
