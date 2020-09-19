/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.critereon.DeserializationContext;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ public class Advancement
/*     */ {
/*     */   private final Advancement parent;
/*     */   private final DisplayInfo display;
/*     */   private final AdvancementRewards rewards;
/*  37 */   private final Set<Advancement> children = Sets.newLinkedHashSet(); private final ResourceLocation id; private final Map<String, Criterion> criteria; private final String[][] requirements;
/*     */   private final Component chatComponent;
/*     */   
/*     */   public Advancement(ResourceLocation debug1, @Nullable Advancement debug2, @Nullable DisplayInfo debug3, AdvancementRewards debug4, Map<String, Criterion> debug5, String[][] debug6) {
/*  41 */     this.id = debug1;
/*  42 */     this.display = debug3;
/*  43 */     this.criteria = (Map<String, Criterion>)ImmutableMap.copyOf(debug5);
/*  44 */     this.parent = debug2;
/*  45 */     this.rewards = debug4;
/*  46 */     this.requirements = debug6;
/*  47 */     if (debug2 != null) {
/*  48 */       debug2.addChild(this);
/*     */     }
/*     */     
/*  51 */     if (debug3 == null) {
/*  52 */       this.chatComponent = (Component)new TextComponent(debug1.toString());
/*     */     } else {
/*  54 */       Component debug7 = debug3.getTitle();
/*  55 */       ChatFormatting debug8 = debug3.getFrame().getChatColor();
/*     */       
/*  57 */       MutableComponent mutableComponent1 = ComponentUtils.mergeStyles(debug7.copy(), Style.EMPTY.withColor(debug8)).append("\n").append(debug3.getDescription());
/*  58 */       MutableComponent mutableComponent2 = debug7.copy().withStyle(debug1 -> debug1.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, debug0)));
/*     */       
/*  60 */       this.chatComponent = (Component)ComponentUtils.wrapInSquareBrackets((Component)mutableComponent2).withStyle(debug8);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Builder deconstruct() {
/*  65 */     return new Builder((this.parent == null) ? null : this.parent.getId(), this.display, this.rewards, this.criteria, this.requirements);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Advancement getParent() {
/*  70 */     return this.parent;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public DisplayInfo getDisplay() {
/*  75 */     return this.display;
/*     */   }
/*     */   
/*     */   public AdvancementRewards getRewards() {
/*  79 */     return this.rewards;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     return "SimpleAdvancement{id=" + 
/*  85 */       getId() + ", parent=" + ((this.parent == null) ? "null" : (String)this.parent
/*  86 */       .getId()) + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + 
/*     */ 
/*     */ 
/*     */       
/*  90 */       Arrays.deepToString((Object[])this.requirements) + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<Advancement> getChildren() {
/*  95 */     return this.children;
/*     */   }
/*     */   
/*     */   public Map<String, Criterion> getCriteria() {
/*  99 */     return this.criteria;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChild(Advancement debug1) {
/* 107 */     this.children.add(debug1);
/*     */   }
/*     */   
/*     */   public ResourceLocation getId() {
/* 111 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 116 */     if (this == debug1) {
/* 117 */       return true;
/*     */     }
/* 119 */     if (!(debug1 instanceof Advancement)) {
/* 120 */       return false;
/*     */     }
/* 122 */     Advancement debug2 = (Advancement)debug1;
/* 123 */     return this.id.equals(debug2.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return this.id.hashCode();
/*     */   }
/*     */   
/*     */   public String[][] getRequirements() {
/* 132 */     return this.requirements;
/*     */   }
/*     */   
/*     */   public Component getChatComponent() {
/* 136 */     return this.chatComponent;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private ResourceLocation parentId;
/*     */     private Advancement parent;
/*     */     private DisplayInfo display;
/* 143 */     private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/* 144 */     private Map<String, Criterion> criteria = Maps.newLinkedHashMap();
/*     */     private String[][] requirements;
/* 146 */     private RequirementsStrategy requirementsStrategy = RequirementsStrategy.AND;
/*     */     
/*     */     private Builder(@Nullable ResourceLocation debug1, @Nullable DisplayInfo debug2, AdvancementRewards debug3, Map<String, Criterion> debug4, String[][] debug5) {
/* 149 */       this.parentId = debug1;
/* 150 */       this.display = debug2;
/* 151 */       this.rewards = debug3;
/* 152 */       this.criteria = debug4;
/* 153 */       this.requirements = debug5;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builder advancement() {
/* 160 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder parent(Advancement debug1) {
/* 164 */       this.parent = debug1;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder parent(ResourceLocation debug1) {
/* 169 */       this.parentId = debug1;
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder display(ItemStack debug1, Component debug2, Component debug3, @Nullable ResourceLocation debug4, FrameType debug5, boolean debug6, boolean debug7, boolean debug8) {
/* 174 */       return display(new DisplayInfo(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8));
/*     */     }
/*     */     
/*     */     public Builder display(ItemLike debug1, Component debug2, Component debug3, @Nullable ResourceLocation debug4, FrameType debug5, boolean debug6, boolean debug7, boolean debug8) {
/* 178 */       return display(new DisplayInfo(new ItemStack((ItemLike)debug1.asItem()), debug2, debug3, debug4, debug5, debug6, debug7, debug8));
/*     */     }
/*     */     
/*     */     public Builder display(DisplayInfo debug1) {
/* 182 */       this.display = debug1;
/* 183 */       return this;
/*     */     }
/*     */     
/*     */     public Builder rewards(AdvancementRewards.Builder debug1) {
/* 187 */       return rewards(debug1.build());
/*     */     }
/*     */     
/*     */     public Builder rewards(AdvancementRewards debug1) {
/* 191 */       this.rewards = debug1;
/* 192 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCriterion(String debug1, CriterionTriggerInstance debug2) {
/* 196 */       return addCriterion(debug1, new Criterion(debug2));
/*     */     }
/*     */     
/*     */     public Builder addCriterion(String debug1, Criterion debug2) {
/* 200 */       if (this.criteria.containsKey(debug1)) {
/* 201 */         throw new IllegalArgumentException("Duplicate criterion " + debug1);
/*     */       }
/* 203 */       this.criteria.put(debug1, debug2);
/* 204 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(RequirementsStrategy debug1) {
/* 208 */       this.requirementsStrategy = debug1;
/* 209 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canBuild(Function<ResourceLocation, Advancement> debug1) {
/* 218 */       if (this.parentId == null)
/* 219 */         return true; 
/* 220 */       if (this.parent == null) {
/* 221 */         this.parent = debug1.apply(this.parentId);
/*     */       }
/* 223 */       return (this.parent != null);
/*     */     }
/*     */     
/*     */     public Advancement build(ResourceLocation debug1) {
/* 227 */       if (!canBuild(debug0 -> null)) {
/* 228 */         throw new IllegalStateException("Tried to build incomplete advancement!");
/*     */       }
/* 230 */       if (this.requirements == null) {
/* 231 */         this.requirements = this.requirementsStrategy.createRequirements(this.criteria.keySet());
/*     */       }
/* 233 */       return new Advancement(debug1, this.parent, this.display, this.rewards, this.criteria, this.requirements);
/*     */     }
/*     */     
/*     */     public Advancement save(Consumer<Advancement> debug1, String debug2) {
/* 237 */       Advancement debug3 = build(new ResourceLocation(debug2));
/* 238 */       debug1.accept(debug3);
/* 239 */       return debug3;
/*     */     }
/*     */     
/*     */     public JsonObject serializeToJson() {
/* 243 */       if (this.requirements == null) {
/* 244 */         this.requirements = this.requirementsStrategy.createRequirements(this.criteria.keySet());
/*     */       }
/*     */       
/* 247 */       JsonObject debug1 = new JsonObject();
/*     */       
/* 249 */       if (this.parent != null) {
/* 250 */         debug1.addProperty("parent", this.parent.getId().toString());
/* 251 */       } else if (this.parentId != null) {
/* 252 */         debug1.addProperty("parent", this.parentId.toString());
/*     */       } 
/*     */       
/* 255 */       if (this.display != null) {
/* 256 */         debug1.add("display", this.display.serializeToJson());
/*     */       }
/*     */       
/* 259 */       debug1.add("rewards", this.rewards.serializeToJson());
/*     */       
/* 261 */       JsonObject debug2 = new JsonObject();
/* 262 */       for (Map.Entry<String, Criterion> debug4 : this.criteria.entrySet()) {
/* 263 */         debug2.add(debug4.getKey(), ((Criterion)debug4.getValue()).serializeToJson());
/*     */       }
/* 265 */       debug1.add("criteria", (JsonElement)debug2);
/*     */       
/* 267 */       JsonArray debug3 = new JsonArray();
/* 268 */       for (String[] debug7 : this.requirements) {
/* 269 */         JsonArray debug8 = new JsonArray();
/* 270 */         for (String debug12 : debug7) {
/* 271 */           debug8.add(debug12);
/*     */         }
/* 273 */         debug3.add((JsonElement)debug8);
/*     */       } 
/* 275 */       debug1.add("requirements", (JsonElement)debug3);
/*     */       
/* 277 */       return debug1;
/*     */     }
/*     */     
/*     */     public void serializeToNetwork(FriendlyByteBuf debug1) {
/* 281 */       if (this.parentId == null) {
/* 282 */         debug1.writeBoolean(false);
/*     */       } else {
/* 284 */         debug1.writeBoolean(true);
/* 285 */         debug1.writeResourceLocation(this.parentId);
/*     */       } 
/* 287 */       if (this.display == null) {
/* 288 */         debug1.writeBoolean(false);
/*     */       } else {
/* 290 */         debug1.writeBoolean(true);
/* 291 */         this.display.serializeToNetwork(debug1);
/*     */       } 
/* 293 */       Criterion.serializeToNetwork(this.criteria, debug1);
/* 294 */       debug1.writeVarInt(this.requirements.length);
/* 295 */       for (String[] debug5 : this.requirements) {
/* 296 */         debug1.writeVarInt(debug5.length);
/* 297 */         for (String debug9 : debug5) {
/* 298 */           debug1.writeUtf(debug9);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 305 */       return "Task Advancement{parentId=" + this.parentId + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 310 */         Arrays.deepToString((Object[])this.requirements) + '}';
/*     */     }
/*     */ 
/*     */     
/*     */     public static Builder fromJson(JsonObject debug0, DeserializationContext debug1) {
/* 315 */       ResourceLocation debug2 = debug0.has("parent") ? new ResourceLocation(GsonHelper.getAsString(debug0, "parent")) : null;
/* 316 */       DisplayInfo debug3 = debug0.has("display") ? DisplayInfo.fromJson(GsonHelper.getAsJsonObject(debug0, "display")) : null;
/* 317 */       AdvancementRewards debug4 = debug0.has("rewards") ? AdvancementRewards.deserialize(GsonHelper.getAsJsonObject(debug0, "rewards")) : AdvancementRewards.EMPTY;
/* 318 */       Map<String, Criterion> debug5 = Criterion.criteriaFromJson(GsonHelper.getAsJsonObject(debug0, "criteria"), debug1);
/* 319 */       if (debug5.isEmpty()) {
/* 320 */         throw new JsonSyntaxException("Advancement criteria cannot be empty");
/*     */       }
/* 322 */       JsonArray debug6 = GsonHelper.getAsJsonArray(debug0, "requirements", new JsonArray());
/* 323 */       String[][] debug7 = new String[debug6.size()][]; int debug8;
/* 324 */       for (debug8 = 0; debug8 < debug6.size(); debug8++) {
/* 325 */         JsonArray debug9 = GsonHelper.convertToJsonArray(debug6.get(debug8), "requirements[" + debug8 + "]");
/* 326 */         debug7[debug8] = new String[debug9.size()];
/* 327 */         for (int debug10 = 0; debug10 < debug9.size(); debug10++) {
/* 328 */           debug7[debug8][debug10] = GsonHelper.convertToString(debug9.get(debug10), "requirements[" + debug8 + "][" + debug10 + "]");
/*     */         }
/*     */       } 
/* 331 */       if (debug7.length == 0) {
/* 332 */         debug7 = new String[debug5.size()][];
/* 333 */         debug8 = 0;
/* 334 */         for (String debug10 : debug5.keySet()) {
/* 335 */           (new String[1])[0] = debug10; debug7[debug8++] = new String[1];
/*     */         } 
/*     */       } 
/* 338 */       for (String[] debug11 : debug7) {
/* 339 */         if (debug11.length == 0 && debug5.isEmpty()) {
/* 340 */           throw new JsonSyntaxException("Requirement entry cannot be empty");
/*     */         }
/* 342 */         for (String debug15 : debug11) {
/* 343 */           if (!debug5.containsKey(debug15)) {
/* 344 */             throw new JsonSyntaxException("Unknown required criterion '" + debug15 + "'");
/*     */           }
/*     */         } 
/*     */       } 
/* 348 */       for (String debug9 : debug5.keySet()) {
/* 349 */         boolean debug10 = false;
/* 350 */         for (String[] debug14 : debug7) {
/* 351 */           if (ArrayUtils.contains((Object[])debug14, debug9)) {
/* 352 */             debug10 = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 356 */         if (!debug10) {
/* 357 */           throw new JsonSyntaxException("Criterion '" + debug9 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
/*     */         }
/*     */       } 
/* 360 */       return new Builder(debug2, debug3, debug4, debug5, debug7);
/*     */     }
/*     */     
/*     */     public static Builder fromNetwork(FriendlyByteBuf debug0) {
/* 364 */       ResourceLocation debug1 = debug0.readBoolean() ? debug0.readResourceLocation() : null;
/* 365 */       DisplayInfo debug2 = debug0.readBoolean() ? DisplayInfo.fromNetwork(debug0) : null;
/* 366 */       Map<String, Criterion> debug3 = Criterion.criteriaFromNetwork(debug0);
/* 367 */       String[][] debug4 = new String[debug0.readVarInt()][];
/* 368 */       for (int debug5 = 0; debug5 < debug4.length; debug5++) {
/* 369 */         debug4[debug5] = new String[debug0.readVarInt()];
/* 370 */         for (int debug6 = 0; debug6 < (debug4[debug5]).length; debug6++) {
/* 371 */           debug4[debug5][debug6] = debug0.readUtf(32767);
/*     */         }
/*     */       } 
/* 374 */       return new Builder(debug1, debug2, AdvancementRewards.EMPTY, debug3, debug4);
/*     */     }
/*     */     
/*     */     public Map<String, Criterion> getCriteria() {
/* 378 */       return this.criteria;
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\Advancement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */