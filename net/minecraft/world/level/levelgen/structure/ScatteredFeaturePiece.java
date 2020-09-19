/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*    */ 
/*    */ public abstract class ScatteredFeaturePiece
/*    */   extends StructurePiece {
/*    */   protected final int width;
/*    */   protected final int height;
/*    */   protected final int depth;
/* 17 */   protected int heightPosition = -1;
/*    */   
/*    */   protected ScatteredFeaturePiece(StructurePieceType debug1, Random debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8) {
/* 20 */     super(debug1, 0);
/*    */     
/* 22 */     this.width = debug6;
/* 23 */     this.height = debug7;
/* 24 */     this.depth = debug8;
/*    */     
/* 26 */     setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(debug2));
/*    */     
/* 28 */     if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 29 */       this.boundingBox = new BoundingBox(debug3, debug4, debug5, debug3 + debug6 - 1, debug4 + debug7 - 1, debug5 + debug8 - 1);
/*    */     } else {
/* 31 */       this.boundingBox = new BoundingBox(debug3, debug4, debug5, debug3 + debug8 - 1, debug4 + debug7 - 1, debug5 + debug6 - 1);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected ScatteredFeaturePiece(StructurePieceType debug1, CompoundTag debug2) {
/* 36 */     super(debug1, debug2);
/* 37 */     this.width = debug2.getInt("Width");
/* 38 */     this.height = debug2.getInt("Height");
/* 39 */     this.depth = debug2.getInt("Depth");
/* 40 */     this.heightPosition = debug2.getInt("HPos");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 45 */     debug1.putInt("Width", this.width);
/* 46 */     debug1.putInt("Height", this.height);
/* 47 */     debug1.putInt("Depth", this.depth);
/* 48 */     debug1.putInt("HPos", this.heightPosition);
/*    */   }
/*    */   
/*    */   protected boolean updateAverageGroundHeight(LevelAccessor debug1, BoundingBox debug2, int debug3) {
/* 52 */     if (this.heightPosition >= 0) {
/* 53 */       return true;
/*    */     }
/*    */     
/* 56 */     int debug4 = 0;
/* 57 */     int debug5 = 0;
/* 58 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/* 59 */     for (int debug7 = this.boundingBox.z0; debug7 <= this.boundingBox.z1; debug7++) {
/* 60 */       for (int debug8 = this.boundingBox.x0; debug8 <= this.boundingBox.x1; debug8++) {
/* 61 */         debug6.set(debug8, 64, debug7);
/* 62 */         if (debug2.isInside((Vec3i)debug6)) {
/* 63 */           debug4 += debug1.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)debug6).getY();
/* 64 */           debug5++;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     if (debug5 == 0) {
/* 70 */       return false;
/*    */     }
/* 72 */     this.heightPosition = debug4 / debug5;
/* 73 */     this.boundingBox.move(0, this.heightPosition - this.boundingBox.y0 + debug3, 0);
/* 74 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\ScatteredFeaturePiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */