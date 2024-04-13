package ru.ancap.framework.plugin.api.language.locale.loader;

import ru.ancap.framework.command.api.commands.operator.exclusive.Exclusive;
import ru.ancap.framework.command.api.commands.operator.exclusive.Pass;
import ru.ancap.framework.command.api.commands.operator.exclusive.Permission;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.common.CommonPermissions;
import ru.ancap.framework.speak.common.CommonMessageDomains;

public class LocaleReloadInput extends Exclusive {
    
    public LocaleReloadInput(LocaleHandle localeHandle) {
        this(new Permission(CommonPermissions.reloadLocales), localeHandle);
    }
    
    public LocaleReloadInput(Pass pass, LocaleHandle localeHandle) {
        super(pass, dispatch -> {
            localeHandle.reload();
            dispatch.source().communicator().message(new LAPIMessage(CommonMessageDomains.Reload.localesSuccessfullyReloaded));
        });
    }
    
}