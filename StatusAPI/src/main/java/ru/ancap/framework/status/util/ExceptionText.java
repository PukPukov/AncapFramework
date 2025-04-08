package ru.ancap.framework.status.util;

import lombok.RequiredArgsConstructor;
import ru.ancap.commons.string.Postformatter;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.message.MultilineText;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.modifier.ArgumentPlaceholder;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.CAPIDescribedException;

import java.util.Arrays;

@RequiredArgsConstructor
public class ExceptionText implements CallableText {
    
    private final CallableText delegate;
    
    @Override
    public String call(String identifier) {
        return identifier.equals(Identifier.CONSOLE_ID) ? new MultilineText(Text.EMPTY, this.delegate).call(identifier) : this.delegate.call(identifier);
    }
    
    public ExceptionText(Throwable throwable) {
        this(
            new LAPIText(
                CommonMessageDomains.Test.errorOutputForm,
                new Placeholder("exception", throwable.getClass().getName()),
                new Placeholder("exception data", spacing(Postformatter.postformat(throwable.toString()))),
                throwable instanceof CAPIDescribedException capiDescribedException ?
                    new Placeholder("message", identifier -> spacing(capiDescribedException.message().call(identifier))) :
                    new Placeholder("message", spacing(throwable.getMessage())),
                new ArgumentPlaceholder("stack trace", prefix -> new ChatBook<>(
                    Arrays.asList(throwable.getStackTrace()),
                    element -> new Text(
                        prefix,
                        new Placeholder(
                            "class name",
                            element.getClassName()
                        ),
                        new Placeholder(
                            "method name",
                            element.getMethodName()
                        ),
                        new Placeholder(
                            "line number",
                            element.getLineNumber()
                        ),
                        new Placeholder(
                            "module name",
                            element.getModuleName()
                        ),
                        new Placeholder(
                            "module version",
                            element.getModuleVersion()
                        ),
                        new Placeholder(
                            "class loader name",
                            element.getClassLoaderName()
                        )
                    )
                ))
            )
        );
    }
    
    private static String spacing(String postformat) {
        if (postformat.contains("\n")) postformat = "\n"+postformat;
        return postformat;
    }
    
}