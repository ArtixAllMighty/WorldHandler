package exopandora.worldhandler.text;

import javax.annotation.Nullable;

import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SignText
{
	private MutableStringTextComponent text = new MutableStringTextComponent();
	private final int line;
	
	public SignText(int line)
	{
		this.line = line;
	}
	
	public int getLine()
	{
		return this.line;
	}
	
	public MutableStringTextComponent getString()
	{
		return this.text;
	}
	
	public void setString(MutableStringTextComponent string)
	{
		this.text = string;
	}
	
	public void setCommand(String command)
	{
		if(command != null && !command.isEmpty())
		{
			this.text.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		}
		else
		{
			this.text.getStyle().setClickEvent(null);
		}
	}
	
	@Nullable
	public String getCommand()
	{
		if(this.hasCommand())
		{
			return this.text.getStyle().getClickEvent().getValue();
		}
		
		return null;
	}
	
	public boolean hasCommand()
	{
		return this.text.getStyle().getClickEvent() != null && this.text.getStyle().getClickEvent().getAction() == Action.RUN_COMMAND && this.text.getStyle().getClickEvent().getValue() != null;
	}
	
	@Override
	public String toString()
	{
		if(this.text.getUnformattedComponentText().isEmpty())
		{
			return this.text.getUnformattedComponentText();
		}
		
		if(this.text.getStyle().isEmpty() && !this.hasCommand())
		{
			return this.text.getFormattedText();
		}
		
		return this.text.serialize();
	}
}
