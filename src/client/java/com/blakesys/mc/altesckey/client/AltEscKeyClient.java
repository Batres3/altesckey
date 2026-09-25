package com.blakesys.mc.altesckey.client;

import com.blakesys.mc.altesckey.AltEscKey;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class AltEscKeyClient implements ClientModInitializer {
    public static CodeKeyMapping ESC_KEYBIND;
    public static CodeKeyMapping ALT_ESC_KEYBIND;

    // Optional: custom category (recommended for organization)
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(AltEscKey.MOD_ID, "main")
    );

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        ALT_ESC_KEYBIND = new CodeKeyMapping(
                "key.altesckey.altesc",     // Translation key for the name (shown in controls menu)
                InputConstants.Type.KEYBOARD,       // KEYSYM = keyboard, MOUSE = mouse button
                InputConstants.UNKNOWN.getValue(),            // Default keycode is unbound (aka unknown)
                CATEGORY                  // Category (group in controls menu)
        );


        KeyMappingHelper.registerKeyMapping(ALT_ESC_KEYBIND);



        ESC_KEYBIND = new CodeKeyMapping(
                "key.altesckey.esc",     // Translation key for the name (shown in controls menu)
                InputConstants.Type.KEYBOARD,       // KEYSYM = keyboard, MOUSE = mouse button
                InputConstants.KEY_ESCAPE,            // Default keycode is unbound (aka unknown)
                CATEGORY                  // Category (group in controls menu)
        );
        KeyMappingHelper.registerKeyMapping(ESC_KEYBIND);


    }
}
