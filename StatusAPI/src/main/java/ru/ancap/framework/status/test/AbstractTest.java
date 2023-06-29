package ru.ancap.framework.status.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.language.additional.LAPIMessage;

import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AbstractTest implements Test {
    
    private final CallableMessage name;
    private final Function<String, TestResult> test;

    public AbstractTest(String nameId, Supplier<TestResult> test) {
        this(nameId, ignored -> test.get());
    }
    
    public AbstractTest(String nameId, Function<String, TestResult> test) {
        this(new LAPIMessage(nameId), (senderId) -> {
            try {
                return test.apply(senderId);
            } catch (Throwable throwable) {
                return TestResult.error(throwable);
            }
        });
    }

    @Override
    public CallableMessage name() {
        return this.name;
    }

    @Override
    public TestResult makeTestFor(String testerId) {
        return this.test.apply(testerId);
    }
    
}
