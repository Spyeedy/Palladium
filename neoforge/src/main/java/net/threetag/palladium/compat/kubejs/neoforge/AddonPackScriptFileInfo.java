package net.threetag.palladium.compat.kubejs.neoforge;

import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import dev.latvian.mods.kubejs.script.ScriptPackInfo;

import java.nio.file.Path;
import java.util.List;

public class AddonPackScriptFileInfo extends ScriptFileInfo {

    public List<String> lines;

    public AddonPackScriptFileInfo(ScriptPackInfo p, Path ph, String f) {
        super(p, ph, f);
    }

}
