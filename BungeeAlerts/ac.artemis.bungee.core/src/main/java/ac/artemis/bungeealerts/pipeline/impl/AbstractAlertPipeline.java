package ac.artemis.bungeealerts.pipeline.impl;

import ac.artemis.bungeealerts.BungeeProvider;
import ac.artemis.bungeealerts.pipeline.AlertPipeline;

public abstract class AbstractAlertPipeline implements AlertPipeline {
    protected final BungeeProvider plugin;

    public AbstractAlertPipeline(BungeeProvider plugin) {
        this.plugin = plugin;
    }
}
