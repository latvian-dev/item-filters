package dev.latvian.mods.itemfilters.item;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class StrongNBTFilterItem extends StringValueFilterItem {
	public static class NBTData extends StringValueData<CompoundTag> {
		public NBTData(ItemStack is) {
			super(is);
		}

		@Nullable
		@Override
		protected CompoundTag fromString(String s) {
			if (s.isEmpty()) {
				return null;
			}

			try {
				return TagParser.parseTag(s);
			} catch (Exception ex) {
				return null;
			}
		}

		@Override
		protected String toString(CompoundTag value) {
			if (value == null) {
				return "";
			}

			return value.toString();
		}

		@Override
		@Nullable
		public CompoundTag getValue() {
			if (load) {
				load = false;
				value = null;

				if (filter.hasTag() && filter.getTag().contains("value")) {
					value = filter.getTag().getCompound("value");
				}
			}

			return value;
		}

		@Override
		public void setValue(@Nullable CompoundTag v) {
			value = v;
			load = false;

			if (value == null) {
				filter.removeTagKey("value");
			} else {
				filter.addTagElement("value", value);
			}
		}

		@Override
		public Component getValueAsComponent() {
			CompoundTag v = getValue();

			if (v == null) {
				return TextComponent.EMPTY;
			}

			return NbtUtils.toPrettyComponent(v);
		}
	}

	@Override
	public StringValueData<?> createData(ItemStack stack) {
		return new NBTData(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty() && player.getOffhandItem().hasTag()) {
			NBTData data = getStringValueData(player.getMainHandItem());
			data.setValue(player.getOffhandItem().getTag().copy());
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getMainHandItem());
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		NBTData data = getStringValueData(filter);
		CompoundTag tag1 = data.getValue();
		CompoundTag tag2 = stack.getTag();
		return Objects.equals(tag1, tag2);
	}

	@Override
	public String getHelpKey() {
		return "itemfilters.help_text.nbt";
	}

    @Override
    public void getItems(ItemStack filter, Set<Item> set) {
		// any item could potentially have NBT, so we need the lot
		Registry.ITEM.forEach(set::add);
    }
}