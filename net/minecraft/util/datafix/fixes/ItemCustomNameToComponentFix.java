/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class ItemCustomNameToComponentFix extends DataFix {
/*    */   public ItemCustomNameToComponentFix(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 22 */     Optional<? extends Dynamic<?>> debug2 = debug1.get("display").result();
/* 23 */     if (debug2.isPresent()) {
/* 24 */       Dynamic<?> debug3 = debug2.get();
/* 25 */       Optional<String> debug4 = debug3.get("Name").asString().result();
/* 26 */       if (debug4.isPresent()) {
/* 27 */         debug3 = debug3.set("Name", debug3.createString(Component.Serializer.toJson((Component)new TextComponent(debug4.get()))));
/*    */       } else {
/* 29 */         Optional<String> debug5 = debug3.get("LocName").asString().result();
/* 30 */         if (debug5.isPresent()) {
/* 31 */           debug3 = debug3.set("Name", debug3.createString(Component.Serializer.toJson((Component)new TranslatableComponent(debug5.get()))));
/* 32 */           debug3 = debug3.remove("LocName");
/*    */         } 
/*    */       } 
/* 35 */       return debug1.set("display", debug3);
/*    */     } 
/* 37 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 42 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 43 */     OpticFinder<?> debug2 = debug1.findField("tag");
/*    */     
/* 45 */     return fixTypeEverywhereTyped("ItemCustomNameToComponentFix", debug1, debug2 -> debug2.updateTyped(debug1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemCustomNameToComponentFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */