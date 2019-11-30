package dev.latvian.mods.itemfilters.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class PaintAPI
{
	/**
	 * IPaintable Capability. Use getPaintable() to get an instance.
	 */
	@CapabilityInject(IPaintable.class)
	public static Capability<IPaintable> CAPABILITY;

	@Nullable
	public static IPaintable get(@Nullable ICapabilityProvider provider)
	{
		return provider == null ? null : provider.getCapability(CAPABILITY).orElse(null);
	}
}