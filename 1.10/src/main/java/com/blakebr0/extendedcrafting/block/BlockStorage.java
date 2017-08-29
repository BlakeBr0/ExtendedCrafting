package com.blakebr0.extendedcrafting.block;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStorage extends BlockBase {
	
    public static final PropertyEnum<Type> VARIANT = PropertyEnum.<Type>create("variant", Type.class);

    public static ItemStack blockInferiumCoal;
    public static ItemStack blockPrudentiumCoal;
    public static ItemStack blockIntermediumCoal;
    public static ItemStack blockSuperiumCoal;
    public static ItemStack blockSupremiumCoal;
    
    public BlockStorage(){
        super("storage", Material.ROCK, SoundType.STONE, 5.0F, 10.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.BLACK_IRON));
    }
    
    @Override
    public void init(){
    	GameRegistry.register(this);
    	GameRegistry.register(new ItemBlockStorage(this).setRegistryName(getRegistryName()));
    	
    }

    @Override
    public int damageDropped(IBlockState state){
        return ((Type)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> stacks){
        for(Type type : Type.values()){
            stacks.add(new ItemStack(item, 1, type.getMetadata()));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void initModels(){
    	for(Type type : Type.values()){
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), type.getMetadata(), new ModelResourceLocation(getRegistryName().toString() + "_" + type.byMetadata(type.getMetadata()).getName()));
    	}
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(VARIANT, Type.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return ((Type)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] { VARIANT });
    }
    
	public static enum Type implements IStringSerializable {	
		
		BLACK_IRON(0, "black_iron"),
		LUMINOUS_IRON(1, "luminous_iron"),
		LUMINOUS_GOLD(2, "luminous_gold"),
		LUMINOUS_DIAMOND(3, "luminous_diamond"),
		LUMINOUS_EMERALD(4, "luminous_emerald");
		
        private static final Type[] META_LOOKUP = new Type[values().length];
		private final int meta;
		private final String name;
		
		Type(int meta, String name){
			this.meta = meta;
			this.name = name;
		}
		
		public int getMetadata(){
			return this.meta;
		}

		@Override
		public String getName(){
			return this.name;
		}
		
        public static Type byMetadata(int meta){
            if(meta < 0 || meta >= META_LOOKUP.length){
                meta = 0;
            }
            return META_LOOKUP[meta];
        }

        static {
            for(Type type : values()){
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
	}
}