package ru.ancap.framework.status.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.ArgumentPlaceholder;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.Arrays;

public interface Test {
    
    CallableMessage name();
    TestResult makeTestFor(String testerIdentifier, TestingParameters testingParameters);

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class TestResult {
        
        public static TestResult SUCCESS = new TestResult(TestStatus.SUCCESS, null);
        
        public static TestResult error(Throwable throwable) {
            return new TestResult(
                TestStatus.FAILURE,
                new LAPIMessage(
                    CommonMessageDomains.Test.errorOutputForm,
                    new Placeholder("exception", throwable.getClass().getName()),
                    throwable instanceof CAPIDescribedException capiDescribedException ?
                        new Placeholder("message", capiDescribedException.message()) :
                        new Placeholder("message", throwable.getMessage()),
                    new ArgumentPlaceholder("stack trace", prefix -> new ChatBook<>(
                        Arrays.asList(throwable.getStackTrace()),
                        element -> new Message(
                            prefix,
                            new Placeholder(
                                "stack trace element",
                                element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber()
                                    + "(" + element.getModuleName() +":"+element.getModuleVersion()+"<"+element.getClassLoaderName()+")"
                            )
                        )
                    ))
                )
            );
        }

        public static TestResult skip(CallableMessage reason) {
            return new TestResult(TestStatus.SKIPPED, reason);
        }

        public static TestResult error(CallableMessage description) {
            return new TestResult(TestStatus.FAILURE, description);
        }
        
        TestStatus status;
        CallableMessage description;

        public TestStatus status() { return this.status; }
        public CallableMessage description() { return this.description; }

        public enum TestStatus {
            
            SUCCESS,
            SKIPPED,
            FAILURE
            
        }
    }
    @AllArgsConstructor
    class TestingParameters {
        
        boolean skipHandTests;
        
    }
    
}