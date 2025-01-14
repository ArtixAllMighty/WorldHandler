package exopandora.worldhandler.builder.impl;

import exopandora.worldhandler.builder.CommandBuilder;
import exopandora.worldhandler.builder.Syntax;
import exopandora.worldhandler.builder.types.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderTag extends CommandBuilder
{
	public BuilderTag()
	{
		
	}
	
	public BuilderTag(String player, EnumMode mode, String name)
	{
		this.setPlayer(player);
		this.setMode(mode);
		this.setName(name);
	}
	
	public void setPlayer(String player)
	{
		this.setNode(0, player);
	}
	
	public String getPlayer()
	{
		return this.getNodeAsString(0);
	}
	
	public void setMode(EnumMode mode)
	{
		if(mode != null)
		{
			this.setNode(1, mode.toString());
		}
	}
	
	public void setName(String name)
	{
		this.setNode(2, name);
	}
	
	public String getName()
	{
		return this.getNodeAsString(2);
	}
	
	@Override
	public String getCommandName()
	{
		return "tag";
	}
	
	@Override
	public Syntax getSyntax()
	{
		Syntax syntax = new Syntax();
		
		syntax.addRequired("player", Type.STRING);
		syntax.addRequired("add|list|remove", Type.STRING);
		syntax.addRequired("name", Type.STRING);
		
		return syntax;
	}
	
	public BuilderTag getBuilderForMode(EnumMode mode)
	{
		return new BuilderTag(this.getPlayer(), mode, this.getName());
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum EnumMode
	{
		ADD,
		LIST,
		REMOVE;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}
