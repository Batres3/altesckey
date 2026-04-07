package com.blakesys.mc.altesckey.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

/**
 * Extend KeyMapping simply so we can access the currently bound code.
 * Which is not otherwise directly accessible in the KeyMapping base class.
 * Only implement the one constructor we care about.
 */
public class CodeKeyMapping extends KeyMapping {
    public CodeKeyMapping(final String name, final InputConstants.Type type, final int value, final KeyMapping.Category category) {
        super(name, type, value, category, 0);
    }

    /**
     * Get the key code that is bound.
     * @return the bound key code.
     */
    public int getCode() {
        return super.key.getValue();
    }

}
