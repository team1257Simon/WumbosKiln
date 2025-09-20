package wumbologymajor.wumboskiln;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import wumbologymajor.wumboskiln.client.gui.screen.KilnScreen;
import wumbologymajor.wumboskiln.data.recipes.DynamicKilnRecipeGenerator;
import wumbologymajor.wumboskiln.data.recipes.KilnRecipeProvider;
import wumbologymajor.wumboskiln.init.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WumbosKiln.MODID)
public class WumbosKiln {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wumboskiln";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public WumbosKiln(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        WKRecipes.RECIPE_TYPES.register(modEventBus);
        WKRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        WKBlocks.BLOCKS.register(modEventBus);
        WKBlocks.BLOCK_ENTITIES.register(modEventBus);
        WKItems.ITEMS.register(modEventBus);
        WKMenus.MENUS.register(modEventBus);
        WKCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (WumbosKiln) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(WKCreativeTab::addToTabs);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, WKConfig.SPEC);
        if(FMLEnvironment.dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @SubscribeEvent
    public void onAddServerReloadListeners(@NotNull AddServerReloadListenersEvent event) {
        event.addListener(modResourceLocation("dynamic_gen"), new DynamicKilnRecipeGenerator.Activator(event));
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation modResourceLocation(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}

        @SubscribeEvent
        public static void onGatherData(@NotNull GatherDataEvent.Client event) {
            LOGGER.info("Generating vanilla kiln smelting recipes");
            event.createProvider(KilnRecipeProvider.Runner::new);
        }

        @SubscribeEvent
        public static void registerScreens(@NotNull RegisterMenuScreensEvent event) {
            LOGGER.info("Registered screen");
            event.register(WKMenus.KILN_MENU.get(), KilnScreen::new);
        }
    }
}
