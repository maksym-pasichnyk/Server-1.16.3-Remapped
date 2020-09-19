/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class StainedGlassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {
/*    */   public StainedGlassPaneBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/*  9 */     super(debug2);
/* 10 */     this.color = debug1;
/* 11 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */   private final DyeColor color;
/*    */   
/*    */   public DyeColor getColor() {
/* 16 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StainedGlassPaneBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */