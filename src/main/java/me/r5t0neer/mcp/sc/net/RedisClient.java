package me.r5t0neer.mcp.sc.net;

import me.r5t0neer.mcp.sc.net.exc.RedisAuthException;
import redis.clients.jedis.Jedis;

public class RedisClient
{
    public Jedis jedis;

    public RedisClient(RedisCredentials credentials) throws RedisAuthException
    {
        jedis = new Jedis(credentials.host(), credentials.port());

        String reply = "";

        if(!credentials.user().isBlank())
        {
            reply = jedis.auth(credentials.user(), credentials.password());
        }
        else if(!credentials.password().isEmpty())
        {
            reply = jedis.auth(credentials.password());
        }

        if(!reply.isEmpty() && reply.charAt(0) != '+')
        {
            throw new RedisAuthException(reply);
        }
    }
}
