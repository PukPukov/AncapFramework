package ru.ancap.framework.command.api.commands.object.event;

public sealed interface CommandEvent permits CommandDispatch, CommandWrite {}
