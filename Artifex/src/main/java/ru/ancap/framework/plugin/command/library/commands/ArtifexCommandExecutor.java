package ru.ancap.framework.plugin.command.library.commands;

import ru.ancap.framework.api.LAPI;
import ru.ancap.framework.api.command.commands.CommandTarget;
import ru.ancap.framework.api.command.commands.operator.speaking.Adviser;
import ru.ancap.framework.plugin.Artifex;

public class ArtifexCommandExecutor extends CommandTarget {
    public ArtifexCommandExecutor() {
        super(
                new Adviser(sender -> LAPI.localized(Artifex.MESSAGE_DOMAIN+"plugin-info", sender.getName())
                        .replace("%VERSION%", Artifex.INFO.getVersion())
                        .replace("%AUTHORS%", Artifex.INFO.getAuthors().stream().reduce((s1, s2) -> s1+" ,"+s2).get())
                )
        );
    }
}
