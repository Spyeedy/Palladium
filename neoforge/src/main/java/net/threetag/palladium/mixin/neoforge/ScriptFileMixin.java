package net.threetag.palladium.mixin.neoforge;

import dev.latvian.mods.kubejs.script.ScriptFile;
import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import net.threetag.palladium.compat.kubejs.neoforge.AddonPackScriptFileInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Mixin(ScriptFile.class)
public class ScriptFileMixin {

    @Shadow @Final public ScriptFileInfo info;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/nio/file/Files;readAllLines(Ljava/nio/file/Path;)Ljava/util/List;"))
    private List<String> constructorRedirect(Path path) throws IOException {
        return this.info instanceof AddonPackScriptFileInfo packInfo ? packInfo.lines : Files.readAllLines(path);
    }

}
