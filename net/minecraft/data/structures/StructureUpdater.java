/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.util.datafix.DataFixers;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class StructureUpdater
/*    */   implements SnbtToNbt.Filter
/*    */ {
/* 14 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public CompoundTag apply(String debug1, CompoundTag debug2) {
/* 18 */     if (debug1.startsWith("data/minecraft/structures/")) {
/* 19 */       return updateStructure(debug1, patchVersion(debug2));
/*    */     }
/* 21 */     return debug2;
/*    */   }
/*    */   
/*    */   private static CompoundTag patchVersion(CompoundTag debug0) {
/* 25 */     if (!debug0.contains("DataVersion", 99)) {
/* 26 */       debug0.putInt("DataVersion", 500);
/*    */     }
/* 28 */     return debug0;
/*    */   }
/*    */   
/*    */   private static CompoundTag updateStructure(String debug0, CompoundTag debug1) {
/* 32 */     StructureTemplate debug2 = new StructureTemplate();
/* 33 */     int debug3 = debug1.getInt("DataVersion");
/* 34 */     int debug4 = 2532;
/* 35 */     if (debug3 < 2532) {
/* 36 */       LOGGER.warn("SNBT Too old, do not forget to update: " + debug3 + " < " + '৤' + ": " + debug0);
/*    */     }
/* 38 */     CompoundTag debug5 = NbtUtils.update(DataFixers.getDataFixer(), DataFixTypes.STRUCTURE, debug1, debug3);
/* 39 */     debug2.load(debug5);
/* 40 */     return debug2.save(new CompoundTag());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\structures\StructureUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */