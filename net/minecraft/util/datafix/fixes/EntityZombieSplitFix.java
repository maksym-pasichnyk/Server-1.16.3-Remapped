/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityZombieSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntityZombieSplitFix(Schema debug1, boolean debug2) {
/* 11 */     super("EntityZombieSplitFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String debug1, Dynamic<?> debug2) {
/* 16 */     if (Objects.equals("Zombie", debug1))
/* 17 */     { String debug3 = "Zombie";
/* 18 */       int debug4 = debug2.get("ZombieType").asInt(0);
/* 19 */       switch (debug4)
/*    */       
/*    */       { 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         default:
/* 35 */           debug2 = debug2.remove("ZombieType");
/* 36 */           return Pair.of(debug3, debug2);
/*    */         case 1: case 2: case 3: case 4: case 5: debug3 = "ZombieVillager"; debug2 = debug2.set("Profession", debug2.createInt(debug4 - 1));
/* 38 */         case 6: break; }  debug3 = "Husk"; }  return Pair.of(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityZombieSplitFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */