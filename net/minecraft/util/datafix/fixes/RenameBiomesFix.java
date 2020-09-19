/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RenameBiomesFix extends DataFix {
/*    */   private final String name;
/*    */   private final Map<String, String> biomes;
/*    */   
/*    */   public RenameBiomesFix(Schema debug1, boolean debug2, String debug3, Map<String, String> debug4) {
/* 20 */     super(debug1, debug2);
/* 21 */     this.biomes = debug4;
/* 22 */     this.name = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<Pair<String, String>> debug1 = DSL.named(References.BIOME.typeName(), NamespacedSchema.namespacedString());
/* 28 */     if (!Objects.equals(debug1, getInputSchema().getType(References.BIOME))) {
/* 29 */       throw new IllegalStateException("Biome type is not what was expected.");
/*    */     }
/* 31 */     return fixTypeEverywhere(this.name, debug1, debug1 -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\RenameBiomesFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */