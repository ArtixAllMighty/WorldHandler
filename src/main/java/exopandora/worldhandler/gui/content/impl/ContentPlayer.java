package exopandora.worldhandler.gui.content.impl;

import com.mojang.blaze3d.platform.GlStateManager;

import exopandora.worldhandler.builder.ICommandBuilder;
import exopandora.worldhandler.builder.impl.BuilderGeneric;
import exopandora.worldhandler.builder.impl.BuilderMultiCommand;
import exopandora.worldhandler.builder.impl.BuilderPlayer;
import exopandora.worldhandler.builder.impl.BuilderSpawnpoint;
import exopandora.worldhandler.gui.button.GuiButtonBase;
import exopandora.worldhandler.gui.button.GuiTextFieldTooltip;
import exopandora.worldhandler.gui.category.Categories;
import exopandora.worldhandler.gui.category.Category;
import exopandora.worldhandler.gui.container.Container;
import exopandora.worldhandler.gui.container.impl.GuiWorldHandler;
import exopandora.worldhandler.gui.content.Content;
import exopandora.worldhandler.gui.content.Contents;
import exopandora.worldhandler.helper.ActionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ContentPlayer extends Content
{
	private String selectedMain = "start";
	
	private GuiTextFieldTooltip posXField;
	private GuiTextFieldTooltip posYField;
	private GuiTextFieldTooltip posZField;
	
	private GuiTextFieldTooltip scoreField;
	private GuiTextFieldTooltip coinsField;
	private GuiTextFieldTooltip xpField;
	
	private final BuilderGeneric builderSetworldspawn = new BuilderGeneric("setworldspawn");
	private final BuilderSpawnpoint builderSpawnpoint = new BuilderSpawnpoint();
	private final BuilderPlayer builderKill = new BuilderPlayer("kill");
	private final BuilderGeneric builderClear = new BuilderGeneric("clear");
	
	private final BuilderMultiCommand builderMiscellaneous = new BuilderMultiCommand(this.builderSetworldspawn, this.builderSpawnpoint, this.builderKill, this.builderClear);
	
	@Override
	public ICommandBuilder getCommandBuilder()
	{
		if(this.selectedMain.equals("miscellaneous"))
		{
			return this.builderMiscellaneous;
		}
		
		return null;
	}
	
	@Override
	public void initGui(Container container, int x, int y)
	{
		this.posXField = new GuiTextFieldTooltip(x + 118, y, 114, 20);
		this.posYField = new GuiTextFieldTooltip(x + 118, y + 24, 114, 20);
		this.posZField = new GuiTextFieldTooltip(x + 118, y + 48, 114, 20);
		this.scoreField = new GuiTextFieldTooltip(x + 118, y + 12, 114, 20);
		this.coinsField = new GuiTextFieldTooltip(x + 118, y + 36, 114, 20);
		this.xpField = new GuiTextFieldTooltip(x + 118, y + 60, 114, 20);
		
		this.tick(container);
	}
	
	@Override
	public void initButtons(Container container, int x, int y)
	{
		GuiButtonBase button1;
		GuiButtonBase button2;
		GuiButtonBase button3;
		GuiButtonBase button4;
		
		container.add(new GuiButtonBase(x, y + 96, 114, 20, I18n.format("gui.worldhandler.generic.back"), () -> ActionHelper.back(this)));
		container.add(new GuiButtonBase(x + 118, y + 96, 114, 20, I18n.format("gui.worldhandler.generic.backToGame"), ActionHelper::backToGame));
		
		container.add(button1 = new GuiButtonBase(x, y, 114, 20, I18n.format("gui.worldhandler.entities.player.start"), () ->
		{
			this.selectedMain = "start";
			container.init();
		}));
		container.add(button2 = new GuiButtonBase(x, y + 24, 114, 20, I18n.format("gui.worldhandler.entities.player.score"), () ->
		{
			this.selectedMain = "score";
			container.init();
		}));
		container.add(button3 = new GuiButtonBase(x, y + 48, 114, 20, I18n.format("gui.worldhandler.entities.player.position"), () ->
		{
			this.selectedMain = "position";
			container.init();
		}));
		container.add(button4 = new GuiButtonBase(x, y + 72, 114, 20, I18n.format("gui.worldhandler.entities.player.miscellaneous"), () ->
		{
			this.selectedMain = "miscellaneous";
			container.init();
		}));
		
		if(this.selectedMain.equals("start"))
		{
			button1.active = false;
		}
		else if(this.selectedMain.equals("score"))
		{
			button2.active = false;
		}
		else if(this.selectedMain.equals("position"))
		{
			button3.active = false;
			
			container.add(new GuiButtonBase(x + 118, y + 72, 114, 20, I18n.format("gui.worldhandler.entities.player.position.copy_position"), () ->
			{
				int posX = MathHelper.floor(Minecraft.getInstance().player.posX);
				int posY = MathHelper.floor(Minecraft.getInstance().player.posY);
				int posZ = MathHelper.floor(Minecraft.getInstance().player.posZ);
				
				Minecraft.getInstance().keyboardListener.setClipboardString(posX + " " + posY + " " + posZ);
			}));
		}
		else if(this.selectedMain.equals("miscellaneous"))
		{
			button4.active = false;
			
			container.add(new GuiButtonBase(x + 118, y, 114, 20, TextFormatting.RED + I18n.format("gui.worldhandler.entities.player.miscellaneous.set_spawn"), () ->
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.CONTINUE.withBuilder(this.builderSpawnpoint).withParent(Contents.PLAYER)));
			}));
			container.add(new GuiButtonBase(x + 118, y + 24, 114, 20, TextFormatting.RED + I18n.format("gui.worldhandler.entities.player.miscellaneous.set_global_spawn"), () ->
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.CONTINUE.withBuilder(this.builderSetworldspawn).withParent(Contents.PLAYER)));
			}));
			container.add(new GuiButtonBase(x + 118, y + 48, 114, 20, TextFormatting.RED + I18n.format("gui.worldhandler.entities.player.miscellaneous.kill"), () ->
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.CONTINUE.withBuilder(this.builderKill).withParent(Contents.PLAYER)));
			}));
			container.add(new GuiButtonBase(x + 118, y + 72, 114, 20, TextFormatting.RED + I18n.format("gui.worldhandler.entities.player.miscellaneous.clear_inventory"), () ->
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.CONTINUE.withBuilder(this.builderClear).withParent(Contents.PLAYER)));
			}));
		}
	}
	
	@Override
	public void tick(Container container)
	{
		this.posXField.setText("X: " + MathHelper.floor(Minecraft.getInstance().player.posX));
		this.posYField.setText("Y: " + MathHelper.floor(Minecraft.getInstance().player.posY));
		this.posZField.setText("Z: " + MathHelper.floor(Minecraft.getInstance().player.posZ));
		this.scoreField.setText(I18n.format("gui.worldhandler.entities.player.score") + ": " + Minecraft.getInstance().player.getScore());
		this.coinsField.setText(I18n.format("gui.worldhandler.entities.player.score.experience") + ": " + Minecraft.getInstance().player.experienceLevel + "L");
		this.xpField.setText(I18n.format("gui.worldhandler.entities.player.score.experience_coins") + ": " + Minecraft.getInstance().player.experienceTotal);
	}
	
	@Override
	public void drawScreen(Container container, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		if(this.selectedMain.equals("start"))
		{
			int xPos = x + 175;
			int yPos = y + 82;
			int playerNameWidth = Minecraft.getInstance().fontRenderer.getStringWidth(Minecraft.getInstance().player.getName().getFormattedText()) / 2;
			
			Screen.fill(container.width / 2 - playerNameWidth - 1 + 59, yPos - 74, container.width / 2 + playerNameWidth + 1 + 59, yPos - 65, 0x3F000000);
			Minecraft.getInstance().fontRenderer.drawString(Minecraft.getInstance().player.getName().getFormattedText(), container.width / 2 - playerNameWidth + 59, yPos - 73, 0xE0E0E0);
			
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			InventoryScreen.drawEntityOnScreen(xPos, yPos, 30, xPos - mouseX, yPos - mouseY - 44, Minecraft.getInstance().player);
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		}
		else if(this.selectedMain.equals("score"))
		{
			this.scoreField.renderButton(mouseX, mouseY, partialTicks);
			this.xpField.renderButton(mouseX, mouseY, partialTicks);
			this.coinsField.renderButton(mouseX, mouseY, partialTicks);
		}
		else if(this.selectedMain.equals("position"))
		{
			this.posXField.renderButton(mouseX, mouseY, partialTicks);
			this.posYField.renderButton(mouseX, mouseY, partialTicks);
			this.posZField.renderButton(mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	public void onPlayerNameChanged(String username)
	{
		this.builderSpawnpoint.setPlayer(username);
		this.builderKill.setPlayer(username);
	}
	
	@Override
	public Category getCategory()
	{
		return Categories.PLAYER;
	}
	
	@Override
	public String getTitle()
	{
		return I18n.format("gui.worldhandler.title.player.player");
	}
	
	@Override
	public String getTabTitle()
	{
		return I18n.format("gui.worldhandler.tab.player.player");
	}
	
	@Override
	public Content getActiveContent()
	{
		return Contents.PLAYER;
	}
}
