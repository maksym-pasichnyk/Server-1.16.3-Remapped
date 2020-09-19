/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityCatSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntityCatSplitFix(Schema debug1, boolean debug2) {
/* 11 */     super("EntityCatSplitFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String debug1, Dynamic<?> debug2) {
/* 16 */     if (Objects.equals("minecraft:ocelot", debug1)) {
/* 17 */       int debug3 = debug2.get("CatType").asInt(0);
/* 18 */       if (debug3 == 0) {
/* 19 */         String debug4 = debug2.get("Owner").asString("");
/* 20 */         String debug5 = debug2.get("OwnerUUID").asString("");
/* 21 */         if (debug4.length() > 0 || debug5.length() > 0) {
/* 22 */           debug2.set("Trusting", debug2.createBoolean(true));
/*    */         }
/* 24 */       } else if (debug3 > 0 && debug3 < 4) {
/* 25 */         debug2 = debug2.set("CatType", debug2.createInt(debug3));
/* 26 */         debug2 = debug2.set("OwnerUUID", debug2.createString(debug2.get("OwnerUUID").asString("")));
/* 27 */         return Pair.of("minecraft:cat", debug2);
/*    */       } 
/*    */     } 
/*    */     
/* 31 */     return Pair.of(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityCatSplitFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */