package com.blakesys.mc.altesckey.client.mixin;

import net.minecraft.client.input.InputWithModifiers;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.blakesys.mc.altesckey.client.AltEscKeyClient.ESC_KEYBIND;
import static com.blakesys.mc.altesckey.client.AltEscKeyClient.ALT_ESC_KEYBIND;

@Mixin(InputWithModifiers.class)
public interface AltEscKeyMixin {

	@Shadow
	int input();

	/**
	 * This is the Main logic of the mod.
	 * @author Arthur Blake
	 * @reason Add ability to detect a configured alternate escape key.
	 */
	@Overwrite
	default boolean isEscape() {
		final int i = this.input();
		final boolean escBound = !ESC_KEYBIND.isUnbound();
		final boolean altBound = !ALT_ESC_KEYBIND.isUnbound();

		// main logic for the mod
		if (escBound || altBound) {
			return (escBound && i == ESC_KEYBIND.getCode()) ||
							(altBound && i == ALT_ESC_KEYBIND.getCode());
		}

		// safety in case there is no key bind to Escape at all!
		// this otherwise could put the user in quite a pickle
		// as they would not be able to cleanly pause or quit the game!

		// we might add an option later in case this is actually what the user
		// wants. although I'm not sure why they would want this...
		return i == GLFW.GLFW_KEY_ESCAPE;
	}
}