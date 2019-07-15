package com.latmod.mods.itemfilters.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

/**
 * @author LatvianModder
 */
public interface IPaintable
{
	void paint(BlockState paint, Direction facing, boolean all);

	BlockState getPaint();
}