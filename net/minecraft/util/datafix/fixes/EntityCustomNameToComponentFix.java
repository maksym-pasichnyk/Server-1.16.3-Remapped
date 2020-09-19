/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityCustomNameToComponentFix
/*    */   extends DataFix {
/*    */   public EntityCustomNameToComponentFix(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     OpticFinder<String> debug1 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/* 25 */     return fixTypeEverywhereTyped("EntityCustomNameToComponentFix", getInputSchema().getType(References.ENTITY), debug1 -> debug1.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Dynamic<?> fixTagCustomName(Dynamic<?> debug0) {
/* 35 */     String debug1 = debug0.get("CustomName").asString("");
/* 36 */     if (debug1.isEmpty()) {
/* 37 */       return debug0.remove("CustomName");
/*    */     }
/* 39 */     return debug0.set("CustomName", debug0.createString(Component.Serializer.toJson((Component)new TextComponent(debug1))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityCustomNameToComponentFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */