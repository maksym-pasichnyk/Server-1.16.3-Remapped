/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Set;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FillPlayerHead
/*    */   extends LootItemConditionalFunction {
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public FillPlayerHead(LootItemCondition[] debug1, LootContext.EntityTarget debug2) {
/* 26 */     super(debug1);
/* 27 */     this.entityTarget = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 32 */     return LootItemFunctions.FILL_PLAYER_HEAD;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 37 */     return (Set<LootContextParam<?>>)ImmutableSet.of(this.entityTarget.getParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 42 */     if (debug1.getItem() == Items.PLAYER_HEAD) {
/* 43 */       Entity debug3 = (Entity)debug2.getParamOrNull(this.entityTarget.getParam());
/* 44 */       if (debug3 instanceof Player) {
/* 45 */         GameProfile debug4 = ((Player)debug3).getGameProfile();
/* 46 */         debug1.getOrCreateTag().put("SkullOwner", (Tag)NbtUtils.writeGameProfile(new CompoundTag(), debug4));
/*    */       } 
/*    */     } 
/* 49 */     return debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<FillPlayerHead>
/*    */   {
/*    */     public void serialize(JsonObject debug1, FillPlayerHead debug2, JsonSerializationContext debug3) {
/* 59 */       super.serialize(debug1, debug2, debug3);
/* 60 */       debug1.add("entity", debug3.serialize(debug2.entityTarget));
/*    */     }
/*    */ 
/*    */     
/*    */     public FillPlayerHead deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 65 */       LootContext.EntityTarget debug4 = (LootContext.EntityTarget)GsonHelper.getAsObject(debug1, "entity", debug2, LootContext.EntityTarget.class);
/* 66 */       return new FillPlayerHead(debug3, debug4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FillPlayerHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */