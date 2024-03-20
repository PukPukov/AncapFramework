package ru.ancap.framework.plugin.api.information;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AncapPluginSettings {

    private final ConfigurationSection section;

    protected static class Path {
        public static final String LISTENERS_REGISTER_STAGE = "auto-register-stage.listeners";
        public static final String COMMAND_EXECUTORS_REGISTER_STAGE = "auto-register-stage.command-executors";
        public static final String COMMAND_LIST = "commands.list";
        public static final String ALIASES_DOMAIN = "commands.aliases";
    }

    public AncapPluginSettings(@Nullable ConfigurationSection configuration) {
        this.section = configuration;
    }

    public List<String> getCommandList() {
        if (this.section == null) return List.of();
        return this.section.getStringList(Path.COMMAND_LIST);
    }

    public List<String> getAliasesList(String commandName) {
        if (this.section == null) return List.of();
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

    private RegisterStage registerStageOf(String path) {
        if (this.section == null) return RegisterStage.ANCAP_PLUGIN_ENABLE;
        return RegisterStage.valueOf(this.section.getString(path, "ANCAP_PLUGIN_ENABLE").toUpperCase());
    }
    
}