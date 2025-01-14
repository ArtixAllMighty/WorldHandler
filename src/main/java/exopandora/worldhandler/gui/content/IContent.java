package exopandora.worldhandler.gui.content;

import javax.annotation.Nullable;

import exopandora.worldhandler.builder.ICommandBuilder;
import exopandora.worldhandler.gui.category.Category;
import exopandora.worldhandler.gui.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IContent
{
	default void init(Container container)
	{
		
	}
	
	default void initGui(Container container, int x, int y)
	{
		
	}
	
	void initButtons(Container container, int x, int y);
	
	default void tick(Container container)
	{
		
	}
	
	default void drawScreen(Container container, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		
	}
	
	default void onPlayerNameChanged(String username)
	{
		
	}
	
	Category getCategory();
	
	String getTitle();
	String getTabTitle();
	
	Content getActiveContent();
	
	@Nullable
	default Content getBackContent()
	{
		return Contents.MAIN;
	}
	
	@Nullable
	default ICommandBuilder getCommandBuilder()
	{
		return null;
	}
	
	@Nullable
	default String[] getHeadline()
	{
		return null;
	}
	
	default void onGuiClosed()
	{
		
	}
}
