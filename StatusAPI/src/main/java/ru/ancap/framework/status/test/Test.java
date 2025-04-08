package ru.ancap.framework.status.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.status.util.ExceptionText;

import java.util.Optional;
import java.util.Set;

public interface Test {
    
    String id();
    CallableText name();
    TestResult makeTestFor(String testerIdentifier, TestingParameters testingParameters);

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class TestResult {
        
        public static TestResult SUCCESS = new TestResult(TestStatus.SUCCESS, null);
        
        public static TestResult error(Throwable throwable) {
            return new TestResult(
                TestStatus.FAILURE,
                new ExceptionText(throwable)
            );
        }

        public static TestResult skip(CallableText reason) {
            return new TestResult(TestStatus.SKIPPED, reason);
        }

        public static TestResult error(CallableText description) {
            return new TestResult(TestStatus.FAILURE, description);
        }
        
        TestStatus status;
        CallableText description;

        public TestStatus status() { return this.status; }
        public CallableText description() { return this.description; }

        public enum TestStatus {
            
            SUCCESS,
            SKIPPED,
            FAILURE
            
        }
    }
    @AllArgsConstructor
    class TestingParameters {
        
        Optional<Set<String>> toRun;
        Set<String> skipped;
        boolean skipHandTests;
        
    }
    
}