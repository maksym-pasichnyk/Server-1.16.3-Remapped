/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class EntityMinecartIdentifiersFix extends DataFix {
/*    */   public EntityMinecartIdentifiersFix(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */   
/* 21 */   private static final List<String> MINECART_BY_ID = Lists.newArrayList((Object[])new String[] { "MinecartRideable", "MinecartChest", "MinecartFurnace" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 30 */     TaggedChoice.TaggedChoiceType<String> debug1 = getInputSchema().findChoiceType(References.ENTITY);
/* 31 */     TaggedChoice.TaggedChoiceType<String> debug2 = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 33 */     return fixTypeEverywhere("EntityMinecartIdentifiersFix", (Type)debug1, (Type)debug2, debug2 -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityMinecartIdentifiersFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */