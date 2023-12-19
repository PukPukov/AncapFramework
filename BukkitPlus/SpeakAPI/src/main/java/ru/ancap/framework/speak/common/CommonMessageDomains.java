package ru.ancap.framework.speak.common;

import lombok.experimental.UtilityClass;

@UtilityClass public class CommonMessageDomains {
    
    public static String pluginInfo;
    public static String yesNo;
    public static String clickToSelect;
    public static String sentToConsole;
    public static String mergeTemplate;

    @UtilityClass public static class Status {

        public static String testForm;
        public static String working;
        public static String testSkipped;
        public static String down;
        public static String pressToPrintDescription;
        public static String top;

        @UtilityClass public static class Skip {

            public static String notThatTester;
            public static String testerTypes;
            
        }
        
    }

    @UtilityClass public static class Debug {
        
        public static String errorOutputForm;
        
    }
}
