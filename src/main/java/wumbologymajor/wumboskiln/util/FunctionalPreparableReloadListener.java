package wumbologymajor.wumboskiln.util;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public abstract class FunctionalPreparableReloadListener<T extends BiConsumer<ResourceManager, ProfilerFiller>>
        extends SimplePreparableReloadListener<T> {
    @Override
    protected void apply(T t, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        t.accept(resourceManager, profilerFiller);
    }
}
