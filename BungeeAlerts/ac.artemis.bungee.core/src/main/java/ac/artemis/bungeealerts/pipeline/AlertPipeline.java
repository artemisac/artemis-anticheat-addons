package ac.artemis.bungeealerts.pipeline;

import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.bungeealerts.BungeeAlert;

public interface AlertPipeline {
    void start();
    void stop();
    void shareAlert(final BungeeAlert alert);
}
