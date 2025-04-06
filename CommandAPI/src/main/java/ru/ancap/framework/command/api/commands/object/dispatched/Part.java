package ru.ancap.framework.command.api.commands.object.dispatched;


public record Part(String main, String original, int beginIndexInclusive, int endIndexInclusive) {}