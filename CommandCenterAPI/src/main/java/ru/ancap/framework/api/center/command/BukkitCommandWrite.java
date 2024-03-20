package ru.ancap.framework.api.center.command;

import org.jetbrains.annotations.Nullable;

public record BukkitCommandWrite(BukkitCommandLineSpeaker writer, String line, @Nullable String hotArgument) { }