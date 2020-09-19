/*     */ package net.minecraft.commands.synchronization;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.commands.arguments.AngleArgument;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.commands.arguments.DimensionArgument;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.EntitySummonArgument;
/*     */ import net.minecraft.commands.arguments.GameProfileArgument;
/*     */ import net.minecraft.commands.arguments.ItemEnchantmentArgument;
/*     */ import net.minecraft.commands.arguments.MessageArgument;
/*     */ import net.minecraft.commands.arguments.MobEffectArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.NbtTagArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
/*     */ import net.minecraft.commands.arguments.OperationArgument;
/*     */ import net.minecraft.commands.arguments.ParticleArgument;
/*     */ import net.minecraft.commands.arguments.RangeArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.ScoreboardSlotArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.TeamArgument;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*     */ import net.minecraft.commands.synchronization.brigadier.BrigadierArgumentSerializers;
/*     */ import net.minecraft.gametest.framework.TestClassNameArgument;
/*     */ import net.minecraft.gametest.framework.TestFunctionArgument;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ArgumentTypes
/*     */ {
/*  65 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  67 */   private static final Map<Class<?>, Entry<?>> BY_CLASS = Maps.newHashMap();
/*  68 */   private static final Map<ResourceLocation, Entry<?>> BY_NAME = Maps.newHashMap();
/*     */   
/*     */   public static <T extends ArgumentType<?>> void register(String debug0, Class<T> debug1, ArgumentSerializer<T> debug2) {
/*  71 */     ResourceLocation debug3 = new ResourceLocation(debug0);
/*     */     
/*  73 */     if (BY_CLASS.containsKey(debug1)) {
/*  74 */       throw new IllegalArgumentException("Class " + debug1.getName() + " already has a serializer!");
/*     */     }
/*  76 */     if (BY_NAME.containsKey(debug3)) {
/*  77 */       throw new IllegalArgumentException("'" + debug3 + "' is already a registered serializer!");
/*     */     }
/*  79 */     Entry<T> debug4 = new Entry<>(debug1, debug2, debug3);
/*  80 */     BY_CLASS.put(debug1, debug4);
/*  81 */     BY_NAME.put(debug3, debug4);
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/*  85 */     BrigadierArgumentSerializers.bootstrap();
/*  86 */     register("entity", EntityArgument.class, (ArgumentSerializer<EntityArgument>)new EntityArgument.Serializer());
/*  87 */     register("game_profile", GameProfileArgument.class, new EmptyArgumentSerializer<>(GameProfileArgument::gameProfile));
/*  88 */     register("block_pos", BlockPosArgument.class, new EmptyArgumentSerializer<>(BlockPosArgument::blockPos));
/*  89 */     register("column_pos", ColumnPosArgument.class, new EmptyArgumentSerializer<>(ColumnPosArgument::columnPos));
/*  90 */     register("vec3", Vec3Argument.class, new EmptyArgumentSerializer<>(Vec3Argument::vec3));
/*  91 */     register("vec2", Vec2Argument.class, new EmptyArgumentSerializer<>(Vec2Argument::vec2));
/*  92 */     register("block_state", BlockStateArgument.class, new EmptyArgumentSerializer<>(BlockStateArgument::block));
/*  93 */     register("block_predicate", BlockPredicateArgument.class, new EmptyArgumentSerializer<>(BlockPredicateArgument::blockPredicate));
/*  94 */     register("item_stack", ItemArgument.class, new EmptyArgumentSerializer<>(ItemArgument::item));
/*  95 */     register("item_predicate", ItemPredicateArgument.class, new EmptyArgumentSerializer<>(ItemPredicateArgument::itemPredicate));
/*  96 */     register("color", ColorArgument.class, new EmptyArgumentSerializer<>(ColorArgument::color));
/*  97 */     register("component", ComponentArgument.class, new EmptyArgumentSerializer<>(ComponentArgument::textComponent));
/*  98 */     register("message", MessageArgument.class, new EmptyArgumentSerializer<>(MessageArgument::message));
/*  99 */     register("nbt_compound_tag", CompoundTagArgument.class, new EmptyArgumentSerializer<>(CompoundTagArgument::compoundTag));
/* 100 */     register("nbt_tag", NbtTagArgument.class, new EmptyArgumentSerializer<>(NbtTagArgument::nbtTag));
/* 101 */     register("nbt_path", NbtPathArgument.class, new EmptyArgumentSerializer<>(NbtPathArgument::nbtPath));
/* 102 */     register("objective", ObjectiveArgument.class, new EmptyArgumentSerializer<>(ObjectiveArgument::objective));
/* 103 */     register("objective_criteria", ObjectiveCriteriaArgument.class, new EmptyArgumentSerializer<>(ObjectiveCriteriaArgument::criteria));
/* 104 */     register("operation", OperationArgument.class, new EmptyArgumentSerializer<>(OperationArgument::operation));
/* 105 */     register("particle", ParticleArgument.class, new EmptyArgumentSerializer<>(ParticleArgument::particle));
/* 106 */     register("angle", AngleArgument.class, new EmptyArgumentSerializer<>(AngleArgument::angle));
/* 107 */     register("rotation", RotationArgument.class, new EmptyArgumentSerializer<>(RotationArgument::rotation));
/* 108 */     register("scoreboard_slot", ScoreboardSlotArgument.class, new EmptyArgumentSerializer<>(ScoreboardSlotArgument::displaySlot));
/* 109 */     register("score_holder", ScoreHolderArgument.class, (ArgumentSerializer<ScoreHolderArgument>)new ScoreHolderArgument.Serializer());
/* 110 */     register("swizzle", SwizzleArgument.class, new EmptyArgumentSerializer<>(SwizzleArgument::swizzle));
/* 111 */     register("team", TeamArgument.class, new EmptyArgumentSerializer<>(TeamArgument::team));
/* 112 */     register("item_slot", SlotArgument.class, new EmptyArgumentSerializer<>(SlotArgument::slot));
/* 113 */     register("resource_location", ResourceLocationArgument.class, new EmptyArgumentSerializer<>(ResourceLocationArgument::id));
/* 114 */     register("mob_effect", MobEffectArgument.class, new EmptyArgumentSerializer<>(MobEffectArgument::effect));
/* 115 */     register("function", FunctionArgument.class, new EmptyArgumentSerializer<>(FunctionArgument::functions));
/* 116 */     register("entity_anchor", EntityAnchorArgument.class, new EmptyArgumentSerializer<>(EntityAnchorArgument::anchor));
/* 117 */     register("int_range", RangeArgument.Ints.class, new EmptyArgumentSerializer<>(RangeArgument::intRange));
/* 118 */     register("float_range", RangeArgument.Floats.class, new EmptyArgumentSerializer<>(RangeArgument::floatRange));
/* 119 */     register("item_enchantment", ItemEnchantmentArgument.class, new EmptyArgumentSerializer<>(ItemEnchantmentArgument::enchantment));
/* 120 */     register("entity_summon", EntitySummonArgument.class, new EmptyArgumentSerializer<>(EntitySummonArgument::id));
/* 121 */     register("dimension", DimensionArgument.class, new EmptyArgumentSerializer<>(DimensionArgument::dimension));
/* 122 */     register("time", TimeArgument.class, new EmptyArgumentSerializer<>(TimeArgument::time));
/* 123 */     register("uuid", UuidArgument.class, new EmptyArgumentSerializer<>(UuidArgument::uuid));
/*     */     
/* 125 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 126 */       register("test_argument", TestFunctionArgument.class, new EmptyArgumentSerializer<>(TestFunctionArgument::testFunctionArgument));
/* 127 */       register("test_class", TestClassNameArgument.class, new EmptyArgumentSerializer<>(TestClassNameArgument::testClassName));
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Entry<?> get(ResourceLocation debug0) {
/* 133 */     return BY_NAME.get(debug0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Entry<?> get(ArgumentType<?> debug0) {
/* 138 */     return BY_CLASS.get(debug0.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends ArgumentType<?>> void serialize(FriendlyByteBuf debug0, T debug1) {
/* 143 */     Entry<T> debug2 = (Entry)get((ArgumentType<?>)debug1);
/* 144 */     if (debug2 == null) {
/* 145 */       LOGGER.error("Could not serialize {} ({}) - will not be sent to client!", debug1, debug1.getClass());
/* 146 */       debug0.writeResourceLocation(new ResourceLocation(""));
/*     */       
/*     */       return;
/*     */     } 
/* 150 */     debug0.writeResourceLocation(debug2.name);
/* 151 */     debug2.serializer.serializeToNetwork(debug1, debug0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static ArgumentType<?> deserialize(FriendlyByteBuf debug0) {
/* 156 */     ResourceLocation debug1 = debug0.readResourceLocation();
/* 157 */     Entry<?> debug2 = get(debug1);
/*     */     
/* 159 */     if (debug2 == null) {
/* 160 */       LOGGER.error("Could not deserialize {}", debug1);
/* 161 */       return null;
/*     */     } 
/*     */     
/* 164 */     return (ArgumentType<?>)debug2.serializer.deserializeFromNetwork(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends ArgumentType<?>> void serializeToJson(JsonObject debug0, T debug1) {
/* 169 */     Entry<T> debug2 = (Entry)get((ArgumentType<?>)debug1);
/* 170 */     if (debug2 == null) {
/* 171 */       LOGGER.error("Could not serialize argument {} ({})!", debug1, debug1.getClass());
/* 172 */       debug0.addProperty("type", "unknown");
/*     */     } else {
/* 174 */       debug0.addProperty("type", "argument");
/* 175 */       debug0.addProperty("parser", debug2.name.toString());
/*     */       
/* 177 */       JsonObject debug3 = new JsonObject();
/* 178 */       debug2.serializer.serializeToJson(debug1, debug3);
/* 179 */       if (debug3.size() > 0) {
/* 180 */         debug0.add("properties", (JsonElement)debug3);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <S> JsonObject serializeNodeToJson(CommandDispatcher<S> debug0, CommandNode<S> debug1) {
/* 186 */     JsonObject debug2 = new JsonObject();
/*     */     
/* 188 */     if (debug1 instanceof com.mojang.brigadier.tree.RootCommandNode) {
/* 189 */       debug2.addProperty("type", "root");
/* 190 */     } else if (debug1 instanceof com.mojang.brigadier.tree.LiteralCommandNode) {
/* 191 */       debug2.addProperty("type", "literal");
/* 192 */     } else if (debug1 instanceof ArgumentCommandNode) {
/* 193 */       serializeToJson(debug2, ((ArgumentCommandNode)debug1).getType());
/*     */     } else {
/* 195 */       LOGGER.error("Could not serialize node {} ({})!", debug1, debug1.getClass());
/*     */       
/* 197 */       debug2.addProperty("type", "unknown");
/*     */     } 
/*     */     
/* 200 */     JsonObject debug3 = new JsonObject();
/* 201 */     for (CommandNode<S> debug5 : (Iterable<CommandNode<S>>)debug1.getChildren()) {
/* 202 */       debug3.add(debug5.getName(), (JsonElement)serializeNodeToJson(debug0, debug5));
/*     */     }
/* 204 */     if (debug3.size() > 0) {
/* 205 */       debug2.add("children", (JsonElement)debug3);
/*     */     }
/*     */     
/* 208 */     if (debug1.getCommand() != null) {
/* 209 */       debug2.addProperty("executable", Boolean.valueOf(true));
/*     */     }
/*     */     
/* 212 */     if (debug1.getRedirect() != null) {
/* 213 */       Collection<String> debug4 = debug0.getPath(debug1.getRedirect());
/* 214 */       if (!debug4.isEmpty()) {
/* 215 */         JsonArray debug5 = new JsonArray();
/* 216 */         for (String debug7 : debug4) {
/* 217 */           debug5.add(debug7);
/*     */         }
/* 219 */         debug2.add("redirect", (JsonElement)debug5);
/*     */       } 
/*     */     } 
/*     */     
/* 223 */     return debug2;
/*     */   }
/*     */   
/*     */   public static boolean isTypeRegistered(ArgumentType<?> debug0) {
/* 227 */     return (get(debug0) != null);
/*     */   }
/*     */   
/*     */   public static <T> Set<ArgumentType<?>> findUsedArgumentTypes(CommandNode<T> debug0) {
/* 231 */     Set<CommandNode<T>> debug1 = Sets.newIdentityHashSet();
/* 232 */     Set<ArgumentType<?>> debug2 = Sets.newHashSet();
/* 233 */     findUsedArgumentTypes(debug0, debug2, debug1);
/* 234 */     return debug2;
/*     */   }
/*     */   
/*     */   private static <T> void findUsedArgumentTypes(CommandNode<T> debug0, Set<ArgumentType<?>> debug1, Set<CommandNode<T>> debug2) {
/* 238 */     if (!debug2.add(debug0)) {
/*     */       return;
/*     */     }
/*     */     
/* 242 */     if (debug0 instanceof ArgumentCommandNode) {
/* 243 */       debug1.add(((ArgumentCommandNode)debug0).getType());
/*     */     }
/*     */     
/* 246 */     debug0.getChildren().forEach(debug2 -> findUsedArgumentTypes(debug2, debug0, debug1));
/* 247 */     CommandNode<T> debug3 = debug0.getRedirect();
/* 248 */     if (debug3 != null)
/* 249 */       findUsedArgumentTypes(debug3, debug1, debug2); 
/*     */   }
/*     */   
/*     */   static class Entry<T extends ArgumentType<?>>
/*     */   {
/*     */     public final Class<T> clazz;
/*     */     public final ArgumentSerializer<T> serializer;
/*     */     public final ResourceLocation name;
/*     */     
/*     */     private Entry(Class<T> debug1, ArgumentSerializer<T> debug2, ResourceLocation debug3) {
/* 259 */       this.clazz = debug1;
/* 260 */       this.serializer = debug2;
/* 261 */       this.name = debug3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\ArgumentTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */