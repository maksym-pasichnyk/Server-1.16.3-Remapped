/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class HeightmapRenamingFix extends DataFix {
/*    */   public HeightmapRenamingFix(Schema debug1, boolean debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 21 */     OpticFinder<?> debug2 = debug1.findField("Level");
/* 22 */     return fixTypeEverywhereTyped("HeightmapRenamingFix", debug1, debug2 -> debug2.updateTyped(debug1, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> debug1) {
/* 28 */     Optional<? extends Dynamic<?>> debug2 = debug1.get("Heightmaps").result();
/* 29 */     if (!debug2.isPresent()) {
/* 30 */       return debug1;
/*    */     }
/*    */     
/* 33 */     Dynamic<?> debug3 = debug2.get();
/*    */     
/* 35 */     Optional<? extends Dynamic<?>> debug4 = debug3.get("LIQUID").result();
/* 36 */     if (debug4.isPresent()) {
/* 37 */       debug3 = debug3.remove("LIQUID");
/* 38 */       debug3 = debug3.set("WORLD_SURFACE_WG", debug4.get());
/*    */     } 
/*    */     
/* 41 */     Optional<? extends Dynamic<?>> debug5 = debug3.get("SOLID").result();
/* 42 */     if (debug5.isPresent()) {
/* 43 */       debug3 = debug3.remove("SOLID");
/* 44 */       debug3 = debug3.set("OCEAN_FLOOR_WG", debug5.get());
/* 45 */       debug3 = debug3.set("OCEAN_FLOOR", debug5.get());
/*    */     } 
/*    */     
/* 48 */     Optional<? extends Dynamic<?>> debug6 = debug3.get("LIGHT").result();
/* 49 */     if (debug6.isPresent()) {
/* 50 */       debug3 = debug3.remove("LIGHT");
/* 51 */       debug3 = debug3.set("LIGHT_BLOCKING", debug6.get());
/*    */     } 
/*    */     
/* 54 */     Optional<? extends Dynamic<?>> debug7 = debug3.get("RAIN").result();
/* 55 */     if (debug7.isPresent()) {
/* 56 */       debug3 = debug3.remove("RAIN");
/* 57 */       debug3 = debug3.set("MOTION_BLOCKING", debug7.get());
/* 58 */       debug3 = debug3.set("MOTION_BLOCKING_NO_LEAVES", debug7.get());
/*    */     } 
/*    */     
/* 61 */     return debug1.set("Heightmaps", debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\HeightmapRenamingFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */