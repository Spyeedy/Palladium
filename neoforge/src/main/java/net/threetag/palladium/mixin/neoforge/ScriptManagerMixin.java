package net.threetag.palladium.mixin.neoforge;

import dev.latvian.mods.kubejs.script.*;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.compat.kubejs.neoforge.AddonPackScriptFileInfo;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(ScriptManager.class)
public class ScriptManagerMixin {

    @Shadow(remap = false)
    @Final
    public Map<String, ScriptPack> packs;

    @Shadow(remap = false)
    @Final
    public ScriptType scriptType;

    @Inject(at = @At("RETURN"), method = "loadFromDirectory", remap = false)
    public void loadFromDirectory(CallbackInfo ci) {
        AddonPackManager.getInstance().getPackList().reload();
        AddonPackManager.getInstance().getPackList().setSelected(AddonPackManager.getInstance().getPackList().getAvailableIds());

        Map<String, ScriptPack> scriptFileInfoMap = new HashMap<>();

        for (Pack pack : AddonPackManager.getInstance().getPackList().getAvailablePacks()) {
            var packType = this.scriptType == ScriptType.CLIENT ? PackType.CLIENT_RESOURCES : (this.scriptType == ScriptType.SERVER ? PackType.SERVER_DATA : AddonPackManager.getPackType());
            var packResources = pack.open();

            for (String namespace : packResources.getNamespaces(packType)) {
                packResources.listResources(packType, namespace, "kubejs_scripts", (path, inputStreamIoSupplier) -> {
                    if (path.getPath().endsWith(".js") || path.getPath().endsWith(".ts") && !path.getPath().endsWith(".d.ts")) {
                        var scriptPack = scriptFileInfoMap.computeIfAbsent(namespace, s -> new ScriptPack((ScriptManager) (Object) this, new ScriptPackInfo("addonpack_" + s, "")));
                        var scriptFileInfo = new AddonPackScriptFileInfo(scriptPack.info, null, path.getPath());

                        try {
                            InputStream inputStream = Objects.requireNonNull(packResources.getResource(packType, path)).get();
                            scriptFileInfo.lines = IOUtils.readLines(inputStream, Charset.defaultCharset());
                            inputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        scriptPack.info.scripts.add(scriptFileInfo);
                    }
                });
            }

            packResources.close();
        }

        for (Map.Entry<String, ScriptPack> e : scriptFileInfoMap.entrySet()) {
            var scriptPack = e.getValue();

            for (var fileInfo : scriptPack.info.scripts) {
                try {
                    var file = new ScriptFile(scriptPack, fileInfo);
                    var skip = file.skipLoading();

                    if (skip.isEmpty()) {
                        scriptPack.scripts.add(file);
                    } else {
                        scriptType.console.info("Skipped " + fileInfo.location + ": " + skip);
                    }
                } catch (Throwable error) {
                    scriptType.console.error("Failed to pre-load script file '" + fileInfo.location + "'", error);
                }
            }

            scriptPack.scripts.sort(null);
            this.packs.put(scriptPack.info.namespace, scriptPack);
        }
    }

}
