package ru.ancap.framework.api.command.util;

import org.bukkit.command.CommandSender;

import java.util.function.BiFunction;

public interface TypeNameProvider extends BiFunction<Class<?>, CommandSender, String> {
}
