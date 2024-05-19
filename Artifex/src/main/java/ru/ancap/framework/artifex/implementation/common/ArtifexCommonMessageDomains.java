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
        
        CommonMessageDomains.Status.testForm = this.domain("status.test-form");
        CommonMessageDomains.Status.working = this.domain("status.working");
        CommonMessageDomains.Status.testSkipped = this.domain("status.test-skipped");
        CommonMessageDomains.Status.down = this.domain("status.down");
        CommonMessageDomains.Status.pressToPrintDescription = this.domain("status.press-to-print-description");
        CommonMessageDomains.Status.top = this.domain("status.top");
        
        CommonMessageDomains.Status.Skip.notThatTester = this.domain("status.skip.not-that-tester");
        CommonMessageDomains.Status.Skip.handTestRefusal = this.domain("status.skip.hand-test-refusal");
        CommonMessageDomains.Status.Skip.testerTypes = this.domain("status.skip.tester-types");
        
        CommonMessageDomains.Test.errorOutputForm = this.domain("test.error-output-form");
        CommonMessageDomains.Test.root = this.domain("test");
        
        CommonMessageDomains.Reload.localesSuccessfullyReloaded = this.domain("reload.locales-successfully-reloaded");
    }

    private String domain(String domain) {
        return LAPIDomain.of(Artifex.class, domain);
    }

}