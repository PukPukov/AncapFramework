package ru.ancap.framework.status;

import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.flag.FlagParser;
import ru.ancap.framework.command.api.commands.flag.object.FlagData;
import ru.ancap.framework.command.api.commands.object.dispatched.Part;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.*;
import ru.ancap.framework.communicate.message.clickable.ClickableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.Test;

import java.util.*;
import java.util.stream.Collectors;

import static ru.ancap.framework.command.api.commands.exception.CommandExceptions.*;

public class StatusOutput extends CommandTarget {
    
    public static final String SKIP_HAND_TESTS_FLAG = "skip-hand-tests";
    public static final String INCLUDE_FLAG = "include";
    public static final String EXCLUDE_FLAG = "exclude";
    
    private static final CallableText INCLUDE_TEST_ID = new LAPIText(CommonMessageDomains.Status.includeTestId);
    private static final CallableText EXCLUDE_TEST_ID = new LAPIText(CommonMessageDomains.Status.excludeTestId);
    
    public StatusOutput(CallableText systemName, List<Test> tests) {
        this(systemName, collectTestsMap(tests));
    }
    
    private StatusOutput(CallableText systemName, Map<String, Test> tests) {
        super(new CommandOperator() {
            @Override
            public void on(CommandDispatch dispatch) {
                Map<String, List<FlagData>> flags = FlagParser.parseFlags(dispatch.command().allNextParts().stream()
                    .map(Part::main).toList()).destroy();
                List<FlagData> includes = flags.get(INCLUDE_FLAG);
                List<FlagData> excludes = flags.get(EXCLUDE_FLAG);
                Test.TestingParameters testingParameters = new Test.TestingParameters(
                    includes.isEmpty() ? Optional.empty() : Optional.of(destroy(includes, INCLUDE_FLAG, INCLUDE_TEST_ID)),
                    destroy(excludes, EXCLUDE_FLAG, EXCLUDE_TEST_ID),
                    flags.containsKey(SKIP_HAND_TESTS_FLAG)
                );
                dispatch.source().communicator().message(new MultilineText(
                    new LAPIText(
                        CommonMessageDomains.Status.top,
                        new Placeholder("system name", systemName)
                    ),
                    new ChatBook<>(
                        tests.entrySet(),
                        test -> new LAPIText(
                            CommonMessageDomains.Status.testForm,
                            new Placeholder("module name", test.getValue().name()),
                            new Placeholder("status", identifier -> {
                                Test.TestResult result = test.getValue().makeTestFor(
                                    identifier,
                                    testingParameters
                                );
                                CallableText represent = switch (result.status()) {
                                    case SUCCESS -> new LAPIText(CommonMessageDomains.Status.working);
                                    case SKIPPED -> new BadTestText(CommonMessageDomains.Status.testSkipped, result.description());
                                    case FAILURE -> new BadTestText(CommonMessageDomains.Status.down, result.description());
                                };
                                return represent.call(identifier);
                            })
                        )
                    )
                ));
            }
            @Override
            public void on(CommandWrite write) {
                List<String> tabCompletes = new ArrayList<>();
                Set<String> testIds = tests.keySet();
                tabCompletes.add(FlagParser.PREFIX+SKIP_HAND_TESTS_FLAG);
                for (var flag : List.of(INCLUDE_FLAG, EXCLUDE_FLAG)) {
                    tabCompletes.addAll(testIds.stream()
                        .map(testId -> FlagParser.PREFIX+flag+"="+testId).toList());
                }
                write.speaker().sendTab(tabCompletes);
            }
        });
    }
    
    private static Map<String, Test> collectTestsMap(List<Test> tests) {
        Map<String, Test> map = new HashMap<>(tests.size());
        for (Test test : tests) map.put(test.id(), test);
        return map;
    }
    
    private static Set<String> destroy(List<FlagData> flagData, String key, CallableText dataDescription) {
        return flagData.stream().map(flag -> flag.data().orElseThrow(noFlagData(key, dataDescription))).collect(Collectors.toSet());
    }
    
    private static class BadTestText extends WrapperText {
        
        public BadTestText(String mainMessageDomain, CallableText description) {
            super(new ClickableText(
                new HoveredText(
                    new LAPIText(mainMessageDomain),
                    new LAPIText(CommonMessageDomains.Status.pressToPrintDescription)
                ),
                click -> {
                    Communicator.of(click.clicker()).message(new LAPIText(CommonMessageDomains.sentToConsole));
                    Communicator.of(Bukkit.getConsoleSender()).message(description);
                }
            ));
        }
        
    }
    
}