package ru.ancap.framework.api.center.command;

import lombok.With;
import lombok.experimental.WithBy;

import java.util.List;

@With @WithBy
public record CommandRegistrationState(String id, List<String> keys, CommandEventHandler handler) {}