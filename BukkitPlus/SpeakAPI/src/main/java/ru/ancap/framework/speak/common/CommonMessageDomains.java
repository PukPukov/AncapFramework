package ru.ancap.framework.speak.common;

import ru.ancap.commons.ImplementationRequired;

import java.util.function.Function;

@ImplementationRequired
public class CommonMessageDomains {
    
    public static String pluginInfo;
    public static String yesNo;
    public static String clickToSelect;
    public static String sentToConsole;
    public static String inDev;
    
    public static class Status {

        public static String testForm;
        public static String working;
        public static String testSkipped;
        public static String down;
        public static String pressToPrintDescription;
        public static String top;
        public static String includeTestId;
        public static String excludeTestId;
        public static Function<String, String> moduleName;
        
        public static class Skip {

            public static String notThatTester;
            public static String handTestRefusal;
            public static String testerTypes;
            public static String notExplicitlyIncluded;
            public static String excluded;
            
        }
        
    }

    public static class Test {
        
        public static String errorOutputForm;
        public static String handTestFailure;
        public static String root;
        
    }
    
    public static class Reload {
        
        public static String localesSuccessfullyReloaded;
        
    }
    
    public static class Error {
        
        public static String bukkitPermission;
        public static String operateIsImpossible;
    }
    
}