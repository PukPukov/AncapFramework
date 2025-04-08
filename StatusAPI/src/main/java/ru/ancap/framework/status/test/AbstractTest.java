package ru.ancap.framework.status.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true) @Getter
public class AbstractTest implements Test {
    
    private final String id;
    private final CallableText name;
    private final Function<String, TestResult> test;

    public AbstractTest(String id, Supplier<TestResult> test) {
        this(id, ignored -> test.get());
    }
    
    public AbstractTest(String id, Function<String, TestResult> test) {
        this(
            id,
            new LAPIText(CommonMessageDomains.Status.moduleName.apply(id)),
            (senderId) -> {
                try {
                    return test.apply(senderId);
                } catch (Throwable throwable) {
                    return TestResult.error(throwable);
                }
            }
        );
    }

    @Override
    public TestResult makeTestFor(String testerId, TestingParameters testingParameters) {
        if (testingParameters.toRun.isPresent() && !testingParameters.toRun.get().contains(this.id)) {
            return TestResult.skip(new LAPIText(CommonMessageDomains.Status.Skip.notExplicitlyIncluded));
        }
        if (testingParameters.skipped.contains(this.id)) {
            return TestResult.skip(new LAPIText(CommonMessageDomains.Status.Skip.excluded));
        }
        if (this instanceof HandTest && testingParameters.skipHandTests) {
            return TestResult.skip(new LAPIText(CommonMessageDomains.Status.Skip.handTestRefusal));
        }
        return this.test.apply(testerId);
    }
    
}