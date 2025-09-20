package wumbologymajor.wumboskiln;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = WumbosKiln.MODID)
public class WKConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    public static final WKConfig CONFIG;
    public static final String GENERATE_DYNAMIC_COMMENT = """
            Whether or not to dynamically generate recipes for the kiln.\s
            This will generate a recipe for any smelting recipe that lacks a blasting or smoking equivalent.""";

    static {
        Pair<WKConfig, ModConfigSpec> pair = BUILDER.configure(WKConfig::new);
        SPEC = pair.getRight();
        CONFIG = pair.getLeft();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }

    public final ModConfigSpec.BooleanValue generateDynamicResources;

    private WKConfig(ModConfigSpec.@NotNull Builder builder) {
        generateDynamicResources = builder.worldRestart()
                .comment(GENERATE_DYNAMIC_COMMENT)
                .define("generate_dynamic", true);
    }
}
