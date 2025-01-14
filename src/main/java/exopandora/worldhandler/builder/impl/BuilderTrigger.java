package exopandora.worldhandler.builder.impl;

import exopandora.worldhandler.builder.CommandBuilder;
import exopandora.worldhandler.builder.Syntax;
import exopandora.worldhandler.builder.types.Type;
import exopandora.worldhandler.helper.EnumHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderTrigger extends CommandBuilder
{
	public BuilderTrigger()
	{
		this.setValue(0);
	}
	
	public BuilderTrigger(String objective, EnumMode mode, int value)
	{
		this.setObjective(objective);
		this.setMode(mode);
		this.setValue(value);
	}
	
	public void setObjective(String name)
	{
		this.setNode(0, name != null ? name.replaceAll(" ", "_") : null);
	}
	
	public String getObjective()
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
	
	public EnumMode getMode()
	{
		return EnumHelper.valueOf(this.getNodeAsString(1), EnumMode.class);
	}
	
	public void setValue(int value)
	{
		this.setNode(2, value);
	}
	
	public int getValue()
	{
		return this.getNodeAsInt(2);
	}
	
	@Override
	public String getCommandName()
	{
		return "trigger";
	}
	
	@Override
	public Syntax getSyntax()
	{
		Syntax syntax = new Syntax();
		
		syntax.addRequired("objective", Type.STRING);
		syntax.addRequired("add|set", Type.STRING);
		syntax.addRequired("value", Type.INT);
		
		return syntax;
	}
	
	public BuilderTrigger getBuilderForMode(EnumMode mode)
	{
		return new BuilderTrigger(this.getObjective(), mode, this.getValue());
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum EnumMode
	{
		ADD,
		SET;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}
