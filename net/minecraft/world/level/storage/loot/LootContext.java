/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LootContext
/*     */ {
/*     */   private final Random random;
/*     */   private final float luck;
/*     */   private final ServerLevel level;
/*     */   private final Function<ResourceLocation, LootTable> lootTables;
/*  39 */   private final Set<LootTable> visitedTables = Sets.newLinkedHashSet();
/*     */   
/*     */   private final Function<ResourceLocation, LootItemCondition> conditions;
/*  42 */   private final Set<LootItemCondition> visitedConditions = Sets.newLinkedHashSet();
/*     */   
/*     */   private final Map<LootContextParam<?>, Object> params;
/*     */   
/*     */   private final Map<ResourceLocation, DynamicDrop> dynamicDrops;
/*     */   
/*     */   private LootContext(Random debug1, float debug2, ServerLevel debug3, Function<ResourceLocation, LootTable> debug4, Function<ResourceLocation, LootItemCondition> debug5, Map<LootContextParam<?>, Object> debug6, Map<ResourceLocation, DynamicDrop> debug7) {
/*  49 */     this.random = debug1;
/*  50 */     this.luck = debug2;
/*  51 */     this.level = debug3;
/*  52 */     this.lootTables = debug4;
/*  53 */     this.conditions = debug5;
/*  54 */     this.params = (Map<LootContextParam<?>, Object>)ImmutableMap.copyOf(debug6);
/*  55 */     this.dynamicDrops = (Map<ResourceLocation, DynamicDrop>)ImmutableMap.copyOf(debug7);
/*     */   }
/*     */   
/*     */   public boolean hasParam(LootContextParam<?> debug1) {
/*  59 */     return this.params.containsKey(debug1);
/*     */   }
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
/*     */   public void addDynamicDrops(ResourceLocation debug1, Consumer<ItemStack> debug2) {
/*  72 */     DynamicDrop debug3 = this.dynamicDrops.get(debug1);
/*  73 */     if (debug3 != null) {
/*  74 */       debug3.add(this, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getParamOrNull(LootContextParam<T> debug1) {
/*  81 */     return (T)this.params.get(debug1);
/*     */   }
/*     */   
/*     */   public boolean addVisitedTable(LootTable debug1) {
/*  85 */     return this.visitedTables.add(debug1);
/*     */   }
/*     */   
/*     */   public void removeVisitedTable(LootTable debug1) {
/*  89 */     this.visitedTables.remove(debug1);
/*     */   }
/*     */   
/*     */   public boolean addVisitedCondition(LootItemCondition debug1) {
/*  93 */     return this.visitedConditions.add(debug1);
/*     */   }
/*     */   
/*     */   public void removeVisitedCondition(LootItemCondition debug1) {
/*  97 */     this.visitedConditions.remove(debug1);
/*     */   }
/*     */   
/*     */   public LootTable getLootTable(ResourceLocation debug1) {
/* 101 */     return this.lootTables.apply(debug1);
/*     */   }
/*     */   
/*     */   public LootItemCondition getCondition(ResourceLocation debug1) {
/* 105 */     return this.conditions.apply(debug1);
/*     */   }
/*     */   
/*     */   public Random getRandom() {
/* 109 */     return this.random;
/*     */   }
/*     */   
/*     */   public float getLuck() {
/* 113 */     return this.luck;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/* 117 */     return this.level;
/*     */   }
/*     */   @FunctionalInterface
/*     */   public static interface DynamicDrop {
/*     */     void add(LootContext param1LootContext, Consumer<ItemStack> param1Consumer); }
/* 122 */   public static class Builder { private final Map<LootContextParam<?>, Object> params = Maps.newIdentityHashMap(); private final ServerLevel level;
/* 123 */     private final Map<ResourceLocation, LootContext.DynamicDrop> dynamicDrops = Maps.newHashMap();
/*     */     
/*     */     private Random random;
/*     */     private float luck;
/*     */     
/*     */     public Builder(ServerLevel debug1) {
/* 129 */       this.level = debug1;
/*     */     }
/*     */     
/*     */     public Builder withRandom(Random debug1) {
/* 133 */       this.random = debug1;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptionalRandomSeed(long debug1) {
/* 138 */       if (debug1 != 0L) {
/* 139 */         this.random = new Random(debug1);
/*     */       }
/* 141 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptionalRandomSeed(long debug1, Random debug3) {
/* 145 */       if (debug1 == 0L) {
/* 146 */         this.random = debug3;
/*     */       } else {
/* 148 */         this.random = new Random(debug1);
/*     */       } 
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withLuck(float debug1) {
/* 154 */       this.luck = debug1;
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder withParameter(LootContextParam<T> debug1, T debug2) {
/* 159 */       this.params.put(debug1, debug2);
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder withOptionalParameter(LootContextParam<T> debug1, @Nullable T debug2) {
/* 164 */       if (debug2 == null) {
/* 165 */         this.params.remove(debug1);
/*     */       } else {
/* 167 */         this.params.put(debug1, debug2);
/*     */       } 
/* 169 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withDynamicDrop(ResourceLocation debug1, LootContext.DynamicDrop debug2) {
/* 173 */       LootContext.DynamicDrop debug3 = this.dynamicDrops.put(debug1, debug2);
/*     */       
/* 175 */       if (debug3 != null) {
/* 176 */         throw new IllegalStateException("Duplicated dynamic drop '" + this.dynamicDrops + "'");
/*     */       }
/*     */       
/* 179 */       return this;
/*     */     }
/*     */     
/*     */     public ServerLevel getLevel() {
/* 183 */       return this.level;
/*     */     }
/*     */     
/*     */     public <T> T getParameter(LootContextParam<T> debug1) {
/* 187 */       T debug2 = (T)this.params.get(debug1);
/* 188 */       if (debug2 == null) {
/* 189 */         throw new IllegalArgumentException("No parameter " + debug1);
/*     */       }
/* 191 */       return debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public <T> T getOptionalParameter(LootContextParam<T> debug1) {
/* 197 */       return (T)this.params.get(debug1);
/*     */     }
/*     */     
/*     */     public LootContext create(LootContextParamSet debug1) {
/* 201 */       Sets.SetView setView1 = Sets.difference(this.params.keySet(), debug1.getAllowed());
/* 202 */       if (!setView1.isEmpty()) {
/* 203 */         throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + setView1);
/*     */       }
/*     */       
/* 206 */       Sets.SetView setView2 = Sets.difference(debug1.getRequired(), this.params.keySet());
/* 207 */       if (!setView2.isEmpty()) {
/* 208 */         throw new IllegalArgumentException("Missing required parameters: " + setView2);
/*     */       }
/*     */       
/* 211 */       Random debug4 = this.random;
/* 212 */       if (debug4 == null) {
/* 213 */         debug4 = new Random();
/*     */       }
/*     */       
/* 216 */       MinecraftServer debug5 = this.level.getServer();
/* 217 */       return new LootContext(debug4, this.luck, this.level, debug5.getLootTables()::get, debug5.getPredicateManager()::get, this.params, this.dynamicDrops);
/*     */     } }
/*     */ 
/*     */   
/*     */   public enum EntityTarget {
/* 222 */     THIS("this", LootContextParams.THIS_ENTITY),
/* 223 */     KILLER("killer", LootContextParams.KILLER_ENTITY),
/* 224 */     DIRECT_KILLER("direct_killer", LootContextParams.DIRECT_KILLER_ENTITY),
/* 225 */     KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final LootContextParam<? extends Entity> param;
/*     */ 
/*     */     
/*     */     EntityTarget(String debug3, LootContextParam<? extends Entity> debug4) {
/* 233 */       this.name = debug3;
/* 234 */       this.param = debug4;
/*     */     }
/*     */     
/*     */     public LootContextParam<? extends Entity> getParam() {
/* 238 */       return this.param;
/*     */     }
/*     */     
/*     */     public static EntityTarget getByName(String debug0) {
/* 242 */       for (EntityTarget debug4 : values()) {
/* 243 */         if (debug4.name.equals(debug0)) {
/* 244 */           return debug4;
/*     */         }
/*     */       } 
/* 247 */       throw new IllegalArgumentException("Invalid entity target " + debug0);
/*     */     }
/*     */     
/*     */     public static class Serializer
/*     */       extends TypeAdapter<EntityTarget> {
/*     */       public void write(JsonWriter debug1, LootContext.EntityTarget debug2) throws IOException {
/* 253 */         debug1.value(debug2.name);
/*     */       }
/*     */ 
/*     */       
/*     */       public LootContext.EntityTarget read(JsonReader debug1) throws IOException {
/* 258 */         return LootContext.EntityTarget.getByName(debug1.nextString());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */