package me.r5t0neer.mcp.sc.net;

import me.r5t0neer.mcp.sc.net.exc.RedisAuthException;

import java.util.HashMap;
import java.util.Map;

public class RedisPublishClient
{
    private RedisClient client;
    private Map<String, RedisChannel> channels;

    public RedisPublishClient(RedisCredentials credentials) throws RedisAuthException
    {
        loadClient(credentials);

        channels = new HashMap<>();
    }

    public RedisChannel createChannel(String name)
    {
        RedisChannel ch = new RedisChannel(name, client);

        channels.put(name, ch);

        return ch;
    }

    private void loadClient(RedisCredentials credentials) throws RedisAuthException
    {
        client = new RedisClient(credentials);
        client.jedis.clientSetname("r5StaffChat_publisher");
    }

    public void reload(RedisCredentials credentials) throws RedisAuthException
    {
        loadClient(credentials);

        // then reconnect all channels

        for(RedisChannel ch : channels.values())
            ch.reload(credentials);
    }
}
