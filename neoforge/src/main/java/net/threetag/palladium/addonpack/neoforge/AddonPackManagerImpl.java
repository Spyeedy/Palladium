package net.threetag.palladium.addonpack.neoforge;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import net.threetag.palladium.addonpack.AddonPackManager;

import java.util.Objects;
import java.util.function.Consumer;

public class AddonPackManagerImpl {

    public static RepositorySource getModRepositorySource() {
        return new ModPackSource(Objects.requireNonNull(AddonPackManager.getPackType()));
    }

    public static class ModPackSource implements RepositorySource {

        private final PackType type;

        public ModPackSource(PackType type) {
            this.type = type;
        }

        @Override
        public void loadPacks(Consumer<Pack> onLoad) {
//            for (IModInfo modInfo : ModList.get().getMods()) {
//                PathPackResources pack = ResourcePackLoader.createPackForMod(modInfo.getOwningFile());
//
//                if (Objects.equals(pack.packId(), "minecraft")) continue;
//
//                final String name = "mod:" + modInfo.getModId();
//                final Pack packInfo = Pack.readMetaAndCreate(name, Component.literal(pack.packId()), false, id -> new ModResourcePack(pack, modInfo), AddonPackManager.getPackType(), Pack.Position.BOTTOM, PackSource.DEFAULT);
//                if (packInfo == null) {
//                    continue;
//                }
//                onLoad.accept(packInfo);
//            }
        }
    }


//    public static class ModResourcePack extends PathPackResources {
//
//        private final PathPackResources parent;
//        private final IModInfo mod;
//
//        public ModResourcePack(PathPackResources parent, IModInfo mod) {
//            super(parent.packId(), parent.isBuiltin(), parent.getSource());
//            this.parent = parent;
//            this.mod = mod;
//        }
//
//        @Override
//        public @Nullable IoSupplier<InputStream> getRootResource(String... paths) {
//            String fileName = String.join("/", paths);
//            final Path path = resolve(paths);
//
//            if ("pack.mcmeta".equals(fileName) && (path == null || !Files.exists(path))) {
//                String id = this.mod.getModId();
//                String version = MavenVersionStringHelper.artifactVersionToString(this.mod.getVersion());
//                String description = this.mod.getDescription();
//                String pack = String.format("{\"pack\":{\"id\": \"%s\", \"version\": \"%s\", \"description\":\"%s\"}}", id, version, description);
//                return () -> IOUtils.toInputStream(pack, Charsets.UTF_8);
//            }
//
//            return super.getRootResource(paths);
//        }
//
//        @Override
//        protected @NotNull Path resolve(String... paths) {
//            return this.parent instanceof PathPackResourcesAccessor acc ? acc.invokeResolve(paths) : super.resolve(paths);
//        }
//    }
}
