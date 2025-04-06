package ru.ancap.framework.artifex;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import ru.ancap.commons.list.merge.MergeList;
import ru.ancap.commons.map.SafeMap;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.command.api.commands.operator.exclusive.Exclusive;
import ru.ancap.framework.command.api.commands.operator.exclusive.OP;
import ru.ancap.framework.command.api.commands.operator.exclusive.Permission;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.Ancap;
import ru.ancap.framework.plugin.api.AncapMinimalisticPlugin;
import ru.ancap.framework.plugin.api.information.AuthorsSupplier;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleHandle;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleReloadInput;
import ru.ancap.framework.plugin.util.InDevMessage;
import ru.ancap.framework.status.StatusOutput;
import ru.ancap.framework.status.test.Test;

import java.util.*;
import java.util.function.Supplier;

import static ru.ancap.framework.plugin.api.commands.exception.CommandExceptions.*;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class ArtifexCommandExecutor extends CommandTarget {
    
    public ArtifexCommandExecutor(Ancap ancap, List<Test> tests, LocaleHandle localeHandle) {
        super(new Delegate(
            new Raw(new AuthorsSupplier(Artifex.PLUGIN())),
            new SubCommand(
                new StringDelegatePattern("tps"),
                new Reply(() -> new LAPIMessage(
                    Artifex.class, "command.tps",
                    new Placeholder("tps", ancap.getServerTPS())
                ))
            ),
            new SubCommand(
                new StringDelegatePattern("benchmark"),
                new Exclusive(
                    new OP(),
                    new Delegate(
                        new SubCommand(
                            new StringDelegatePattern("command-api"),
                            dispatch -> dispatch.source().communicator().message(InDevMessage.instance())
                        ),
                        new SubCommand(
                            new StringDelegatePattern("dummy"),
                            dispatch -> {}
                        )
                    )
                )
            ),
            new SubCommand("dependency-graph-analyze", new Delegate(
                new SubCommand("find-child", new Exclusive(
                    new Permission("artifex.analyze-child.find-child"),
                    new CommandOperator() { // TODO migrate to proper arguments util when/if its ready
                        @Override
                        public void on(CommandWrite write) {
                            if (!write.line().isRaw()) return;
                            write.speaker().sendTab(Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).toList());
                        }
                        
                        @Override
                        public void on(CommandDispatch dispatch) {
                            String requestedPluginName = dispatch.command().nextArgument(noArgument(() -> new LAPIMessage(Artifex.class, "arguments.requested-plugin")));
                            
                            if (requestedPluginName.equalsIgnoreCase(Artifex.PLUGIN().getName())) {
                                dispatch.source().communicator().message(ArtifexCommandExecutor.artifexPluginsMessageSupplier.get());
                                return;
                            }
                            
                            Map<String, Set<Plugin>> children = SafeMap.<String, Set<Plugin>>builder(new HashMap<>())
                                .guaranteed(HashSet::new)
                                .build();
                            
                            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) for (Plugin dependency : deepDependencies(plugin)) {
                                children.get(dependency.getName()).add(plugin);
                            }
                            
                            dispatch.source().communicator().message(new LAPIMessage(
                                Artifex.class, "dependent-plugins.main-form",
                                new Placeholder("plugin", requestedPluginName),
                                new Placeholder(
                                    "dependent plugins",
                                    new ChatBook<>(
                                        children.get(requestedPluginName),
                                        plugin -> new LAPIMessage(
                                            Artifex.class, "dependent-plugins." + (plugin instanceof AncapMinimalisticPlugin ? "ancap" : "simple") + "-plugin-form",
                                            new Placeholder("plugin", plugin.getName())
                                        )
                                    )
                                )
                            ));
                        }
                        
                        private List<Plugin> deepDependencies(Plugin plugin) {
                            Set<Plugin> visited = new HashSet<>();
                            List<Plugin> dependencies = new ArrayList<>();
                            collectDependencies(plugin, visited, dependencies);
                            dependencies.remove(plugin);
                            return dependencies;
                        }
                        
                        private void collectDependencies(Plugin plugin, Set<Plugin> visited, List<Plugin> dependencies) {
                            if (visited.contains(plugin)) {
                                Bukkit.getLogger().warning("Cyclic dependency detected for plugin: " + plugin.getName());
                                return;
                            }
                            
                            visited.add(plugin);
                            if (!dependencies.contains(plugin)) dependencies.add(plugin);
                            List<Plugin> pluginDependencies = directDependencies(plugin);
                            for (Plugin dependency : pluginDependencies) collectDependencies(dependency, visited, dependencies);
                            visited.remove(plugin);
                        }
                        
                        private List<Plugin> directDependencies(Plugin plugin) {
                            return new MergeList<>(plugin.getDescription().getDepend(), plugin.getDescription().getSoftDepend()).stream()
                                .map(name -> Bukkit.getPluginManager().getPlugin(name))
                                .filter(Objects::nonNull)
                                .toList();
                        }
                }))
            )),
            new SubCommand(
                new StringDelegatePattern("status"),
                new Exclusive(
                    new OP(),
                    new StatusOutput(new Message("<color:#e51e1e>AncapFramework</color:#e51e1e>"), tests)
                )
            ),
            new SubCommand(
                new StringDelegatePattern("plugins"),
                new Exclusive(
                    new Permission("artifex.view-artifex-plugins"),
                    new Reply(artifexPluginsMessageSupplier)
                )
            ),
            new SubCommand(
                new StringDelegatePattern("reload"),
                new Delegate(
                    new SubCommand(
                        new StringDelegatePattern("locales"),
                        new LocaleReloadInput(localeHandle)
                    )
                )
            )
        ));
    }
    
    private static final Supplier<CallableMessage> artifexPluginsMessageSupplier = () -> new LAPIMessage(
        Artifex.class, "command.plugins.base-message",
        new Placeholder("plugins", new ChatBook<>(Artifex.PLUGIN().ancapPlugins(), plugin -> new LAPIMessage(
            Artifex.class, "command.plugins.plugin-form",
            new Placeholder("name", plugin.getName()),
            new Placeholder("version", plugin.getDescription().getVersion()),
            new Placeholder("authors", plugin.getDescription().getAuthors())
        )))
    );
    
}