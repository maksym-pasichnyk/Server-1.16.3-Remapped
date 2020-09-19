/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ZombieVillagerRebuildXpFix extends NamedEntityFix {
/*    */   public ZombieVillagerRebuildXpFix(Schema debug1, boolean debug2) {
/* 11 */     super(debug1, debug2, "Zombie Villager XP rebuild", References.ENTITY, "minecraft:zombie_villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 16 */     return debug1.update(DSL.remainderFinder(), debug0 -> {
/*    */           Optional<Number> debug1 = debug0.get("Xp").asNumber().result();
/*    */           if (!debug1.isPresent()) {
/*    */             int debug2 = debug0.get("VillagerData").get("level").asInt(1);
/*    */             return debug0.set("Xp", debug0.createInt(VillagerRebuildLevelAndXpFix.getMinXpPerLevel(debug2)));
/*    */           } 
/*    */           return debug0;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ZombieVillagerRebuildXpFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */