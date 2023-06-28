package me.r5t0neer.mcp.sc.net;

import me.r5t0neer.mcp.sc.cfg.ConfigManager;
import redis.clients.jedis.JedisPubSub;

public abstract class AMessageBroker extends JedisPubSub {}
