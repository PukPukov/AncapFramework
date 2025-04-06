package ru.ancap.framework.artifex.implementation.command.center.util;

import java.util.ArrayList;
import java.util.List;

public class ArgumentSplitter {
    
    public static SplitResult split(String command) {
        List<Part> parts = new ArrayList<>();
        EscapingBuffer escapingBuffer = new EscapingBuffer();
        StringBuilder currentArgument = new StringBuilder();
        StringBuilder currentOriginal = new StringBuilder();
        boolean inQuotes = false;
        int currentStartIndex = 0;
        boolean lastCharWasPlainLetter = false;
        
        for (int i = 0; i < command.length(); i++) {
            char currentChar = command.charAt(i);
            escapingBuffer.step();
            
            boolean isEscaped = escapingBuffer.isCurrentlyEscaped();
            boolean isDelimiter = (currentChar == ' ') && !inQuotes && !isEscaped;
            
            if (isDelimiter) {
                if (!currentOriginal.isEmpty()) {
                    String main = currentArgument.toString();
                    String original = currentOriginal.toString();
                    int endIndex = i - 1;
                    parts.add(new Part(main, original, currentStartIndex, endIndex));
                    currentArgument.setLength(0);
                    currentOriginal.setLength(0);
                    currentStartIndex = i + 1;
                } else {
                    currentStartIndex = i + 1;
                }
                lastCharWasPlainLetter = false;
            } else {
                currentOriginal.append(currentChar);
                if (isEscaped) {
                    lastCharWasPlainLetter = true;
                    currentArgument.append(currentChar);
                } else {
                    lastCharWasPlainLetter = false;
                    switch (currentChar) {
                        case '\\' -> escapingBuffer.escapeNext();
                        case '"' -> inQuotes = !inQuotes;
                        case ' ' -> {
                            if (inQuotes) {
                                currentArgument.append(currentChar);
                            }
                        }
                        default -> {
                            lastCharWasPlainLetter = true;
                            currentArgument.append(currentChar);
                        }
                    }
                }
            }
        }
        
        if (!currentOriginal.isEmpty()) {
            String main = currentArgument.toString();
            String original = currentOriginal.toString();
            int endIndex = command.length() - 1;
            parts.add(new Part(main, original, currentStartIndex, endIndex));
        }
        
        System.out.println("inQuotes "+inQuotes);
        System.out.println("isEscapeNext "+escapingBuffer.isEscapeNext());
        System.out.println("lastCharWasPlainLetter "+lastCharWasPlainLetter);
        boolean heat = inQuotes || escapingBuffer.isEscapeNext() || lastCharWasPlainLetter;
        return new SplitResult(parts, heat);
    }

    
    public record SplitResult(List<Part> parts, boolean hot) {}
    public record Part(String main, String original, int beginIndexInclusive, int endIndexInclusive) {}
    
}