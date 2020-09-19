/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class StatePropertiesPredicate
/*     */ {
/*  24 */   public static final StatePropertiesPredicate ANY = new StatePropertiesPredicate((List<PropertyMatcher>)ImmutableList.of());
/*     */   private final List<PropertyMatcher> properties;
/*     */   
/*     */   static abstract class PropertyMatcher
/*     */   {
/*     */     public PropertyMatcher(String debug1) {
/*  30 */       this.name = debug1;
/*     */     }
/*     */     private final String name;
/*     */     public <S extends StateHolder<?, S>> boolean match(StateDefinition<?, S> debug1, S debug2) {
/*  34 */       Property<?> debug3 = debug1.getProperty(this.name);
/*  35 */       if (debug3 == null) {
/*  36 */         return false;
/*     */       }
/*     */       
/*  39 */       return match((StateHolder<?, ?>)debug2, debug3);
/*     */     }
/*     */     
/*     */     protected abstract <T extends Comparable<T>> boolean match(StateHolder<?, ?> param1StateHolder, Property<T> param1Property);
/*     */     
/*     */     public abstract JsonElement toJson();
/*     */     
/*     */     public String getName() {
/*  47 */       return this.name;
/*     */     }
/*     */     
/*     */     public void checkState(StateDefinition<?, ?> debug1, Consumer<String> debug2) {
/*  51 */       Property<?> debug3 = debug1.getProperty(this.name);
/*  52 */       if (debug3 == null)
/*  53 */         debug2.accept(this.name); 
/*     */     }
/*     */   }
/*     */   
/*     */   static class ExactPropertyMatcher
/*     */     extends PropertyMatcher {
/*     */     private final String value;
/*     */     
/*     */     public ExactPropertyMatcher(String debug1, String debug2) {
/*  62 */       super(debug1);
/*  63 */       this.value = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     protected <T extends Comparable<T>> boolean match(StateHolder<?, ?> debug1, Property<T> debug2) {
/*  68 */       Comparable comparable = debug1.getValue(debug2);
/*  69 */       Optional<T> debug4 = debug2.getValue(this.value);
/*  70 */       return (debug4.isPresent() && comparable.compareTo(debug4.get()) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement toJson() {
/*  75 */       return (JsonElement)new JsonPrimitive(this.value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class RangedPropertyMatcher
/*     */     extends PropertyMatcher {
/*     */     @Nullable
/*     */     private final String minValue;
/*     */     @Nullable
/*     */     private final String maxValue;
/*     */     
/*     */     public RangedPropertyMatcher(String debug1, @Nullable String debug2, @Nullable String debug3) {
/*  87 */       super(debug1);
/*  88 */       this.minValue = debug2;
/*  89 */       this.maxValue = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     protected <T extends Comparable<T>> boolean match(StateHolder<?, ?> debug1, Property<T> debug2) {
/*  94 */       Comparable comparable = debug1.getValue(debug2);
/*     */       
/*  96 */       if (this.minValue != null) {
/*  97 */         Optional<T> debug4 = debug2.getValue(this.minValue);
/*  98 */         if (!debug4.isPresent() || comparable.compareTo(debug4.get()) < 0) {
/*  99 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 103 */       if (this.maxValue != null) {
/* 104 */         Optional<T> debug4 = debug2.getValue(this.maxValue);
/* 105 */         if (!debug4.isPresent() || comparable.compareTo(debug4.get()) > 0) {
/* 106 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 110 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement toJson() {
/* 115 */       JsonObject debug1 = new JsonObject();
/* 116 */       if (this.minValue != null) {
/* 117 */         debug1.addProperty("min", this.minValue);
/*     */       }
/* 119 */       if (this.maxValue != null) {
/* 120 */         debug1.addProperty("max", this.maxValue);
/*     */       }
/* 122 */       return (JsonElement)debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   private static PropertyMatcher fromJson(String debug0, JsonElement debug1) {
/* 127 */     if (debug1.isJsonPrimitive()) {
/* 128 */       String str = debug1.getAsString();
/* 129 */       return new ExactPropertyMatcher(debug0, str);
/*     */     } 
/*     */     
/* 132 */     JsonObject debug2 = GsonHelper.convertToJsonObject(debug1, "value");
/* 133 */     String debug3 = debug2.has("min") ? getStringOrNull(debug2.get("min")) : null;
/* 134 */     String debug4 = debug2.has("max") ? getStringOrNull(debug2.get("max")) : null;
/* 135 */     return (debug3 != null && debug3.equals(debug4)) ? new ExactPropertyMatcher(debug0, debug3) : new RangedPropertyMatcher(debug0, debug3, debug4);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static String getStringOrNull(JsonElement debug0) {
/* 140 */     if (debug0.isJsonNull()) {
/* 141 */       return null;
/*     */     }
/* 143 */     return debug0.getAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private StatePropertiesPredicate(List<PropertyMatcher> debug1) {
/* 149 */     this.properties = (List<PropertyMatcher>)ImmutableList.copyOf(debug1);
/*     */   }
/*     */   
/*     */   public <S extends StateHolder<?, S>> boolean matches(StateDefinition<?, S> debug1, S debug2) {
/* 153 */     for (PropertyMatcher debug4 : this.properties) {
/* 154 */       if (!debug4.match(debug1, debug2)) {
/* 155 */         return false;
/*     */       }
/*     */     } 
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   public boolean matches(BlockState debug1) {
/* 162 */     return matches(debug1.getBlock().getStateDefinition(), debug1);
/*     */   }
/*     */   
/*     */   public boolean matches(FluidState debug1) {
/* 166 */     return matches(debug1.getType().getStateDefinition(), debug1);
/*     */   }
/*     */   
/*     */   public void checkState(StateDefinition<?, ?> debug1, Consumer<String> debug2) {
/* 170 */     this.properties.forEach(debug2 -> debug2.checkState(debug0, debug1));
/*     */   }
/*     */   
/*     */   public static StatePropertiesPredicate fromJson(@Nullable JsonElement debug0) {
/* 174 */     if (debug0 == null || debug0.isJsonNull()) {
/* 175 */       return ANY;
/*     */     }
/* 177 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "properties");
/*     */     
/* 179 */     List<PropertyMatcher> debug2 = Lists.newArrayList();
/* 180 */     for (Map.Entry<String, JsonElement> debug4 : (Iterable<Map.Entry<String, JsonElement>>)debug1.entrySet()) {
/* 181 */       debug2.add(fromJson(debug4.getKey(), debug4.getValue()));
/*     */     }
/*     */     
/* 184 */     return new StatePropertiesPredicate(debug2);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 188 */     if (this == ANY) {
/* 189 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 192 */     JsonObject debug1 = new JsonObject();
/*     */     
/* 194 */     if (!this.properties.isEmpty()) {
/* 195 */       this.properties.forEach(debug1 -> debug0.add(debug1.getName(), debug1.toJson()));
/*     */     }
/*     */     
/* 198 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 202 */     private final List<StatePropertiesPredicate.PropertyMatcher> matchers = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builder properties() {
/* 208 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder hasProperty(Property<?> debug1, String debug2) {
/* 212 */       this.matchers.add(new StatePropertiesPredicate.ExactPropertyMatcher(debug1.getName(), debug2));
/* 213 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hasProperty(Property<Integer> debug1, int debug2) {
/* 217 */       return hasProperty(debug1, Integer.toString(debug2));
/*     */     }
/*     */     
/*     */     public Builder hasProperty(Property<Boolean> debug1, boolean debug2) {
/* 221 */       return hasProperty(debug1, Boolean.toString(debug2));
/*     */     }
/*     */     
/*     */     public <T extends Comparable<T> & StringRepresentable> Builder hasProperty(Property<T> debug1, T debug2) {
/* 225 */       return hasProperty(debug1, ((StringRepresentable)debug2).getSerializedName());
/*     */     }
/*     */     
/*     */     public StatePropertiesPredicate build() {
/* 229 */       return new StatePropertiesPredicate(this.matchers);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\StatePropertiesPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */