package ru.ancap.framework.artifex;

import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.artifex.implementation.plugin.AuthorsSupplier;

public class ArtifexCommandExecutor extends CommandTarget {
    public ArtifexCommandExecutor() {
        super(new AuthorsSupplier(Artifex.PLUGIN));
    }
}
