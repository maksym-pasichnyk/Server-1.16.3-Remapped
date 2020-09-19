/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class LeaveVineDecorator extends TreeDecorator {
/*    */   protected TreeDecoratorType<?> type() {
/* 19 */     return TreeDecoratorType.LEAVE_VINE;
/*    */   }
/*    */   
/* 22 */   public static final Codec<LeaveVineDecorator> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 24 */   public static final LeaveVineDecorator INSTANCE = new LeaveVineDecorator();
/*    */ 
/*    */   
/*    */   public void place(WorldGenLevel debug1, Random debug2, List<BlockPos> debug3, List<BlockPos> debug4, Set<BlockPos> debug5, BoundingBox debug6) {
/* 28 */     debug4.forEach(debug5 -> {
/*    */           if (debug1.nextInt(4) == 0) {
/*    */             BlockPos debug6 = debug5.west();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6)) {
/*    */               addHangingVine((LevelSimulatedRW)debug2, debug6, VineBlock.EAST, debug3, debug4);
/*    */             }
/*    */           } 
/*    */           if (debug1.nextInt(4) == 0) {
/*    */             BlockPos debug6 = debug5.east();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6)) {
/*    */               addHangingVine((LevelSimulatedRW)debug2, debug6, VineBlock.WEST, debug3, debug4);
/*    */             }
/*    */           } 
/*    */           if (debug1.nextInt(4) == 0) {
/*    */             BlockPos debug6 = debug5.north();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6)) {
/*    */               addHangingVine((LevelSimulatedRW)debug2, debug6, VineBlock.SOUTH, debug3, debug4);
/*    */             }
/*    */           } 
/*    */           if (debug1.nextInt(4) == 0) {
/*    */             BlockPos debug6 = debug5.south();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6)) {
/*    */               addHangingVine((LevelSimulatedRW)debug2, debug6, VineBlock.NORTH, debug3, debug4);
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void addHangingVine(LevelSimulatedRW debug1, BlockPos debug2, BooleanProperty debug3, Set<BlockPos> debug4, BoundingBox debug5) {
/* 60 */     placeVine((LevelWriter)debug1, debug2, debug3, debug4, debug5);
/* 61 */     int debug6 = 4;
/*    */     
/* 63 */     debug2 = debug2.below();
/* 64 */     while (Feature.isAir((LevelSimulatedReader)debug1, debug2) && debug6 > 0) {
/* 65 */       placeVine((LevelWriter)debug1, debug2, debug3, debug4, debug5);
/* 66 */       debug2 = debug2.below();
/* 67 */       debug6--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\LeaveVineDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */