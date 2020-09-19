/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandFunction;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ 
/*     */ public class AdvancementRewards
/*     */ {
/*  28 */   public static final AdvancementRewards EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE);
/*     */   
/*     */   private final int experience;
/*     */   private final ResourceLocation[] loot;
/*     */   private final ResourceLocation[] recipes;
/*     */   private final CommandFunction.CacheableFunction function;
/*     */   
/*     */   public AdvancementRewards(int debug1, ResourceLocation[] debug2, ResourceLocation[] debug3, CommandFunction.CacheableFunction debug4) {
/*  36 */     this.experience = debug1;
/*  37 */     this.loot = debug2;
/*  38 */     this.recipes = debug3;
/*  39 */     this.function = debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void grant(ServerPlayer debug1) {
/*  47 */     debug1.giveExperiencePoints(this.experience);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     LootContext debug2 = (new LootContext.Builder(debug1.getLevel())).withParameter(LootContextParams.THIS_ENTITY, debug1).withParameter(LootContextParams.ORIGIN, debug1.position()).withRandom(debug1.getRandom()).create(LootContextParamSets.ADVANCEMENT_REWARD);
/*     */     
/*  54 */     boolean debug3 = false;
/*  55 */     for (ResourceLocation debug7 : this.loot) {
/*  56 */       for (ItemStack debug9 : debug1.server.getLootTables().get(debug7).getRandomItems(debug2)) {
/*  57 */         if (debug1.addItem(debug9)) {
/*  58 */           debug1.level.playSound(null, debug1.getX(), debug1.getY(), debug1.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((debug1.getRandom().nextFloat() - debug1.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*  59 */           debug3 = true; continue;
/*     */         } 
/*  61 */         ItemEntity debug10 = debug1.drop(debug9, false);
/*  62 */         if (debug10 != null) {
/*  63 */           debug10.setNoPickUpDelay();
/*  64 */           debug10.setOwner(debug1.getUUID());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     if (debug3) {
/*  70 */       debug1.inventoryMenu.broadcastChanges();
/*     */     }
/*  72 */     if (this.recipes.length > 0) {
/*  73 */       debug1.awardRecipesByKey(this.recipes);
/*     */     }
/*  75 */     MinecraftServer debug4 = debug1.server;
/*  76 */     this.function.get(debug4.getFunctions())
/*  77 */       .ifPresent(debug2 -> debug0.getFunctions().execute(debug2, debug1.createCommandSourceStack().withSuppressedOutput().withPermission(2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  82 */     return "AdvancementRewards{experience=" + this.experience + ", loot=" + 
/*     */       
/*  84 */       Arrays.toString((Object[])this.loot) + ", recipes=" + 
/*  85 */       Arrays.toString((Object[])this.recipes) + ", function=" + this.function + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonElement serializeToJson() {
/*  91 */     if (this == EMPTY) {
/*  92 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/*  95 */     JsonObject debug1 = new JsonObject();
/*     */     
/*  97 */     if (this.experience != 0) {
/*  98 */       debug1.addProperty("experience", Integer.valueOf(this.experience));
/*     */     }
/*     */     
/* 101 */     if (this.loot.length > 0) {
/* 102 */       JsonArray debug2 = new JsonArray();
/* 103 */       for (ResourceLocation debug6 : this.loot) {
/* 104 */         debug2.add(debug6.toString());
/*     */       }
/* 106 */       debug1.add("loot", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 109 */     if (this.recipes.length > 0) {
/* 110 */       JsonArray debug2 = new JsonArray();
/* 111 */       for (ResourceLocation debug6 : this.recipes) {
/* 112 */         debug2.add(debug6.toString());
/*     */       }
/* 114 */       debug1.add("recipes", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 117 */     if (this.function.getId() != null) {
/* 118 */       debug1.addProperty("function", this.function.getId().toString());
/*     */     }
/*     */     
/* 121 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static AdvancementRewards deserialize(JsonObject debug0) throws JsonParseException {
/*     */     CommandFunction.CacheableFunction debug6;
/* 126 */     int debug1 = GsonHelper.getAsInt(debug0, "experience", 0);
/* 127 */     JsonArray debug2 = GsonHelper.getAsJsonArray(debug0, "loot", new JsonArray());
/* 128 */     ResourceLocation[] debug3 = new ResourceLocation[debug2.size()];
/* 129 */     for (int i = 0; i < debug3.length; i++) {
/* 130 */       debug3[i] = new ResourceLocation(GsonHelper.convertToString(debug2.get(i), "loot[" + i + "]"));
/*     */     }
/* 132 */     JsonArray debug4 = GsonHelper.getAsJsonArray(debug0, "recipes", new JsonArray());
/* 133 */     ResourceLocation[] debug5 = new ResourceLocation[debug4.size()];
/* 134 */     for (int j = 0; j < debug5.length; j++) {
/* 135 */       debug5[j] = new ResourceLocation(GsonHelper.convertToString(debug4.get(j), "recipes[" + j + "]"));
/*     */     }
/*     */     
/* 138 */     if (debug0.has("function")) {
/* 139 */       debug6 = new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(debug0, "function")));
/*     */     } else {
/* 141 */       debug6 = CommandFunction.CacheableFunction.NONE;
/*     */     } 
/* 143 */     return new AdvancementRewards(debug1, debug3, debug5, debug6);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private int experience;
/* 148 */     private final List<ResourceLocation> loot = Lists.newArrayList();
/* 149 */     private final List<ResourceLocation> recipes = Lists.newArrayList();
/*     */     @Nullable
/*     */     private ResourceLocation function;
/*     */     
/*     */     public static Builder experience(int debug0) {
/* 154 */       return (new Builder()).addExperience(debug0);
/*     */     }
/*     */     
/*     */     public Builder addExperience(int debug1) {
/* 158 */       this.experience += debug1;
/* 159 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builder recipe(ResourceLocation debug0) {
/* 172 */       return (new Builder()).addRecipe(debug0);
/*     */     }
/*     */     
/*     */     public Builder addRecipe(ResourceLocation debug1) {
/* 176 */       this.recipes.add(debug1);
/* 177 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AdvancementRewards build() {
/* 190 */       return new AdvancementRewards(this.experience, this.loot.<ResourceLocation>toArray(new ResourceLocation[0]), this.recipes.<ResourceLocation>toArray(new ResourceLocation[0]), (this.function == null) ? CommandFunction.CacheableFunction.NONE : new CommandFunction.CacheableFunction(this.function));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\AdvancementRewards.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */