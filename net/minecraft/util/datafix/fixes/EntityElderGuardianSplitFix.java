/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityElderGuardianSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntityElderGuardianSplitFix(Schema debug1, boolean debug2) {
/* 11 */     super("EntityElderGuardianSplitFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String debug1, Dynamic<?> debug2) {
/* 16 */     return Pair.of((Objects.equals(debug1, "Guardian") && debug2.get("Elder").asBoolean(false)) ? "ElderGuardian" : debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityElderGuardianSplitFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */