/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public abstract class TreeDecorator {
/* 19 */   public static final Codec<TreeDecorator> CODEC = Registry.TREE_DECORATOR_TYPES.dispatch(TreeDecorator::type, TreeDecoratorType::codec);
/*    */   
/*    */   protected abstract TreeDecoratorType<?> type();
/*    */   
/*    */   public abstract void place(WorldGenLevel paramWorldGenLevel, Random paramRandom, List<BlockPos> paramList1, List<BlockPos> paramList2, Set<BlockPos> paramSet, BoundingBox paramBoundingBox);
/*    */   
/*    */   protected void placeVine(LevelWriter debug1, BlockPos debug2, BooleanProperty debug3, Set<BlockPos> debug4, BoundingBox debug5) {
/* 26 */     setBlock(debug1, debug2, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)debug3, Boolean.valueOf(true)), debug4, debug5);
/*    */   }
/*    */   
/*    */   protected void setBlock(LevelWriter debug1, BlockPos debug2, BlockState debug3, Set<BlockPos> debug4, BoundingBox debug5) {
/* 30 */     debug1.setBlock(debug2, debug3, 19);
/* 31 */     debug4.add(debug2);
/* 32 */     debug5.expand(new BoundingBox((Vec3i)debug2, (Vec3i)debug2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TreeDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */