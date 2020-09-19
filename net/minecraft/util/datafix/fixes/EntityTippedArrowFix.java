/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityTippedArrowFix
/*    */   extends SimplestEntityRenameFix {
/*    */   public EntityTippedArrowFix(Schema debug1, boolean debug2) {
/*  9 */     super("EntityTippedArrowFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String debug1) {
/* 14 */     return Objects.equals(debug1, "TippedArrow") ? "Arrow" : debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityTippedArrowFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */