package exopandora.worldhandler.builder.impl;

import javax.annotation.Nullable;

import exopandora.worldhandler.builder.CommandBuilder;
import exopandora.worldhandler.builder.Syntax;
import exopandora.worldhandler.builder.types.Type;
import exopandora.worldhandler.helper.EnumHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderRecipe extends CommandBuilder
{
	public BuilderRecipe()
	{
		this(null, null, null);
	}
	
	public BuilderRecipe(EnumMode mode, String player, ResourceLocation recipe)
	{
		this.setMode(mode);
		this.setPlayer(player);
		this.setRecipe(recipe);
	}
	
	public void setMode(EnumMode mode)
	{
		this.setNode(0, mode != null ? mode.toString() : null);
	}
	
	@Nullable
	public EnumMode getMode()
	{
		return EnumHelper.valueOf(this.getNodeAsString(0), EnumMode.class);
	}
	
	public void setPlayer(String player)
	{
		this.setNode(1, player);
	}
	
	@Nullable
	public String getPlayer()
	{
		return this.getNodeAsString(1);
	}
	
	public void setRecipe(IRecipe<?> recipe)
	{
		this.setRecipe(recipe.getId());
	}
	
	public void setRecipe(ResourceLocation recipe)
	{
		this.setNode(2, recipe);
	}
	
	@Nullable
	public ResourceLocation getRecipe()
	{
		return this.getNodeAsResourceLocation(2);
	}
	
	public BuilderRecipe getBuilderForMode(EnumMode mode)
	{
		return new BuilderRecipe(mode, this.getPlayer(), this.getRecipe());
	}
	
	@Override
	public String getCommandName()
	{
		return "recipe";
	}
	
	@Override
	public Syntax getSyntax()
	{
		Syntax syntax = new Syntax();
		
		syntax.addRequired("give|take", Type.STRING);
		syntax.addOptional("player", Type.STRING);
		syntax.addOptional("recipe", Type.RESOURCE_LOCATION);
		
		return syntax;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum EnumMode
	{
		GIVE,
		TAKE;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}
