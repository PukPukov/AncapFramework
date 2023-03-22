package ru.ancap.framework.plugin.api.information;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AncapPluginSettings {

    private final ConfigurationSection section;

    protected static class Path {
        public static final String PLUGIN_IDENTIFIER = "identifier";
        public static final String UPDATE_INSTALLATION_STRATEGY = "update.strategy.install";
        public static final String UPDATE_CHECK_STRATEGY = "update.strategy.check";
        public static final String LISTENERS_REGISTER_STAGE = "auto-register-stage.listeners";
        public static final String COMMAND_EXECUTORS_REGISTER_STAGE = "auto-register-stage.command-executors";
        public static final String COMMAND_LIST = "commands.list";
        public static final String ALIASES_DOMAIN = "commands.aliases";
    }

    public AncapPluginSettings(ConfigurationSection configuration) {
        this.section = configuration;
    }

    @Nullable
    public String pluginIdentifier(String service) {
        return this.section.getString(Path.PLUGIN_IDENTIFIER+"."+service);
    }

    public UpdateInstallationStrategyType updateInstallationStrategy() {
        String type = this.section.getString(Path.UPDATE_INSTALLATION_STRATEGY, "FORCE");
        return UpdateInstallationStrategyType.valueOf(type.toUpperCase());
    }
    
    public UpdateCheckStrategyType updateCheckStrategyType() {
        String type = this.section.getString(Path.UPDATE_CHECK_STRATEGY, "UPDATE_IF_LATEST_RELEASE_TIME_NEWER");
        return UpdateCheckStrategyType.valueOf(type.toUpperCase());
    }

    public List<String> commandList() {
        return this.section.getStringList(Path.COMMAND_LIST);
    }

    public List<String> aliasesList(String commandName) {
        ConfigurationSection section = this.section.getConfigurationSection(Path.ALIASES_DOMAIN);
        if (section == null) return Collections.emptyList();
        return section.getStringList(commandName);
    }
    
    public RegisterStage commandExecutorRegisterStage() {
        return this.registerStageOf(Path.COMMAND_EXECUTORS_REGISTER_STAGE);
    }

    public RegisterStage listenerRegisterStage() {
        return this.registerStageOf(Path.LISTENERS_REGISTER_STAGE);
    }

    private RegisterStage registerStageOf(String integrator) {
        return RegisterStage.valueOf(this.section.getString(integrator, "ANCAP_PLUGIN_ENABLE").toUpperCase());
    }

}
