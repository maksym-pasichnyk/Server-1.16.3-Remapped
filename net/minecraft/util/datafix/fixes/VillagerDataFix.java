/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class VillagerDataFix extends NamedEntityFix {
/*    */   public VillagerDataFix(Schema debug1, String debug2) {
/* 12 */     super(debug1, false, "Villager profession data fix (" + debug2 + ")", References.ENTITY, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 17 */     Dynamic<?> debug2 = (Dynamic)debug1.get(DSL.remainderFinder());
/*    */     
/* 19 */     return debug1.set(DSL.remainderFinder(), debug2
/* 20 */         .remove("Profession")
/* 21 */         .remove("Career")
/* 22 */         .remove("CareerLevel")
/* 23 */         .set("VillagerData", debug2
/* 24 */           .createMap((Map)ImmutableMap.of(debug2
/* 25 */               .createString("type"), debug2.createString("minecraft:plains"), debug2
/* 26 */               .createString("profession"), debug2.createString(upgradeData(debug2.get("Profession").asInt(0), debug2.get("Career").asInt(0))), debug2
/* 27 */               .createString("level"), DataFixUtils.orElse(debug2.get("CareerLevel").result(), debug2.createInt(1))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String upgradeData(int debug0, int debug1) {
/* 34 */     if (debug0 == 0) {
/* 35 */       if (debug1 == 2)
/* 36 */         return "minecraft:fisherman"; 
/* 37 */       if (debug1 == 3)
/* 38 */         return "minecraft:shepherd"; 
/* 39 */       if (debug1 == 4) {
/* 40 */         return "minecraft:fletcher";
/*    */       }
/* 42 */       return "minecraft:farmer";
/*    */     } 
/* 44 */     if (debug0 == 1) {
/* 45 */       if (debug1 == 2) {
/* 46 */         return "minecraft:cartographer";
/*    */       }
/* 48 */       return "minecraft:librarian";
/*    */     } 
/* 50 */     if (debug0 == 2)
/* 51 */       return "minecraft:cleric"; 
/* 52 */     if (debug0 == 3) {
/* 53 */       if (debug1 == 2)
/* 54 */         return "minecraft:weaponsmith"; 
/* 55 */       if (debug1 == 3) {
/* 56 */         return "minecraft:toolsmith";
/*    */       }
/* 58 */       return "minecraft:armorer";
/*    */     } 
/* 60 */     if (debug0 == 4) {
/* 61 */       if (debug1 == 2) {
/* 62 */         return "minecraft:leatherworker";
/*    */       }
/* 64 */       return "minecraft:butcher";
/*    */     } 
/* 66 */     if (debug0 == 5) {
/* 67 */       return "minecraft:nitwit";
/*    */     }
/* 69 */     return "minecraft:none";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerDataFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */