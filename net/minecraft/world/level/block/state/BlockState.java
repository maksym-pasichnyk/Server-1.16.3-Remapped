/*    */ package net.minecraft.world.level.block.state;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ public class BlockState
/*    */   extends BlockBehaviour.BlockStateBase
/*    */ {
/* 14 */   public static final Codec<BlockState> CODEC = codec((Codec<Block>)Registry.BLOCK, Block::defaultBlockState).stable();
/*    */   
/*    */   public BlockState(Block debug1, ImmutableMap<Property<?>, Comparable<?>> debug2, MapCodec<BlockState> debug3) {
/* 17 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState asState() {
/* 22 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\BlockState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */