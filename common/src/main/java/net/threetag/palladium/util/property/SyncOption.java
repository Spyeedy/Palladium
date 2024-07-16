package net.threetag.palladium.util.property;

public enum SyncOption {

    NONE, SELF, EVERYONE;

    public SyncOption add(SyncOption newSync) {
        return newSync.ordinal() > this.ordinal() ? newSync : this;
    }

}
