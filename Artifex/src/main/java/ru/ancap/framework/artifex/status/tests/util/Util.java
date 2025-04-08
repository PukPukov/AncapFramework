package ru.ancap.framework.artifex.status.tests.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;

@UtilityClass
@ApiStatus.Internal
public class Util {

    public static CommandOperator baseResponse(String domain, short identity) {
        return dispatch -> dispatch.source().communicator().message(new LAPIText(
            Artifex.class, domain,
            new Placeholder("identity", identity)
        ));
    }
    
    public static CallableText commandQuestion(String id, String type, String commandName, byte identity) {
        return new LAPIText(
            Artifex.class, "test.base."+type,
            new Placeholder("action", new LAPIText(
                Artifex.class, "test.checks.action.command", 
                new Placeholder("command", commandName)
            )),
            new Placeholder("expected", new LAPIText(
                Artifex.class, "test.checks.expected."+type+"."+id,
                new Placeholder("identity", identity)
            ))
        );
    }
    
}