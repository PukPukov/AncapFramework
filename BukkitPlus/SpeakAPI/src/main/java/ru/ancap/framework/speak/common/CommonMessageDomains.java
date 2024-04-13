package ru.ancap.framework.speak.common;

import ru.ancap.commons.ImplementationRequired;

@ImplementationRequired
public class CommonMessageDomains {
    
    public static String pluginInfo;
    public static String yesNo;
    public static String clickToSelect;
    public static String sentToConsole;

    public static class Status {

        public static String testForm;
        public static String working;
        public static String testSkipped;
        public static String down;
        public static String pressToPrintDescription;
        public static String top;

        public static class Skip {

            public static String notThatTester;
            public static String handTestRefusal;
            public static String testerTypes;
            
        }
        
    }

    public static class Test {
        
        public static String errorOutputForm;
        
    }
    
    public static class Reload {
        
        public static String localesSuccessfullyReloaded;
        
    }
    
}