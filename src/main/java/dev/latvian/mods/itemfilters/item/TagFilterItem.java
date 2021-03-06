package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TagFilterItem extends StringValueFilterItem
{
	public static class TagData extends StringValueData<Tag<Item>>
	{
		public TagData(ItemStack is)
		{
			super(is);
		}

		@Nullable
		@Override
		public Tag<Item> fromString(String s)
		{
			return ItemTags.getCollection().get(new ResourceLocation(s));
		}

		@Override
		public String toString(Tag<Item> value)
		{
			return value.getId().toString();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new TagData(stack);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		return item.getItem().getTags().contains(new ResourceLocation(getValue(filter)));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack filter)
	{
		List<StringValueFilterVariant> list = new ArrayList<>();

		for (ResourceLocation id : ItemTags.getCollection().getRegisteredTags())
		{
			Tag<Item> tag = ItemTags.getCollection().get(id);

			if (tag != null && !tag.getAllElements().isEmpty())
			{
				StringValueFilterVariant variant = new StringValueFilterVariant(id.toString());
				variant.icon = new ItemStack(tag.getAllElements().iterator().next());
				list.add(variant);
			}
		}

		return list;
	}

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		Tag<Item> items = ItemTags.getCollection().get(new ResourceLocation(getValue(filter)));

		if (items == null || items.getAllElements().isEmpty())
		{
			return;
		}

		NonNullList<ItemStack> list1 = NonNullList.create();

		for (Item item : items.getAllElements())
		{
			item.fillItemGroup(ItemGroup.SEARCH, list1);
		}

		list.addAll(list1);
	}
}