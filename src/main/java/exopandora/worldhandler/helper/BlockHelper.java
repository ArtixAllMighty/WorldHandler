package exopandora.worldhandler.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import exopandora.worldhandler.builder.impl.BuilderFill;
import exopandora.worldhandler.builder.impl.BuilderSetBlock;
import exopandora.worldhandler.builder.impl.BuilderSummon;
import exopandora.worldhandler.builder.types.BlockResourceLocation;
import exopandora.worldhandler.builder.types.Coordinate.CoordinateType;
import exopandora.worldhandler.builder.types.CoordinateDouble;
import exopandora.worldhandler.builder.types.CoordinateInt;
import exopandora.worldhandler.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.client.CUpdateCommandBlockPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlockHelper
{
	private static BlockPos POS_1 = BlockPos.ZERO;
	private static BlockPos POS_2 = BlockPos.ZERO;
	private static final List<Consumer<BlockPos>> POS_1_OBSERVERS = new ArrayList<Consumer<BlockPos>>();
	private static final List<Consumer<BlockPos>> POS_2_OBSERVERS = new ArrayList<Consumer<BlockPos>>();
	private static final Block[] BLACKLIST = new Block[] {Blocks.AIR, Blocks.WATER, Blocks.LAVA};
	
	public static BlockPos getFocusedBlockPos()
	{
		RayTraceResult result = Minecraft.getInstance().objectMouseOver;
		
		if(result != null && result.getType().equals(Type.BLOCK))
		{
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			
			if(!ArrayUtils.contains(BLACKLIST, Minecraft.getInstance().world.getBlockState(blockResult.getPos()).getBlock()))
			{
				return blockResult.getPos();
			}
		}
		
		return Minecraft.getInstance().player.getPosition();
	}
	
	public static Block getFocusedBlock()
	{
		return Minecraft.getInstance().world.getBlockState(getFocusedBlockPos()).getBlock();
	}
	
	public static Block getBlock(BlockPos pos)
	{
		return Minecraft.getInstance().world.getBlockState(pos).getBlock();
	}
	
	public static BlockPos setX(BlockPos pos, double x)
	{
		return new BlockPos(x, pos.getY(), pos.getZ());
	}
	
	public static BlockPos setY(BlockPos pos, double y)
	{
		return new BlockPos(pos.getX(), y, pos.getZ());
	}
	
	public static BlockPos setZ(BlockPos pos, double z)
	{
		return new BlockPos(pos.getX(), pos.getY(), z);
	}
	
	@Nonnull
	public static BlockPos getPos1()
	{
		return POS_1;
	}
	
	public static void setPos1(BlockPos pos1)
	{
		if(POS_1 != null && !POS_1.equals(pos1))
		{
			POS_1 = pos1;
			
			for(Consumer<BlockPos> observer : POS_1_OBSERVERS)
			{
				observer.accept(POS_1);
			}
		}
	}
	
	@Nonnull
	public static BlockPos getPos2()
	{
		return POS_2;
	}
	
	public static void setPos2(BlockPos pos2)
	{
		if(POS_2 != null && !POS_2.equals(pos2))
		{
			POS_2 = pos2;
			
			for(Consumer<BlockPos> observer : POS_2_OBSERVERS)
			{
				observer.accept(POS_2);
			}
		}
	}
	
	public static <T> T addPositionObservers(T observer, Function<T, Consumer<BlockPos>> pos1generator, Function<T, Consumer<BlockPos>> pos2generator)
	{
		BlockHelper.addPos1Observer(pos1generator.apply(observer));
		BlockHelper.addPos2Observer(pos2generator.apply(observer));
		return observer;
	}
	
	public static void addPos1Observer(Consumer<BlockPos> observer)
	{
		POS_1_OBSERVERS.add(observer);
	}
	
	public static void removePos1Observer(Consumer<BlockPos> observer)
	{
		POS_1_OBSERVERS.remove(observer);
	}
	
	public static void addPos2Observer(Consumer<BlockPos> observer)
	{
		POS_2_OBSERVERS.add(observer);
	}
	
	public static void removePos2Observer(Consumer<BlockPos> observer)
	{
		POS_2_OBSERVERS.add(observer);
	}
	
	private static CompoundNBT newCommandBlock(String command)
	{
		CompoundNBT blockState = new CompoundNBT();
		blockState.putString("Name", Blocks.COMMAND_BLOCK.getRegistryName().toString());
		
		CompoundNBT tileEntityData = new CompoundNBT();
		tileEntityData.putString("Command", command);
		tileEntityData.putBoolean("auto", true);
		
		CompoundNBT commandBlock = new CompoundNBT();
		commandBlock.putInt("Time", 1);
		commandBlock.put("BlockState", blockState);
		commandBlock.put("TileEntityData", tileEntityData);
		
		return commandBlock;
	}
	
	public static boolean setCommandBlockNearPlayer(String command)
	{
		if(CommandHelper.canPlayerIssueCommand() && Minecraft.getInstance().getConnection() != null)
		{
			BuilderFill fill = new BuilderFill();
			fill.setX1(new CoordinateInt(0, CoordinateType.GLOBAL));
			fill.setY1(new CoordinateInt(-2, CoordinateType.GLOBAL));
			fill.setZ1(new CoordinateInt(0, CoordinateType.GLOBAL));
			fill.setX2(new CoordinateInt(0, CoordinateType.GLOBAL));
			fill.setY2(new CoordinateInt(0, CoordinateType.GLOBAL));
			fill.setZ2(new CoordinateInt(0, CoordinateType.GLOBAL));
			fill.setBlock1(new BlockResourceLocation(Blocks.AIR.getRegistryName()));
			
			CompoundNBT executer = newCommandBlock(command);
			executer.putString("id", "falling_block");
			
			ListNBT passengers = new ListNBT();
			passengers.add(executer);
			
			CompoundNBT remover = newCommandBlock(fill.toActualCommand());
			remover.put("Passengers", passengers);
			
			ListNBT motion = new ListNBT();
			motion.add(new DoubleNBT(0.0D));
			motion.add(new DoubleNBT(0.315D));
			motion.add(new DoubleNBT(0.0D));
			remover.put("Motion", motion);
			
			Minecraft.getInstance().displayGuiScreen(null);
			Minecraft.getInstance().mouseHelper.grabMouse();
			
			BuilderSummon summon = new BuilderSummon();
			summon.setEntity(EntityType.FALLING_BLOCK.getRegistryName().toString());
			summon.setX(new CoordinateDouble(0.0, CoordinateType.GLOBAL));
			summon.setY(new CoordinateDouble(0.5, CoordinateType.GLOBAL));
			summon.setZ(new CoordinateDouble(0.0, CoordinateType.GLOBAL));
			summon.setNBT(remover);
			
			BlockPos pos = Minecraft.getInstance().player.getPosition().add(0, 3, 0);
			Minecraft.getInstance().player.sendChatMessage(new BuilderSetBlock(pos, Blocks.COMMAND_BLOCK.getRegistryName(), Config.CLIENT.getSettings().getBlockPlacingMode()).toActualCommand());
			Minecraft.getInstance().getConnection().sendPacket(new CUpdateCommandBlockPacket(pos, summon.toActualCommand(false), CommandBlockTileEntity.Mode.REDSTONE, true, false, true));
			
			return true;
		}
		
		return false;
	}
	
	public static void setBlockNearPlayer(Block block)
	{
		switch(Minecraft.getInstance().player.getHorizontalFacing())
		{
			case NORTH:
				CommandHelper.sendCommand(new BuilderSetBlock(new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(2, CoordinateType.LOCAL), block.getRegistryName(), Config.getSettings().getBlockPlacingMode()).withState(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
				break;
			case EAST:
				CommandHelper.sendCommand(new BuilderSetBlock(new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(2, CoordinateType.LOCAL), block.getRegistryName(), Config.getSettings().getBlockPlacingMode()).withState(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
				break;
			case SOUTH:
				CommandHelper.sendCommand(new BuilderSetBlock(new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(2, CoordinateType.LOCAL), block.getRegistryName(), Config.getSettings().getBlockPlacingMode()).withState(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
				break;
			case WEST:
				CommandHelper.sendCommand(new BuilderSetBlock(new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(CoordinateType.LOCAL), new CoordinateInt(2, CoordinateType.LOCAL), block.getRegistryName(), Config.getSettings().getBlockPlacingMode()).withState(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
				break;
			default:
				break;
		}
	}
}
