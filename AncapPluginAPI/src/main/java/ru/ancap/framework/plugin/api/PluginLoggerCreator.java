package ru.ancap.framework.plugin.api;

import lombok.SneakyThrows;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class PluginLoggerCreator {

    public Logger create(AncapPlugin /* not JavaPlugin to ensure the plugin folder is created */ plugin, String name) {
        File logFolder = new File(plugin.getDataFolder(), "logs"); logFolder.mkdirs();
        Logger logger = Logger.getLogger(name.toUpperCase());
        logger.addHandler(this.logHandler(logFolder, name+".log"));
        return logger;
    }

    private final Formatter formatter = new SimpleFormatter() {

        @Override
        public synchronized String format(LogRecord lr) {
            String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";
            return String.format(format, new java.util.Date(lr.getMillis()), lr.getLevel().getLocalizedName(), lr.getMessage());
        }

    };

    @SneakyThrows
    private Handler logHandler(File logFolder, String fileName) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logFileName = fileName.replace(".log", "-" + date + ".log");
        FileHandler handler = new FileHandler(new File(logFolder, logFileName).getAbsolutePath());

        handler.setEncoding("UTF-8");
        handler.setLevel(Level.ALL);
        handler.setErrorManager(new ErrorManager());
        handler.setFormatter(this.formatter);

        return handler;
    }

}