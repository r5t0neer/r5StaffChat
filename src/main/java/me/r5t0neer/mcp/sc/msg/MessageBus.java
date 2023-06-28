package me.r5t0neer.mcp.sc.msg;

import me.r5t0neer.mcp.sc.cfg.ConfigManager;
import me.r5t0neer.mcp.sc.net.AMessageBroker;
import me.r5t0neer.mcp.sc.net.RedisChannel;
import me.r5t0neer.mcp.sc.net.RedisCredentials;
import me.r5t0neer.mcp.sc.net.RedisPublishClient;
import me.r5t0neer.mcp.sc.net.exc.RedisAuthException;

import java.util.ArrayList;
import java.util.List;

public class MessageBus
{
    private final ConfigManager configs;
    private RedisPublishClient redisPublishClient;
    private RedisChannel redisChannel;
    private final List<IMessageConsumer> consumers;

    public MessageBus(ConfigManager configs) throws RedisAuthException
    {
        this.configs = configs;
        loadRedis();

        consumers = new ArrayList<>();
    }

    public void registerConsumer(IMessageConsumer consumer)
    {
        consumers.add(consumer);
    }

    // assuming message queue size is 1
    protected void consume(MessageProducer producer)
    {
        Message ongoingMsg = producer.msgQueue.poll();

        if(ongoingMsg == null)
            return;

        redisChannel.sendMessage(ongoingMsg.encode());
    }

    private void loadRedis() throws RedisAuthException
    {
        redisPublishClient = new RedisPublishClient(configs.getPrimaryConfig().redisCredentials);
        redisChannel = redisPublishClient.createChannel(configs.getPrimaryConfig().redisChannel);

        redisChannel.registerListener(new AMessageBroker()
        {
            @Override
            public void onMessage(String channel, String message)
            {
                for(IMessageConsumer consumer : consumers)
                {
                    try
                    {
                        consumer.onMessage(Message.decode(message));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, configs.getPrimaryConfig().redisCredentials);
    }

    public void reload(RedisCredentials credentials) throws RedisAuthException
    {
        redisPublishClient.reload(credentials);
    }

    public void onDisable()
    {
        redisChannel.onDisable();
    }
}
