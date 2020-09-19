/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ public class EntityZombieVillagerTypeFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   public EntityZombieVillagerTypeFix(Schema debug1, boolean debug2) {
/* 14 */     super(debug1, debug2, "EntityZombieVillagerTypeFix", References.ENTITY, "Zombie");
/*    */   }
/*    */   
/* 17 */   private static final Random RANDOM = new Random();
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 20 */     if (debug1.get("IsVillager").asBoolean(false)) {
/* 21 */       if (!debug1.get("ZombieType").result().isPresent()) {
/* 22 */         int debug2 = getVillagerProfession(debug1.get("VillagerProfession").asInt(-1));
/* 23 */         if (debug2 == -1) {
/* 24 */           debug2 = getVillagerProfession(RANDOM.nextInt(6));
/*    */         }
/*    */         
/* 27 */         debug1 = debug1.set("ZombieType", debug1.createInt(debug2));
/*    */       } 
/*    */       
/* 30 */       debug1 = debug1.remove("IsVillager");
/*    */     } 
/* 32 */     return debug1;
/*    */   }
/*    */   
/*    */   private int getVillagerProfession(int debug1) {
/* 36 */     if (debug1 < 0 || debug1 >= 6) {
/* 37 */       return -1;
/*    */     }
/* 39 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 44 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityZombieVillagerTypeFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */