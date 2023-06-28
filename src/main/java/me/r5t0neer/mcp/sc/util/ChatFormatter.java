package me.r5t0neer.mcp.sc.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.awt.*;
import java.util.PrimitiveIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ChatFormatter
{
    public static String colorize(String message)
    {
        // 1. Hex colors

        String hexPattern = "&<#([0-9A-Fa-f]{6})>";
        Pattern hexRegex = Pattern.compile(hexPattern);
        Matcher hexMatcher = hexRegex.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (hexMatcher.find())
        {
            String colorCode = hexMatcher.group(1);
            String replacement = "§x§" + colorCode.charAt(0) + "§" + colorCode.charAt(1) +
                    "§" + colorCode.charAt(2) + "§" + colorCode.charAt(3) +
                    "§" + colorCode.charAt(4) + "§" + colorCode.charAt(5);
            hexMatcher.appendReplacement(buffer, replacement);
        }

        hexMatcher.appendTail(buffer);
        String output = buffer.toString();

        // 2. Legacy colors

        String colorPattern = "&([0-9a-fk-or])";
        Pattern colorRegex = Pattern.compile(colorPattern);
        Matcher colorMatcher = colorRegex.matcher(output);
        StringBuffer colorBuffer = new StringBuffer();

        while (colorMatcher.find())
        {
            String colorCode = colorMatcher.group(1);
            String replacement = "§" + colorCode;
            colorMatcher.appendReplacement(colorBuffer, replacement);
        }

        colorMatcher.appendTail(colorBuffer);
        return colorBuffer.toString();
    }

    public static String trimColors(String colorizedMessage)
    {
        StringBuilder messageWithoutColors = new StringBuilder();

        boolean color = false;

        for(int i=0; i < colorizedMessage.length(); ++i)
        {
            char ch = colorizedMessage.charAt(i);

            if(ch == '§') color = true;
            else if(color) color = false;
            else
            {
                messageWithoutColors.append(ch);
            }
        }

        return messageWithoutColors.toString();
    }
}
