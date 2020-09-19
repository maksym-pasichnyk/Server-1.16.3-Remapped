/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public abstract class BeardedStructureStart<C extends FeatureConfiguration>
/*    */   extends StructureStart<C> {
/*    */   public BeardedStructureStart(StructureFeature<C> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/*  9 */     super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void calculateBoundingBox() {
/* 14 */     super.calculateBoundingBox();
/*    */ 
/*    */     
/* 17 */     int debug1 = 12;
/*    */     
/* 19 */     this.boundingBox.x0 -= 12;
/* 20 */     this.boundingBox.y0 -= 12;
/* 21 */     this.boundingBox.z0 -= 12;
/* 22 */     this.boundingBox.x1 += 12;
/* 23 */     this.boundingBox.y1 += 12;
/* 24 */     this.boundingBox.z1 += 12;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\BeardedStructureStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */