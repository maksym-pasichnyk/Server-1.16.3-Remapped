/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.worldgen.biome.Biomes;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.newbiome.area.AreaFactory;
/*    */ import net.minecraft.world.level.newbiome.area.LazyArea;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class Layer {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   private final LazyArea area;
/*    */   
/*    */   public Layer(AreaFactory<LazyArea> debug1) {
/* 19 */     this.area = (LazyArea)debug1.make();
/*    */   }
/*    */   
/*    */   public Biome get(Registry<Biome> debug1, int debug2, int debug3) {
/* 23 */     int debug4 = this.area.get(debug2, debug3);
/* 24 */     ResourceKey<Biome> debug5 = Biomes.byId(debug4);
/* 25 */     if (debug5 == null) {
/* 26 */       throw new IllegalStateException("Unknown biome id emitted by layers: " + debug4);
/*    */     }
/*    */     
/* 29 */     Biome debug6 = (Biome)debug1.get(debug5);
/*    */     
/* 31 */     if (debug6 == null) {
/* 32 */       if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 33 */         throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Unknown biome id: " + debug4));
/*    */       }
/* 35 */       LOGGER.warn("Unknown biome id: ", Integer.valueOf(debug4));
/* 36 */       return (Biome)debug1.get(Biomes.byId(0));
/*    */     } 
/*    */     
/* 39 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\Layer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */