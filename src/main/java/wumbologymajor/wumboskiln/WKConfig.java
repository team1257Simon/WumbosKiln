package wumbologymajor.wumboskiln;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static wumbologymajor.wumboskiln.WumbosKiln.LOGGER;

@EventBusSubscriber(modid = WumbosKiln.MODID)
public class WKConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final WKConfig CONFIG;
    private static Set<String> dynamicGenerationBlacklist = Set.of();
    private static final String GENERATE_DYNAMIC_COMMENT = """
            Whether to dynamically generate recipes for the kiln.\s
            This will generate a recipe for any smelting recipe that lacks a blasting or smoking equivalent.""";
    private static final String GENERATION_BLACKLIST_COMMENT = """
            Recipes to exclude from dynamic generation.\s
            Any recipe whose output is listed here will be ignored when generating kiln smelting recipes.\s
            The namespace can be omitted for vanilla items (e.g. minecraft:charcoal).""";
    static {
        Pair<WKConfig, ModConfigSpec> pair = BUILDER.configure(WKConfig::new);
        SPEC = pair.getRight();
        CONFIG = pair.getLeft();
    }
    public static @Unmodifiable @NotNull Set<String> getRecipeBlacklist() {
        return dynamicGenerationBlacklist;
    }
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        LOGGER.debug("RecipeGen Blacklist Entries: {}", CONFIG.generationBlacklist.get().size());
        dynamicGenerationBlacklist = CONFIG.generationBlacklist.get().stream().collect(toUnmodifiableSet());
    }
    public final ModConfigSpec.BooleanValue generateDynamicResources;
    public final ModConfigSpec.ConfigValue<List<? extends String>> generationBlacklist;
    private WKConfig(ModConfigSpec.@NotNull Builder builder) {
        generateDynamicResources = builder.worldRestart()
                .comment(GENERATE_DYNAMIC_COMMENT)
                .define("generate_dynamic", true);
        generationBlacklist = builder.worldRestart()
                .comment(GENERATION_BLACKLIST_COMMENT)
                .defineListAllowEmpty("generation_blacklist", List.of("charcoal", "lime_dye", "green_dye", "popped_chorus_fruit"), String::new, this::validLocationString);
    }
    private boolean validLocationString(Object o) {
        if(o instanceof String s) {
            return s.matches("([a-z0-9_.-]*)(:[a-z0-9_.-/]*)?");
        } else return false;
    }
}
