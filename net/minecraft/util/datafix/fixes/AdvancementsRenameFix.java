/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AdvancementsRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AdvancementsRenameFix(Schema debug1, boolean debug2, String debug3, Function<String, String> debug4) {
/* 15 */     super(debug1, debug2);
/* 16 */     this.name = debug3;
/* 17 */     this.renamer = debug4;
/*    */   }
/*    */   private final Function<String, String> renamer;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), debug1 -> debug1.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\AdvancementsRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */