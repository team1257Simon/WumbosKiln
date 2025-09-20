package wumbologymajor.wumboskiln.init;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import wumbologymajor.wumboskiln.menu.KilnMenu;

import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.*;
import static net.minecraft.world.flag.FeatureFlags.*;
import static wumbologymajor.wumboskiln.WumbosKiln.*;

public class WKMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MENU, MODID);
    public static final Supplier<MenuType<KilnMenu>> KILN_MENU = MENUS.register("kiln_menu", WKMenus::createKilnMenuType);

    @Contract(value = " -> new", pure = true)
    private static @NotNull MenuType<KilnMenu> createKilnMenuType() {
        return new MenuType<>(KilnMenu::new, DEFAULT_FLAGS);
    }
}
