package ru.ancap.framework.artifex.implementation.language.domains.common;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.speak.common.CommonMessageDomains;

public class ArtifexCommonMessageDomains {
    
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
    }

    private String domain(String domain) {
        return LAPIDomain.of(Artifex.class, domain);
    }

}