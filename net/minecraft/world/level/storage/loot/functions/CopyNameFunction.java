/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.Nameable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class CopyNameFunction
/*    */   extends LootItemConditionalFunction {
/*    */   private final NameSource source;
/*    */   
/*    */   private CopyNameFunction(LootItemCondition[] debug1, NameSource debug2) {
/* 21 */     super(debug1);
/* 22 */     this.source = debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 28 */     return LootItemFunctions.COPY_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 33 */     return (Set<LootContextParam<?>>)ImmutableSet.of(this.source.param);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 38 */     Object debug3 = debug2.getParamOrNull(this.source.param);
/* 39 */     if (debug3 instanceof Nameable) {
/* 40 */       Nameable debug4 = (Nameable)debug3;
/* 41 */       if (debug4.hasCustomName()) {
/* 42 */         debug1.setHoverName(debug4.getDisplayName());
/*    */       }
/*    */     } 
/* 45 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> copyName(NameSource debug0) {
/* 49 */     return simpleBuilder(debug1 -> new CopyNameFunction(debug1, debug0));
/*    */   }
/*    */   
/*    */   public enum NameSource {
/* 53 */     THIS("this", LootContextParams.THIS_ENTITY),
/* 54 */     KILLER("killer", LootContextParams.KILLER_ENTITY),
/* 55 */     KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER),
/* 56 */     BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);
/*    */     
/*    */     public final String name;
/*    */     public final LootContextParam<?> param;
/*    */     
/*    */     NameSource(String debug3, LootContextParam<?> debug4) {
/* 62 */       this.name = debug3;
/* 63 */       this.param = debug4;
/*    */     }
/*    */     
/*    */     public static NameSource getByName(String debug0) {
/* 67 */       for (NameSource debug4 : values()) {
/* 68 */         if (debug4.name.equals(debug0)) {
/* 69 */           return debug4;
/*    */         }
/*    */       } 
/* 72 */       throw new IllegalArgumentException("Invalid name source " + debug0);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<CopyNameFunction> {
/*    */     public void serialize(JsonObject debug1, CopyNameFunction debug2, JsonSerializationContext debug3) {
/* 79 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 81 */       debug1.addProperty("source", debug2.source.name);
/*    */     }
/*    */ 
/*    */     
/*    */     public CopyNameFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 86 */       CopyNameFunction.NameSource debug4 = CopyNameFunction.NameSource.getByName(GsonHelper.getAsString(debug1, "source"));
/* 87 */       return new CopyNameFunction(debug3, debug4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyNameFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */