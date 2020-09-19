/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ public class TeamDisplayNameFix
/*    */   extends DataFix {
/*    */   public TeamDisplayNameFix(Schema debug1, boolean debug2) {
/* 20 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 25 */     Type<Pair<String, Dynamic<?>>> debug1 = DSL.named(References.TEAM.typeName(), DSL.remainderType());
/*    */     
/* 27 */     if (!Objects.equals(debug1, getInputSchema().getType(References.TEAM))) {
/* 28 */       throw new IllegalStateException("Team type is not what was expected.");
/*    */     }
/*    */     
/* 31 */     return fixTypeEverywhere("TeamDisplayNameFix", debug1, debug0 -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\TeamDisplayNameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */