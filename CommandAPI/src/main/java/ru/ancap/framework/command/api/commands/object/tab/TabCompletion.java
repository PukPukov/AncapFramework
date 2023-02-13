package ru.ancap.framework.command.api.commands.object.tab;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public interface TabCompletion {

    String completion();
    Optional<Component> tooltipState();

}
