/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class JigsawJunction
/*    */ {
/*    */   private final int sourceX;
/*    */   private final int sourceGroundY;
/*    */   
/*    */   public JigsawJunction(int debug1, int debug2, int debug3, int debug4, StructureTemplatePool.Projection debug5) {
/* 15 */     this.sourceX = debug1;
/* 16 */     this.sourceGroundY = debug2;
/* 17 */     this.sourceZ = debug3;
/* 18 */     this.deltaY = debug4;
/* 19 */     this.destProjection = debug5;
/*    */   }
/*    */   private final int sourceZ; private final int deltaY; private final StructureTemplatePool.Projection destProjection;
/*    */   public int getSourceX() {
/* 23 */     return this.sourceX;
/*    */   }
/*    */   
/*    */   public int getSourceGroundY() {
/* 27 */     return this.sourceGroundY;
/*    */   }
/*    */   
/*    */   public int getSourceZ() {
/* 31 */     return this.sourceZ;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> Dynamic<T> serialize(DynamicOps<T> debug1) {
/* 43 */     ImmutableMap.Builder<T, T> debug2 = ImmutableMap.builder();
/* 44 */     debug2
/* 45 */       .put(debug1.createString("source_x"), debug1.createInt(this.sourceX))
/* 46 */       .put(debug1.createString("source_ground_y"), debug1.createInt(this.sourceGroundY))
/* 47 */       .put(debug1.createString("source_z"), debug1.createInt(this.sourceZ))
/* 48 */       .put(debug1.createString("delta_y"), debug1.createInt(this.deltaY))
/* 49 */       .put(debug1.createString("dest_proj"), debug1.createString(this.destProjection.getName()));
/*    */     
/* 51 */     return new Dynamic(debug1, debug1.createMap((Map)debug2.build()));
/*    */   }
/*    */   
/*    */   public static <T> JigsawJunction deserialize(Dynamic<T> debug0) {
/* 55 */     return new JigsawJunction(debug0
/* 56 */         .get("source_x").asInt(0), debug0
/* 57 */         .get("source_ground_y").asInt(0), debug0
/* 58 */         .get("source_z").asInt(0), debug0
/* 59 */         .get("delta_y").asInt(0), 
/* 60 */         StructureTemplatePool.Projection.byName(debug0.get("dest_proj").asString("")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 66 */     if (this == debug1) return true; 
/* 67 */     if (debug1 == null || getClass() != debug1.getClass()) return false;
/*    */     
/* 69 */     JigsawJunction debug2 = (JigsawJunction)debug1;
/*    */     
/* 71 */     if (this.sourceX != debug2.sourceX) return false; 
/* 72 */     if (this.sourceZ != debug2.sourceZ) return false; 
/* 73 */     if (this.deltaY != debug2.deltaY) return false; 
/* 74 */     return (this.destProjection == debug2.destProjection);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     int debug1 = this.sourceX;
/* 80 */     debug1 = 31 * debug1 + this.sourceGroundY;
/* 81 */     debug1 = 31 * debug1 + this.sourceZ;
/* 82 */     debug1 = 31 * debug1 + this.deltaY;
/* 83 */     debug1 = 31 * debug1 + this.destProjection.hashCode();
/* 84 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return "JigsawJunction{sourceX=" + this.sourceX + ", sourceGroundY=" + this.sourceGroundY + ", sourceZ=" + this.sourceZ + ", deltaY=" + this.deltaY + ", destProjection=" + this.destProjection + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\JigsawJunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */