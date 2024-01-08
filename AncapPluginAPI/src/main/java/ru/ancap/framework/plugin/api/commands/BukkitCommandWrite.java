package ru.ancap.framework.plugin.api.commands;

import org.jetbrains.annotations.Nullable;

public record BukkitCommandWrite(BukkitCommandLineSpeaker writer, String line, @Nullable String hotArgument) {
}
