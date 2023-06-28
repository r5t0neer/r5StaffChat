package me.r5t0neer.mcp.sc.msg;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageProducer
{
    private final MessageBus bus;
    protected Queue<Message> msgQueue = new ArrayDeque<>();

    public MessageProducer(MessageBus bus)
    {
        this.bus = bus;
    }

    public void produce(Message msg)
    {
        msgQueue.add(msg);
        bus.consume(this);
    }
}
