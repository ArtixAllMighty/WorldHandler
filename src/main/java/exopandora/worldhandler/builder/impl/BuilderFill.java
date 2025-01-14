package exopandora.worldhandler.builder.impl;

import javax.annotation.Nullable;

import exopandora.worldhandler.builder.Syntax;
import exopandora.worldhandler.builder.impl.abstr.BuilderDoubleBlockPos;
import exopandora.worldhandler.builder.types.BlockResourceLocation;
import exopandora.worldhandler.builder.types.CoordinateInt;
import exopandora.worldhandler.builder.types.Type;
import exopandora.worldhandler.helper.BlockHelper;
import exopandora.worldhandler.helper.EnumHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BuilderFill extends BuilderDoubleBlockPos
{
	public BuilderFill()
	{
		
	}
	
	public BuilderFill(BlockResourceLocation block1, EnumBlockFilter filter, BlockResourceLocation block2)
	{
		this(BlockHelper.getPos1(), BlockHelper.getPos2(), block1, filter, block2);
	}
	
	public BuilderFill(BlockPos pos1, BlockPos pos2, BlockResourceLocation block1, EnumBlockFilter handling, BlockResourceLocation block2)
	{
		this.setPosition1(pos1);
		this.setPosition2(pos2);
		this.setBlock1(block1);
		this.setBlockHandling(handling);
		this.setBlock2(block2);
	}
	
	public BuilderFill(CoordinateInt x1, CoordinateInt y1, CoordinateInt z1, CoordinateInt x2, CoordinateInt y2, CoordinateInt z2, BlockResourceLocation block1)
	{
		this.setX1(x1);
		this.setY1(y1);
		this.setZ1(z1);
		this.setX2(x2);
		this.setY2(y2);
		this.setZ2(z2);
		this.setBlock1(block1);
	}
	
	public void setBlock1(String block)
	{
		this.setBlock1(BlockResourceLocation.valueOf(block));
	}
	
	public void setBlock1(BlockResourceLocation resource)
	{
		this.setNode(6, resource);
	}
	
	@Nullable
	public BlockResourceLocation getBlock1()
	{
		return this.getNodeAsBlockResourceLocation(6);
	}
	
	public void setBlockHandling(EnumBlockFilter filter)
	{
		this.setNode(7, filter != null ? filter.toString() : null);
	}
	
	public void setBlock2(String block)
	{
		this.setBlock2(BlockResourceLocation.valueOf(block));
	}
	
	public void setBlock2(BlockResourceLocation resource)
	{
		this.setNode(8, resource);
	}
	
	@Nullable
	public BlockResourceLocation getBlock2()
	{
		return this.getNodeAsBlockResourceLocation(8);
	}
	
	@Nullable
	public EnumBlockFilter getBlockFilter()
	{
		return EnumHelper.valueOf(this.getNodeAsString(7), EnumBlockFilter.class);
	}
	
	public BuilderFill getBuilderForFill()
	{
		return new BuilderFill(this.getBlock1(), null, null);
	}
	
	public BuilderFill getBuilderForReplace()
	{
		return new BuilderFill(this.getBlock2(), EnumBlockFilter.REPLACE, this.getBlock1());
	}
	
	@Override
	public String getCommandName()
	{
		return "fill";
	}
	
	@Override
	public final Syntax getSyntax()
	{
		Syntax syntax = new Syntax();
		
		syntax.addRequired("x1", Type.COORDINATE_INT);
		syntax.addRequired("y1", Type.COORDINATE_INT);
		syntax.addRequired("z1", Type.COORDINATE_INT);
		syntax.addRequired("x2", Type.COORDINATE_INT);
		syntax.addRequired("y2", Type.COORDINATE_INT);
		syntax.addRequired("z2", Type.COORDINATE_INT);
		syntax.addRequired("block", Type.BLOCK_RESOURCE_LOCATION);
		syntax.addOptional("filter", Type.STRING);
		syntax.addOptional("block", Type.BLOCK_RESOURCE_LOCATION, "block");
		
		return syntax;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static enum EnumBlockFilter
	{
		REPLACE,
		DESTROY,
		KEEP,
		HOLLOW,
		OUTLINE;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
}
