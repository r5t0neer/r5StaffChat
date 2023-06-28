package me.r5t0neer.mcp.sc.net;

import java.util.HashSet;
import java.util.Set;

public class RedisChannel
{
    private final String name;
    private final RedisClient publishClient;
    private final Set<Listener> listeners;

    public RedisChannel(String name, RedisClient publishClient)
    {
        this.name = name;
        this.publishClient = publishClient;
        this.listeners = new HashSet<>();
    }

    public void registerListener(AMessageBroker broker, RedisCredentials credentials)
    {
        int id = listeners.size();

        Thread th = new Thread(() -> {
            try
            {
                RedisClient client = new RedisClient(credentials);
                client.jedis.clientSetname("r5StaffChat_" + name + "_sub" + id);

                client.jedis.subscribe(broker, name);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        });

        listeners.add(new Listener(id, broker, th));

        th.start();
    }

    public void sendMessage(String message)
    {
        publishClient.jedis.publish(name, message);
    }

    public void reload(RedisCredentials credentials)
    {
        for(Listener lst : listeners)
        {
            lst.restart(credentials, name);
        }
    }

    static class Listener
    {
        int id;
        AMessageBroker broker;
        Thread thread;

        Listener(int id, AMessageBroker broker, Thread thread)
        {
            this.id = id;
            this.broker = broker;
            this.thread = thread;
        }

        void restart(RedisCredentials credentials, String channelName)
        {
            try { thread.stop(); }
            catch(Exception e) { e.printStackTrace(); }

            thread = new Thread(() -> {
                try
                {
                    RedisClient client = new RedisClient(credentials);
                    client.jedis.clientSetname("r5StaffChat_" + channelName + "_sub" + id);

                    client.jedis.subscribe(broker, channelName);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            });

            thread.start();
        }
    }

    public void onDisable()
    {
        for(Listener lst : listeners)
        {
            try { lst.thread.stop(); }
            catch(Exception e) { e.printStackTrace(); }
        }
    }
}
