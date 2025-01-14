package exopandora.worldhandler.gui.content;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import exopandora.worldhandler.Main;
import exopandora.worldhandler.gui.content.impl.ContentAdvancements;
import exopandora.worldhandler.gui.content.impl.ContentButcher;
import exopandora.worldhandler.gui.content.impl.ContentButcherSettings;
import exopandora.worldhandler.gui.content.impl.ContentChangeWorld;
import exopandora.worldhandler.gui.content.impl.ContentContainers;
import exopandora.worldhandler.gui.content.impl.ContentContinue;
import exopandora.worldhandler.gui.content.impl.ContentCustomItem;
import exopandora.worldhandler.gui.content.impl.ContentEditBlocks;
import exopandora.worldhandler.gui.content.impl.ContentEnchantment;
import exopandora.worldhandler.gui.content.impl.ContentExperience;
import exopandora.worldhandler.gui.content.impl.ContentGamerules;
import exopandora.worldhandler.gui.content.impl.ContentMain;
import exopandora.worldhandler.gui.content.impl.ContentMultiplayer;
import exopandora.worldhandler.gui.content.impl.ContentNoteEditor;
import exopandora.worldhandler.gui.content.impl.ContentPlayer;
import exopandora.worldhandler.gui.content.impl.ContentPotions;
import exopandora.worldhandler.gui.content.impl.ContentRecipes;
import exopandora.worldhandler.gui.content.impl.ContentScoreboardObjectives;
import exopandora.worldhandler.gui.content.impl.ContentScoreboardPlayers;
import exopandora.worldhandler.gui.content.impl.ContentScoreboardTeams;
import exopandora.worldhandler.gui.content.impl.ContentSettings;
import exopandora.worldhandler.gui.content.impl.ContentSignEditor;
import exopandora.worldhandler.gui.content.impl.ContentSummon;
import exopandora.worldhandler.gui.content.impl.ContentWorldInfo;
import exopandora.worldhandler.helper.RegistryHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent.NewRegistry;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@OnlyIn(Dist.CLIENT)
public abstract class Content extends ForgeRegistryEntry<Content> implements IContent
{
	public static IForgeRegistry<Content> REGISTRY;
	
	@SubscribeEvent
	public static void createRegistry(NewRegistry event)
	{
		REGISTRY = new RegistryBuilder<Content>()
				.setType(Content.class)
				.setName(new ResourceLocation(Main.MODID + "_content"))
				.disableSaving()
				.disableSync()
				.create();
	}
	
	@SubscribeEvent
	public static void register(Register<Content> event)
	{
		//MAIN
		RegistryHelper.register(event.getRegistry(), "main", new ContentMain());
		RegistryHelper.register(event.getRegistry(), "containers", new ContentContainers());
		RegistryHelper.register(event.getRegistry(), "multiplayer", new ContentMultiplayer());
		
		//ENTITIES
		RegistryHelper.register(event.getRegistry(), "summon", new ContentSummon());
		
		//ITEMS
		RegistryHelper.register(event.getRegistry(), "custom_item", new ContentCustomItem());
		RegistryHelper.register(event.getRegistry(), "enchantment", new ContentEnchantment());
		RegistryHelper.register(event.getRegistry(), "recipes", new ContentRecipes());
		
		//BLOCKS
		RegistryHelper.register(event.getRegistry(), "edit_blocks", new ContentEditBlocks());
		RegistryHelper.register(event.getRegistry(), "sign_editor", new ContentSignEditor());
		RegistryHelper.register(event.getRegistry(), "note_editor", new ContentNoteEditor());
		
		//WORLD
		RegistryHelper.register(event.getRegistry(), "world", new ContentWorldInfo());
		RegistryHelper.register(event.getRegistry(), "gamerules", new ContentGamerules());
		
		//PLAYER
		RegistryHelper.register(event.getRegistry(), "player", new ContentPlayer());
		RegistryHelper.register(event.getRegistry(), "experience", new ContentExperience());
		RegistryHelper.register(event.getRegistry(), "advancements", new ContentAdvancements());
		
		//SCOREBOARD
		RegistryHelper.register(event.getRegistry(), "scoreboard_objectives", new ContentScoreboardObjectives());
		RegistryHelper.register(event.getRegistry(), "scoreboard_teams", new ContentScoreboardTeams());
		RegistryHelper.register(event.getRegistry(), "scoreboard_players", new ContentScoreboardPlayers());
		
		//MISC
		RegistryHelper.register(event.getRegistry(), "change_world", new ContentChangeWorld());
		RegistryHelper.register(event.getRegistry(), "continue", new ContentContinue());
		
		//NO CATEGORY
		RegistryHelper.register(event.getRegistry(), "potions", new ContentPotions());
		RegistryHelper.register(event.getRegistry(), "butcher", new ContentButcher());
		RegistryHelper.register(event.getRegistry(), "butcher_settings", new ContentButcherSettings());
		RegistryHelper.register(event.getRegistry(), "settings", new ContentSettings());
	}
	
	private Map<String, Object> persistence;
	
	@SuppressWarnings("unchecked")
	public <T> T getPersistence(String id, Supplier<T> supplier)
	{
		if(this.persistence == null)
		{
			this.persistence = new HashMap<String, Object>();
		}
		
		if(this.persistence.containsKey(id))
		{
			return (T) this.persistence.get(id);
		}
		
		T object = supplier.get();
		this.persistence.put(id, object);
		
		return object;
	}
}
