package me.r5t0neer.mcp.sc.msg;

import me.r5t0neer.mcp.sc.exc.UnreachableException;

public record Message(String serverName, String displayName, String message)
{
    public String encode()
    {
        StringBuilder s = new StringBuilder();

        // length;data

        s.append(serverName.length());
        s.append(';');
        s.append(serverName);

        s.append(displayName.length());
        s.append(';');
        s.append(displayName);

        s.append(message.length());
        s.append(';');
        s.append(message);

        return s.toString();
    }

    public static Message decode(String encoded) throws Exception
    {
        Offset off = new Offset(0);
        int len;

        len = getInt(encoded, off);
        String serverName = readNChars(encoded, off, len);

        len = getInt(encoded, off);
        String playerName = readNChars(encoded, off, len);

        len = getInt(encoded, off);
        String message = readNChars(encoded, off, len);

        return new Message(serverName, playerName, message);
    }

    private static int getInt(String encoded, Offset off) throws Exception
    {
        StringBuilder s = new StringBuilder();

        while(off.idx < encoded.length())
        {
            char ch = encoded.charAt(off.idx);

            off.idx += 1;

            if(ch == ';')
            {
                return Integer.valueOf(s.toString());
            }

            s.append(ch);
        }

        throw new UnreachableException();
    }

    private static String readNChars(String encoded, Offset off, int n)
    {
        StringBuilder s = new StringBuilder();

        while(off.idx < encoded.length() && n > 0)
        {
            s.append(encoded.charAt(off.idx));

            n -= 1;
            off.idx += 1;
        }

        return s.toString();
    }

    static class Offset
    {
        int idx;

        Offset(int off)
        {
            this.idx = off;
        }
    }
}
