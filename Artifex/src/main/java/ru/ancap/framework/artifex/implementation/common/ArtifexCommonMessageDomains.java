package ru.ancap.framework.artifex.implementation.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.speak.common.CommonMessageDomains;

@ApiStatus.Internal
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtifexCommonMessageDomains {
    
    public static ArtifexCommonMessageDomains init() {
        var domains = new ArtifexCommonMessageDomains();
        domains.load();
        return domains;
    }
    
    public void load() {
        CommonMessageDomains.pluginInfo = this.domain("plugin-info");
        CommonMessageDomains.yesNo = this.domain("yes-no");
        CommonMessageDomains.clickToSelect = this.domain("click-to-select");
        CommonMessageDomains.sentToConsole = this.domain("sent-to-console");
        CommonMessageDomains.inDev = this.domain("in-dev");
        
        CommonMessageDomains.Status.testForm = this.domain("status.test-form");
        CommonMessageDomains.Status.working = this.domain("status.working");
        CommonMessageDomains.Status.testSkipped = this.domain("status.test-skipped");
        CommonMessageDomains.Status.down = this.domain("status.down");
        CommonMessageDomains.Status.pressToPrintDescription = this.domain("status.press-to-print-description");
        CommonMessageDomains.Status.top = this.domain("status.top");
        CommonMessageDomains.Status.includeTestId = this.domain("arguments.include-test-id");
        CommonMessageDomains.Status.excludeTestId = this.domain("arguments.exclude-test-id");
        CommonMessageDomains.Status.moduleName = (module) -> this.domain("modules."+module+".name");
        
        CommonMessageDomains.Status.Skip.notThatTester = this.domain("status.skip.not-that-tester");
        CommonMessageDomains.Status.Skip.handTestRefusal = this.domain("status.skip.hand-test-refusal");
        CommonMessageDomains.Status.Skip.testerTypes = this.domain("status.skip.tester-types");
        CommonMessageDomains.Status.Skip.notExplicitlyIncluded = this.domain("status.skip.not-explicitly-included");
        CommonMessageDomains.Status.Skip.excluded = this.domain("status.skip.excluded");
        
        CommonMessageDomains.Test.errorOutputForm = this.domain("test.error-output-form");
        CommonMessageDomains.Test.handTestFailure = this.domain("test.hand-test-failure");
        CommonMessageDomains.Test.root = this.domain("test");
        
        CommonMessageDomains.Reload.localesSuccessfullyReloaded = this.domain("reload.locales-successfully-reloaded");
        
        CommonMessageDomains.Error.bukkitPermission = this.domain("command.api.error.not-enough-permissions.bukkit-permission");
        CommonMessageDomains.Error.operateIsImpossible = this.domain("command.api.error.operate-is-impossible");
    }

    private String domain(String domain) {
        return LAPIDomain.of(Artifex.class, domain);
    }

}