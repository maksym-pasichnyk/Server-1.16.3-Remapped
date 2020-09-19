/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.critereon.NbtPredicate;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class CopyNbtFunction extends LootItemConditionalFunction {
/*     */   private final DataSource source;
/*     */   private final List<CopyOperation> operations;
/*     */   
/*     */   static class CopyOperation {
/*     */     private final String sourcePathText;
/*     */     private final NbtPathArgument.NbtPath sourcePath;
/*     */     private final String targetPathText;
/*     */     private final NbtPathArgument.NbtPath targetPath;
/*     */     private final CopyNbtFunction.MergeStrategy op;
/*     */     
/*     */     private CopyOperation(String debug1, String debug2, CopyNbtFunction.MergeStrategy debug3) {
/*  42 */       this.sourcePathText = debug1;
/*  43 */       this.sourcePath = CopyNbtFunction.compileNbtPath(debug1);
/*  44 */       this.targetPathText = debug2;
/*  45 */       this.targetPath = CopyNbtFunction.compileNbtPath(debug2);
/*  46 */       this.op = debug3;
/*     */     }
/*     */     
/*     */     public void apply(Supplier<Tag> debug1, Tag debug2) {
/*     */       try {
/*  51 */         List<Tag> debug3 = this.sourcePath.get(debug2);
/*  52 */         if (!debug3.isEmpty()) {
/*  53 */           this.op.merge(debug1.get(), this.targetPath, debug3);
/*     */         }
/*  55 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonObject toJson() {
/*  61 */       JsonObject debug1 = new JsonObject();
/*  62 */       debug1.addProperty("source", this.sourcePathText);
/*  63 */       debug1.addProperty("target", this.targetPathText);
/*  64 */       debug1.addProperty("op", this.op.name);
/*  65 */       return debug1;
/*     */     }
/*     */     
/*     */     public static CopyOperation fromJson(JsonObject debug0) {
/*  69 */       String debug1 = GsonHelper.getAsString(debug0, "source");
/*  70 */       String debug2 = GsonHelper.getAsString(debug0, "target");
/*  71 */       CopyNbtFunction.MergeStrategy debug3 = CopyNbtFunction.MergeStrategy.getByName(GsonHelper.getAsString(debug0, "op"));
/*  72 */       return new CopyOperation(debug1, debug2, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CopyNbtFunction(LootItemCondition[] debug1, DataSource debug2, List<CopyOperation> debug3) {
/*  80 */     super(debug1);
/*  81 */     this.source = debug2;
/*  82 */     this.operations = (List<CopyOperation>)ImmutableList.copyOf(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  87 */     return LootItemFunctions.COPY_NBT;
/*     */   }
/*     */   
/*     */   private static NbtPathArgument.NbtPath compileNbtPath(String debug0) {
/*     */     try {
/*  92 */       return (new NbtPathArgument()).parse(new StringReader(debug0));
/*  93 */     } catch (CommandSyntaxException debug1) {
/*  94 */       throw new IllegalArgumentException("Failed to parse path " + debug0, debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 100 */     return (Set<LootContextParam<?>>)ImmutableSet.of(this.source.param);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 105 */     Tag debug3 = this.source.getter.apply(debug2);
/* 106 */     if (debug3 != null) {
/* 107 */       this.operations.forEach(debug2 -> debug2.apply(debug0::getOrCreateTag, debug1));
/*     */     }
/*     */     
/* 110 */     return debug1;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final CopyNbtFunction.DataSource source;
/* 115 */     private final List<CopyNbtFunction.CopyOperation> ops = Lists.newArrayList();
/*     */     
/*     */     private Builder(CopyNbtFunction.DataSource debug1) {
/* 118 */       this.source = debug1;
/*     */     }
/*     */     
/*     */     public Builder copy(String debug1, String debug2, CopyNbtFunction.MergeStrategy debug3) {
/* 122 */       this.ops.add(new CopyNbtFunction.CopyOperation(debug1, debug2, debug3));
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public Builder copy(String debug1, String debug2) {
/* 127 */       return copy(debug1, debug2, CopyNbtFunction.MergeStrategy.REPLACE);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/* 132 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 137 */       return new CopyNbtFunction(getConditions(), this.source, this.ops);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder copyData(DataSource debug0) {
/* 142 */     return new Builder(debug0);
/*     */   }
/*     */   private static final Function<BlockEntity, Tag> BLOCK_ENTITY_GETTER;
/* 145 */   private static final Function<Entity, Tag> ENTITY_GETTER = NbtPredicate::getEntityTagToCompare; static {
/* 146 */     BLOCK_ENTITY_GETTER = (debug0 -> debug0.save(new CompoundTag()));
/*     */   }
/*     */   
/* 149 */   public enum MergeStrategy { REPLACE("replace")
/*     */     {
/*     */       public void merge(Tag debug1, NbtPathArgument.NbtPath debug2, List<Tag> debug3) throws CommandSyntaxException {
/* 152 */         debug2.set(debug1, (Tag)Iterables.getLast(debug3)::copy);
/*     */       }
/*     */     },
/* 155 */     APPEND("append")
/*     */     {
/*     */       public void merge(Tag debug1, NbtPathArgument.NbtPath debug2, List<Tag> debug3) throws CommandSyntaxException {
/* 158 */         List<Tag> debug4 = debug2.getOrCreate(debug1, ListTag::new);
/* 159 */         debug4.forEach(debug1 -> {
/*     */               
/*     */               if (debug1 instanceof ListTag) {
/*     */                 debug0.forEach(());
/*     */               }
/*     */             });
/*     */       }
/*     */     },
/* 167 */     MERGE("merge")
/*     */     {
/*     */       public void merge(Tag debug1, NbtPathArgument.NbtPath debug2, List<Tag> debug3) throws CommandSyntaxException {
/* 170 */         List<Tag> debug4 = debug2.getOrCreate(debug1, CompoundTag::new);
/* 171 */         debug4.forEach(debug1 -> {
/*     */               if (debug1 instanceof CompoundTag) {
/*     */                 debug0.forEach(());
/*     */               }
/*     */             });
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MergeStrategy(String debug3) {
/* 189 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     public static MergeStrategy getByName(String debug0) {
/* 193 */       for (MergeStrategy debug4 : values()) {
/* 194 */         if (debug4.name.equals(debug0)) {
/* 195 */           return debug4;
/*     */         }
/*     */       } 
/* 198 */       throw new IllegalArgumentException("Invalid merge strategy" + debug0);
/*     */     }
/*     */     
/*     */     public abstract void merge(Tag param1Tag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException; }
/*     */   
/* 203 */   public enum DataSource { THIS("this", LootContextParams.THIS_ENTITY, (String)CopyNbtFunction.ENTITY_GETTER),
/* 204 */     KILLER("killer", LootContextParams.KILLER_ENTITY, (String)CopyNbtFunction.ENTITY_GETTER),
/* 205 */     KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER, (String)CopyNbtFunction.ENTITY_GETTER),
/* 206 */     BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY, (String)CopyNbtFunction.BLOCK_ENTITY_GETTER);
/*     */     
/*     */     public final String name;
/*     */     public final LootContextParam<?> param;
/*     */     public final Function<LootContext, Tag> getter;
/*     */     
/*     */     <T> DataSource(String debug3, LootContextParam<T> debug4, Function<? super T, Tag> debug5) {
/* 213 */       this.name = debug3;
/* 214 */       this.param = debug4;
/* 215 */       this.getter = (debug2 -> {
/*     */           T debug3 = (T)debug2.getParamOrNull(debug0);
/*     */           return (debug3 != null) ? debug1.apply(debug3) : null;
/*     */         });
/*     */     }
/*     */     
/*     */     public static DataSource getByName(String debug0) {
/* 222 */       for (DataSource debug4 : values()) {
/* 223 */         if (debug4.name.equals(debug0)) {
/* 224 */           return debug4;
/*     */         }
/*     */       } 
/* 227 */       throw new IllegalArgumentException("Invalid tag source " + debug0);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<CopyNbtFunction> {
/*     */     public void serialize(JsonObject debug1, CopyNbtFunction debug2, JsonSerializationContext debug3) {
/* 234 */       super.serialize(debug1, debug2, debug3);
/* 235 */       debug1.addProperty("source", debug2.source.name);
/* 236 */       JsonArray debug4 = new JsonArray();
/* 237 */       debug2.operations.stream().map(CopyNbtFunction.CopyOperation::toJson).forEach(debug4::add);
/* 238 */       debug1.add("ops", (JsonElement)debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public CopyNbtFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 243 */       CopyNbtFunction.DataSource debug4 = CopyNbtFunction.DataSource.getByName(GsonHelper.getAsString(debug1, "source"));
/* 244 */       List<CopyNbtFunction.CopyOperation> debug5 = Lists.newArrayList();
/* 245 */       JsonArray debug6 = GsonHelper.getAsJsonArray(debug1, "ops");
/* 246 */       for (JsonElement debug8 : debug6) {
/* 247 */         JsonObject debug9 = GsonHelper.convertToJsonObject(debug8, "op");
/* 248 */         debug5.add(CopyNbtFunction.CopyOperation.fromJson(debug9));
/*     */       } 
/* 250 */       return new CopyNbtFunction(debug3, debug4, debug5);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyNbtFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */