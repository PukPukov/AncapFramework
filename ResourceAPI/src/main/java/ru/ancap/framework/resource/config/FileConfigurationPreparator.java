package ru.ancap.framework.resource.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.ancap.framework.resource.ResourcePreparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class FileConfigurationPreparator implements ResourcePreparator<FileConfiguration> {
    
    private final Function<Integer, TransferMap> versionToMap;
    private String versionFieldName;
    private final boolean saveFiles;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static FileConfigurationPreparator resolveConflicts( 
            Function<Integer, TransferMap> versionToMap,
            String versionFieldName
    ) {
        return FileConfigurationPreparator.builder().resolveConflicts(versionToMap, versionFieldName).build();
    }
    
    public static FileConfigurationPreparator internal() {
        return FileConfigurationPreparator.builder().internal().build();
    }
    
    public static FileConfigurationPreparator plain() {
        return FileConfigurationPreparator.builder().build();
    }
    
    public static class Builder {
        
        private Function<Integer, TransferMap> versionToMap;
        private String versionFieldName;
        private boolean saveFiles = true;
        
        public Builder resolveConflicts(Function<Integer, TransferMap> versionToMap, String versionFieldName) {
            this.versionToMap = versionToMap;
            this.versionFieldName = versionFieldName;
            return this;
        }
        
        public Builder internal() {
            this.saveFiles = false;
            return this;
        }
        
        public FileConfigurationPreparator build() {
            return new FileConfigurationPreparator(this.versionToMap, this.versionFieldName, this.saveFiles);
        }
        
    }
    
    @Override
    @SneakyThrows
    public FileConfiguration prepare(InputStream base, @NonNull File target) {
        FileConfiguration finalConfig;
        switch (this.resolveStateFrom(base, target)) {
            case NULL: return null; 
            case SOFTWARE: finalConfig = this.extract(base); break;
            case USER: finalConfig = this.extract(new FileInputStream(target)); break;
            case CONFLICT:
                FileConfiguration userData = this.extract(new FileInputStream(target));
                FileConfiguration softwareData = this.extract(base);
                if (this.versionFieldName == null) this.versionFieldName = "dummy";
                Function<ConfigurationSection, Integer> versionExtractor = new VersionExtractor(this.versionFieldName);
                if (versionExtractor.apply(userData).equals(versionExtractor.apply(softwareData))) {
                    finalConfig = userData;
                    break;
                }
                TransferMap transferMap = this.versionToMap.apply(versionExtractor.apply(userData));
                finalConfig = softwareData;
                this.writeFile(new File(
                    new File(target.getParentFile(), "backup"),
                    FilenameUtils.getBaseName(target.getName())+"_"+System.currentTimeMillis()+".old"
                ), userData);
                Set<String> userKeys = userData.getKeys(true);
                userKeys.remove(this.versionFieldName);
                for (String key : userKeys) {
                    Object value = userData.get(key);
                    if (value instanceof MemorySection) continue;
                    String transferTarget = transferMap.getTarget(key);
                    if (transferTarget == null) transferTarget = key;
                    finalConfig.set(transferTarget, value);
                } break;
            default: throw new IllegalStateException("Unexpected value: " + this.resolveStateFrom(base, target));
        }
        this.prepareFiles(target, finalConfig);
        return finalConfig;
    }

    private FileConfiguration extract(InputStream input) {
        return YamlConfiguration.loadConfiguration(new InputStreamReader(input));
    }

    private void prepareFiles(File file, FileConfiguration fileConfiguration) {
        if (this.saveFiles) this.writeFile(file, fileConfiguration);
    }

    @SneakyThrows
    private void writeFile(File file, FileConfiguration fileConfiguration) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();
        fileConfiguration.save(file);
    }

    private ResolveState resolveStateFrom(InputStream base, File target) {
        if (base == null && !target.exists()) return ResolveState.NULL;
        if (base == null) return ResolveState.USER;
        if (!target.exists()) return ResolveState.SOFTWARE;
        else return ResolveState.CONFLICT;
    }

    private enum ResolveState {
        
        NULL, 
        SOFTWARE,
        USER,
        CONFLICT
        
    }
}