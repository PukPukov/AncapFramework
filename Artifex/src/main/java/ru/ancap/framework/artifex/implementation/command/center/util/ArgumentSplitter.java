package ru.ancap.framework.artifex.implementation.command.center.util;

import ru.ancap.commons.parse.EscapingBuffer;

import java.util.ArrayList;
import java.util.List;

public class ArgumentSplitter {
    
    public static List<String> split(String command) {
        List<String> arguments = new ArrayList<>();
        EscapingBuffer escapingBuffer = new EscapingBuffer();
        StringBuilder currentArgument = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < command.length(); i++) {
            char currentChar = command.charAt(i);
            escapingBuffer.step();
            
            if (escapingBuffer.currentlyEscaped()) currentArgument.append(currentChar);
            else {
                switch (currentChar) {
                    case '\\' -> escapingBuffer.escapeNext();
                    case '"' -> inQuotes = !inQuotes;
                    case ' ' -> {
                        if (inQuotes) currentArgument.append(currentChar);
                        else {
                            if (!currentArgument.isEmpty()) {
                                arguments.add(currentArgument.toString());
                                currentArgument.setLength(0);
                            }
                        }
                    }
                    default -> currentArgument.append(currentChar);
                }
            }
        }
        
        if (!currentArgument.isEmpty()) arguments.add(currentArgument.toString());
        
        return arguments;
    }
    
}