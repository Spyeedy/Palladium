package net.threetag.palladium.addonpack;

import dev.architectury.platform.Platform;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.nio.file.Path;

public class AddonPackManager {

    private static AddonPackManager INSTANCE;
    public static final PackSource PACK_SOURCE = PackSource.create(PackSource.decorateWithSource("pack.source.addonpack"), true);

    public static final String FOLDER = "palladium/addonpacks";
    public static final String LEGACY_FOLDER = "addonpacks";

    public static AddonPackManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddonPackManager();
        }
        return INSTANCE;
    }

    private AddonPackManager() {
    }

    public static Path getLocation(String path) {
        Path folder = Platform.getGameFolder().resolve(path);
        var file = folder.toFile();
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("Could not create addonpacks directory! Please create the directory yourself, or make sure the name is not taken by a file and you have permission to create directories.");
        return folder;
    }

    public static Path getLocation() {
        return getLocation(FOLDER);
    }

    public static Path getLegacyLocation() {
        return getLocation(LEGACY_FOLDER);
    }

    public static RepositorySource getWrappedPackFinder(PackType packType) {
        var folderPackFinder = new FolderRepositorySource(getLocation(), packType, PACK_SOURCE, LevelStorageSource.parseValidator(Platform.getGameFolder().resolve("allowed_symlinks.txt")));
        var legacyFolderPackFinder = new FolderRepositorySource(getLegacyLocation(), packType, PACK_SOURCE, LevelStorageSource.parseValidator(Platform.getGameFolder().resolve("allowed_symlinks.txt")));

        return (infoConsumer) -> {
            folderPackFinder.loadPacks(pack -> {
                pack.location = new PackLocationInfo("addonpack:" + pack.getId(), pack.location.title(), pack.location.source(), pack.location.knownPackInfo());
                pack.selectionConfig = new PackSelectionConfig(true, pack.selectionConfig.defaultPosition(), pack.selectionConfig.fixedPosition());
                infoConsumer.accept(pack);
            });

            legacyFolderPackFinder.loadPacks(pack -> {
                pack.location = new PackLocationInfo("addonpack:" + pack.getId(), pack.location.title(), pack.location.source(), pack.location.knownPackInfo());
                pack.selectionConfig = new PackSelectionConfig(true, pack.selectionConfig.defaultPosition(), pack.selectionConfig.fixedPosition());
                infoConsumer.accept(pack);
            });
        };
    }

}
