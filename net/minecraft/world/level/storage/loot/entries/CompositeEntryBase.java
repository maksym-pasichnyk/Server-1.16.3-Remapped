/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class CompositeEntryBase
/*    */   extends LootPoolEntryContainer {
/*    */   protected final LootPoolEntryContainer[] children;
/*    */   private final ComposableEntryContainer composedChildren;
/*    */   
/*    */   protected CompositeEntryBase(LootPoolEntryContainer[] debug1, LootItemCondition[] debug2) {
/* 18 */     super(debug2);
/* 19 */     this.children = debug1;
/* 20 */     this.composedChildren = compose((ComposableEntryContainer[])debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 25 */     super.validate(debug1);
/*    */     
/* 27 */     if (this.children.length == 0) {
/* 28 */       debug1.reportProblem("Empty children list");
/*    */     }
/*    */     
/* 31 */     for (int debug2 = 0; debug2 < this.children.length; debug2++) {
/* 32 */       this.children[debug2].validate(debug1.forChild(".entry[" + debug2 + "]"));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract ComposableEntryContainer compose(ComposableEntryContainer[] paramArrayOfComposableEntryContainer);
/*    */   
/*    */   public final boolean expand(LootContext debug1, Consumer<LootPoolEntry> debug2) {
/* 40 */     if (!canRun(debug1)) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     return this.composedChildren.expand(debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends CompositeEntryBase> LootPoolEntryContainer.Serializer<T> createSerializer(final CompositeEntryConstructor<T> constructor) {
/* 53 */     return new LootPoolEntryContainer.Serializer<T>()
/*    */       {
/*    */         public void serializeCustom(JsonObject debug1, T debug2, JsonSerializationContext debug3) {
/* 56 */           debug1.add("children", debug3.serialize(((CompositeEntryBase)debug2).children));
/*    */         }
/*    */ 
/*    */         
/*    */         public final T deserializeCustom(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 61 */           LootPoolEntryContainer[] debug4 = (LootPoolEntryContainer[])GsonHelper.getAsObject(debug1, "children", debug2, LootPoolEntryContainer[].class);
/* 62 */           return (T)constructor.create(debug4, debug3);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface CompositeEntryConstructor<T extends CompositeEntryBase> {
/*    */     T create(LootPoolEntryContainer[] param1ArrayOfLootPoolEntryContainer, LootItemCondition[] param1ArrayOfLootItemCondition);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\CompositeEntryBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */