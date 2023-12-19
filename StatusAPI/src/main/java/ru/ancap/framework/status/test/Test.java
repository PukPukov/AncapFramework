package ru.ancap.framework.status.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.ancap.framework.communicate.message.CallableMessage;

public interface Test {
    
    CallableMessage name();
    TestResult makeTestFor(String testerIdentifier);

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class TestResult {
        
        public static TestResult SUCCESS = new TestResult(TestStatus.SUCCESS, null);
        
        public static TestResult error(Throwable throwable) {
            return null;
            //return new TestResult(
            //    TestStatus.FAILURE,
            //    new ErrorTraceMessage(throwable)
            //);
            // TODO
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
    
}
