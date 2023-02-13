package ru.ancap.framework.plugin.api.information;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.List;

public class AncapPluginSettings {

    private final ConfigurationSection section;

    protected static class Path {
        public static final String PLUGIN_IDENTIFIER = "identifier";
        public static final String LISTENERS_REGISTER_STAGE = "auto-register-stage.listeners";
        public static final String COMMAND_EXECUTORS_REGISTER_STAGE = "auto-register-stage.command-executors";
        public static final String COMMAND_LIST = "commands.list";
        public static final String ALIASES_DOMAIN = "commands.aliases";
    }

    public AncapPluginSettings(ConfigurationSection configuration) {
        this.section = configuration;
    }

    public int getPluginIdentifier() {
        return this.section.getInt(Path.PLUGIN_IDENTIFIER);
    }

    public List<String> getCommandList() {
        return this.section.getStringList(Path.COMMAND_LIST);
    }

    public List<String> getAliasesList(String commandName) {
        ConfigurationSection section = this.section.getConfigurationSection(Path.ALIASES_DOMAIN);
        if (section == null) return Collections.emptyList();
        return section.getStringList(commandName);
    }
    
    public RegisterStage getCommandExecutorRegisterStage() {
        return this.registerStageOf(Path.COMMAND_EXECUTORS_REGISTER_STAGE);
    }

    public RegisterStage getListenerRegisterStage() {
        return this.registerStageOf(Path.LISTENERS_REGISTER_STAGE);
    }

    private RegisterStage registerStageOf(String path) {
        return RegisterStage.valueOf(
                this.section.getString(
                        path, "ANCAP_PLUGIN_ENABLE"
                ).toUpperCase()
        );
    }

}
