package ru.ancap.framework.speak.common;

import lombok.experimental.UtilityClass;
import ru.ancap.commons.ImplementationRequired;

@ImplementationRequired
@UtilityClass public class CommonMessageDomains {
    
    public static String pluginInfo;
    public static String yesNo;
    public static String clickToSelect;
    public static String sentToConsole;

    @UtilityClass public static class Status {

        public static String testForm;
        public static String working;
        public static String testSkipped;
        public static String down;
        public static String pressToPrintDescription;
        public static String top;

        @UtilityClass public static class Skip {

            public static String notThatTester;
            public static String handTestRefusal;
            public static String testerTypes;
            
        }
        
    }

    @UtilityClass public static class Test {
        
        public static String errorOutputForm;
        
    }
}