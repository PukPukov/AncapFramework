package ru.ancap.framework.api.command.commands.command.tab;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public interface TabCompletion {

    String getCompletion();
    Optional<Component> getTooltipState();

}
