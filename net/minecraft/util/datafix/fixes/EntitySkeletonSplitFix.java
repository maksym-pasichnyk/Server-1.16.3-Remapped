/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntitySkeletonSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntitySkeletonSplitFix(Schema debug1, boolean debug2) {
/* 11 */     super("EntitySkeletonSplitFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String debug1, Dynamic<?> debug2) {
/* 16 */     if (Objects.equals(debug1, "Skeleton")) {
/* 17 */       int debug3 = debug2.get("SkeletonType").asInt(0);
/* 18 */       if (debug3 == 1) {
/* 19 */         debug1 = "WitherSkeleton";
/* 20 */       } else if (debug3 == 2) {
/* 21 */         debug1 = "Stray";
/*    */       } 
/*    */     } 
/* 24 */     return Pair.of(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntitySkeletonSplitFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */