package ru.ancap.framework.updater;

import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;
import ru.ancap.framework.plugin.api.AncapPlugin;

public class SpigetUpdater implements Updater {
    
    @Override
    public void update(AncapPlugin plugin) {
        SpigetUpdate updater = new SpigetUpdate(plugin, 12345);

        // This compares versions just by checking if they are equal
        // This means that older versions will also be treated as updates
        updater.setVersionComparator(VersionComparator.EQUAL);

        // This converts a semantic version to an integer and checks if the updated version is greater
        updater.setVersionComparator(VersionComparator.SEM_VER);

        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
                //// A new version is available
                // newVersion - the latest version
                // downloadUrl - URL to the download
                // hasDirectDownload - whether the update is available for a direct download on spiget.org
            }

            @Override
            public void upToDate() {
                //// Plugin is up-to-date
            }
        });
    }
    
}
