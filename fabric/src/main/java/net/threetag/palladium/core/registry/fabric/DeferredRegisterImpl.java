package net.threetag.palladium.core.registry.fabric;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredRegisterImpl {

    public static <T> DeferredRegister<T> createInternal(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
        return new Impl<>(modid, resourceKey);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions", "rawtypes"})
    public static class Impl<T> extends DeferredRegister<T> {

        private final String modid;
        private final Registry<T> registry;
        private final List<RegistryHolder<T>> entries;

        public Impl(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
            this.modid = modid;
            this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(resourceKey.location());
            this.entries = new ArrayList<>();
        }

        @Override
        public void register() {

        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        @Override
        public <R extends T> RegistryHolder<R> register(String id, Supplier<R> supplier) {
            ResourceKey<R> registeredId = (ResourceKey<R>) ResourceKey.create(this.registry.key(), ResourceLocation.fromNamespaceAndPath(this.modid, id));
            Registry registry1 = this.registry;
            RegistryHolder registryHolder = new RegistryHolderImpl(registeredId, Registry.register(registry1, registeredId, supplier.get()), this.registry);
            this.entries.add(registryHolder);
            return registryHolder;
        }

        @Override
        public Collection<RegistryHolder<T>> getEntries() {
            return ImmutableList.copyOf(this.entries);
        }
    }

    public static class RegistryHolderImpl<T> extends RegistryHolder<T> {

        private final ResourceKey<T> id;
        private final T object;
        private final Holder<T> holder;

        @SuppressWarnings("unchecked")
        public RegistryHolderImpl(ResourceKey<T> id, T object, Registry<T> registry) {
            this.id = id;
            this.object = object;
            this.holder = (Holder<T>) registry.get(id).orElseThrow();
        }

        @Override
        public ResourceLocation getId() {
            return this.id.location();
        }

        @Override
        public @NotNull T value() {
            return this.holder.value();
        }

        @Override
        public boolean isBound() {
            return this.holder.isBound();
        }

        @Override
        public boolean is(ResourceLocation location) {
            return this.holder.is(location);
        }

        @Override
        public boolean is(ResourceKey<T> resourceKey) {
            return this.holder.is(resourceKey);
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> predicate) {
            return this.holder.is(predicate);
        }

        @Override
        public boolean is(TagKey<T> tagKey) {
            return this.holder.is(tagKey);
        }

        @Override
        public boolean is(Holder<T> holder) {
            return this.holder.is(holder);
        }

        @Override
        public @NotNull Stream<TagKey<T>> tags() {
            return this.holder.tags();
        }

        @Override
        public @NotNull Either<ResourceKey<T>, T> unwrap() {
            return this.holder.unwrap();
        }

        @Override
        public @NotNull Optional<ResourceKey<T>> unwrapKey() {
            return this.holder.unwrapKey();
        }

        @Override
        public @NotNull Holder.Kind kind() {
            return this.holder.kind();
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> owner) {
            return this.holder.canSerializeIn(owner);
        }
    }

}
