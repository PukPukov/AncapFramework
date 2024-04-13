package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

@RequiredArgsConstructor
public class LocaleReloadInput implements CommandOperator {
    
    private final LocaleHandle localeHandle;
    
    @Override
    public void on(CommandDispatch dispatch) {
        this.localeHandle.reload();
        dispatch.source().communicator().message(new LAPIMessage(CommonMessageDomains.Reload.localesSuccessfullyReloaded));
    }
    
}