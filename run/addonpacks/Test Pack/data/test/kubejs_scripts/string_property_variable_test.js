// Firstly, register the property in the entity
PalladiumEvents.registerProperties((event) => {
    // Only register for players
    if (event.getEntityType() === "minecraft:player") {
        // Arguments: Key of the property, type of the property, default/starting value
        event.registerProperty('standing_on_block', 'string', 'minecraft:air');
    }
});

PlayerEvents.tick((event) => {
    if (palladium.hasProperty(event.player, 'standing_on_block') && abilityUtil.hasPower(event.player, 'test:string_property_variable_test')) {
        if (event.player.isCrouching()) event.player.tell(palladium.getProperty(event.player, 'standing_on_block'));
        palladium.setProperty(event.player, 'standing_on_block', event.player.getBlock().getDown().getId().split(":", 2)[1]);
        // this obviously will break with a ton of multi-textured blocks and whatnot but it's just a proof of concept
    }
})