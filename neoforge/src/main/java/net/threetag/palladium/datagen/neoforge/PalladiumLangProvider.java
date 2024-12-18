package net.threetag.palladium.datagen.neoforge;

import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.power.ability.AbilitySerializers;

public abstract class PalladiumLangProvider extends ExtendedLangProvider {

    public PalladiumLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    public PalladiumLangProvider(PackOutput packOutput, String locale) {
        super(packOutput, Palladium.MOD_ID, locale);
    }

    public static class English extends PalladiumLangProvider {

        public English(PackOutput packOutput) {
            super(packOutput, "en_us");
        }

        @Override
        protected void addTranslations() {
            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Command");
            this.addAbility(AbilitySerializers.RENDER_LAYER, "Render Layer");
//            this.addAbility(AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(AbilitySerializers.SHRINK_BODY_OVERLAY, "Shrink Body Overlay");
            this.addAbility(AbilitySerializers.ATTRIBUTE_MODIFIER, "Attribute Modifier");
            this.addAbility(AbilitySerializers.HEALING, "Healing");
            this.addAbility(AbilitySerializers.SLOWFALL, "Slowfall");
            this.addAbility(AbilitySerializers.DAMAGE_IMMUNITY, "Damage Immunity");
            this.addAbility(AbilitySerializers.INVISIBILITY, "Invisibility");
//            this.addAbility(AbilitySerializers.ENERGY_BEAM, "Energy Beam");
            this.addAbility(AbilitySerializers.SIZE, "Size");
//            this.addAbility(AbilitySerializers.PROJECTILE, "Projectile");
            this.addAbility(AbilitySerializers.SKIN_CHANGE, "Skin Change");
            this.addAbility(AbilitySerializers.AIM, "Aim");
            this.addAbility(AbilitySerializers.HIDE_BODY_PART, "Hide Body Part");
            this.addAbility(AbilitySerializers.REMOVE_BODY_PART, "Remove Body Part");
            this.addAbility(AbilitySerializers.SHADER_EFFECT, "Shader Effect");
            this.addAbility(AbilitySerializers.GUI_OVERLAY, "Gui Overlay");
            this.addAbility(AbilitySerializers.SHOW_BOTH_ARMS, "Show Both Arms");
//            this.addAbility(AbilitySerializers.PLAYER_ANIMATION, "Player Animation");
            this.addAbility(AbilitySerializers.WATER_WALK, "Water Walk");
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Fluid Walking");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Restrict Slots");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.VIBRATE, "Vibrate");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Intangibility");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Name Change");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk Immunity");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Fire Aspect");
            this.addAbility(AbilitySerializers.PARTICLES, "Particles");

            // Commands
            this.add(SuperpowerCommand.QUERY_SUCCESS, "%s has the following superpowers: %s");
        }
    }

    public static class German extends PalladiumLangProvider {

        public German(PackOutput packOutput) {
            super(packOutput, "de_de");
        }

        @Override
        protected void addTranslations() {
            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Befehl");
            this.addAbility(AbilitySerializers.RENDER_LAYER, "Render Layer");
//            this.addAbility(AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(AbilitySerializers.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(AbilitySerializers.ATTRIBUTE_MODIFIER, "Attributmodifikator");
            this.addAbility(AbilitySerializers.HEALING, "Heilung");
            this.addAbility(AbilitySerializers.SLOWFALL, "Langsamer Fall");
            this.addAbility(AbilitySerializers.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(AbilitySerializers.INVISIBILITY, "Unsichtbarkeit");
//            this.addAbility(AbilitySerializers.ENERGY_BEAM, "Energiestrahl");
            this.addAbility(AbilitySerializers.SIZE, "Größe");
//            this.addAbility(AbilitySerializers.PROJECTILE, "Projektil");
            this.addAbility(AbilitySerializers.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(AbilitySerializers.AIM, "Zielen");
            this.addAbility(AbilitySerializers.HIDE_BODY_PART, "Körperteile verstecken");
            this.addAbility(AbilitySerializers.REMOVE_BODY_PART, "Körperteile entfernen");
            this.addAbility(AbilitySerializers.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(AbilitySerializers.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(AbilitySerializers.SHOW_BOTH_ARMS, "Beide Arme zeigen");
//            this.addAbility(AbilitySerializers.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(AbilitySerializers.WATER_WALK, "Auf Wasser Laufen");
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Auf Flüssigkeit Laufen");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.VIBRATE, "Vibrieren");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Ungreifbarkeit");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Verbrennung");
            this.addAbility(AbilitySerializers.PARTICLES, "Partikel");
        }
    }

    public static class Saxon extends PalladiumLangProvider {

        public Saxon(PackOutput packOutput) {
            super(packOutput, "sxu");
        }

        @Override
        protected void addTranslations() {
            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Befehl");
            this.addAbility(AbilitySerializers.RENDER_LAYER, "Render Layer");
//            this.addAbility(AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(AbilitySerializers.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(AbilitySerializers.ATTRIBUTE_MODIFIER, "Ättribütmodifikator");
            this.addAbility(AbilitySerializers.HEALING, "Helung");
            this.addAbility(AbilitySerializers.SLOWFALL, "Langsamer Fall");
            this.addAbility(AbilitySerializers.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(AbilitySerializers.INVISIBILITY, "Unsischtbarkeet");
//            this.addAbility(AbilitySerializers.ENERGY_BEAM, "Energiestrahl");
            this.addAbility(AbilitySerializers.SIZE, "Größe");
//            this.addAbility(AbilitySerializers.PROJECTILE, "Projektil");
            this.addAbility(AbilitySerializers.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(AbilitySerializers.AIM, "Zielen");
            this.addAbility(AbilitySerializers.HIDE_BODY_PART, "Görperdeile versteggen");
            this.addAbility(AbilitySerializers.REMOVE_BODY_PART, "Görperdeile entfernen");
            this.addAbility(AbilitySerializers.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(AbilitySerializers.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(AbilitySerializers.SHOW_BOTH_ARMS, "Beide Arme zeijen");
//            this.addAbility(AbilitySerializers.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(AbilitySerializers.WATER_WALK, "Uff Wasser Lofen");
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Uff Flüssichkeht Lofen");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.VIBRATE, "Vibrieren");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Ungreifbarkeht");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Vorbrennung");
            this.addAbility(AbilitySerializers.PARTICLES, "Partikel");
        }
    }

}
