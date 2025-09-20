package wumbologymajor.wumboskiln.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import wumbologymajor.wumboskiln.WumbosKiln;

import static net.minecraft.world.item.CreativeModeTab.TabVisibility.*;
import static net.minecraft.world.item.Items.*;
import static wumbologymajor.wumboskiln.init.WKItems.*;

public class WKCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WumbosKiln.MODID);

    static {
        CREATIVE_MODE_TABS.register("blocks", WKCreativeTab::blocksTab);
    }

    public static void addToTabs(@NotNull BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.insertAfter(new ItemStack(BLAST_FURNACE), new ItemStack(KILN.asItem()), PARENT_AND_SEARCH_TABS);
        }
    }

    private static @NotNull CreativeModeTab blocksTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.wumboskiln"))
                .displayItems(WKCreativeTab::putItem)
                .build();
    }

    private static void putItem(ItemDisplayParameters parameters, @NotNull Output output) {
        output.accept(KILN);
    }
}
