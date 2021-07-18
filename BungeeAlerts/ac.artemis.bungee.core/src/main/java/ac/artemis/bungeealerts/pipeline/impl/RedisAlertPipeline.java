package ac.artemis.bungeealerts.pipeline.impl;

import ac.artemis.bungeealerts.BungeeAlert;
import ac.artemis.bungeealerts.BungeeProvider;
import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.bungeealerts.messaging.MessageSerializer;
import ac.artemis.bungeealerts.messaging.SimpleBase64Serializer;
import ac.artemis.bungeealerts.utils.Config;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisAlertPipeline extends AbstractAlertPipeline {
    public RedisAlertPipeline(BungeeProvider plugin) {
        super(plugin);
    }

    private final MessageSerializer<BungeeAlert> serializer = new SimpleBase64Serializer();
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final ExecutorService writeService = Executors.newSingleThreadExecutor();
    private JedisPool pool;
    private Jedis outbound;
    private Jedis inbound;

    @Override
    public void start() {
        final Config config = plugin.getConfig("redis.yml");

        final String host = config.getString("connect.host");
        final int port = config.getInt("connect.port");
        final String password = config.getString("connect.password");
        final int timeout = config.getInt("timeout");
        final boolean ssl = config.getBoolean("ssl");

        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.pool = new JedisPool(poolConfig, host, port, timeout, password, ssl);
        this.inbound = pool.getResource();
        this.outbound = pool.getResource();

        service.execute(() -> {
            this.inbound.subscribe(new BinaryJedisPubSub() {
                @Override
                public void onMessage(byte[] channel, byte[] message) {
                    final String name = new String(channel);

                    if (!name.equalsIgnoreCase("artemis-verbose"))
                        return;

                    final BungeeAlert alert = serializer.deserialize(message);

                    plugin.provider().receiveAlert(alert);
                }
            }, "artemis-verbose".getBytes(StandardCharsets.UTF_8));
        });
    }

    @Override
    public void stop() {
        this.inbound.quit();
        this.inbound.disconnect();
        this.outbound.disconnect();
        this.pool.close();
    }

    @Override
    public void shareAlert(BungeeAlert alert) {
        writeService.execute(() -> {
            final byte[] encoded = serializer.serialize(alert);
            outbound.publish("artemis-verbose".getBytes(StandardCharsets.UTF_8), encoded);
        });
    }
}
