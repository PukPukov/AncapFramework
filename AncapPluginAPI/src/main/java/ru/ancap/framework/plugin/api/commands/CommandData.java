package ru.ancap.framework.plugin.api.commands;

import lombok.With;
import lombok.experimental.WithBy;

import java.util.List;

@With @WithBy
public record CommandData(String id, List<String> sources, CommandHandleState handleState) {}
