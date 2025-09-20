package wumbologymajor.wumboskiln.init;

import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static wumbologymajor.wumboskiln.WumbosKiln.*;

public class WKItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<BlockItem> KILN = ITEMS.registerSimpleBlockItem(WKBlocks.KILN);
}
