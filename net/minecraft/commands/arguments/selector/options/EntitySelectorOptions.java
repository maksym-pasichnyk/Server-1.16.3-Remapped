/*     */ package net.minecraft.commands.arguments.selector.options;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.advancements.critereon.WrappedMinMaxBounds;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.ServerAdvancementManager;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ public class EntitySelectorOptions {
/*  53 */   private static final Map<String, Option> OPTIONS = Maps.newHashMap(); public static final DynamicCommandExceptionType ERROR_UNKNOWN_OPTION; public static final DynamicCommandExceptionType ERROR_INAPPLICABLE_OPTION;
/*     */   static {
/*  55 */     ERROR_UNKNOWN_OPTION = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.options.unknown", new Object[] { debug0 }));
/*  56 */     ERROR_INAPPLICABLE_OPTION = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.options.inapplicable", new Object[] { debug0 }));
/*  57 */   } public static final SimpleCommandExceptionType ERROR_RANGE_NEGATIVE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.distance.negative"));
/*  58 */   public static final SimpleCommandExceptionType ERROR_LEVEL_NEGATIVE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.level.negative"));
/*  59 */   public static final SimpleCommandExceptionType ERROR_LIMIT_TOO_SMALL = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.limit.toosmall")); public static final DynamicCommandExceptionType ERROR_SORT_UNKNOWN; public static final DynamicCommandExceptionType ERROR_GAME_MODE_INVALID; public static final DynamicCommandExceptionType ERROR_ENTITY_TYPE_INVALID; static {
/*  60 */     ERROR_SORT_UNKNOWN = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.options.sort.irreversible", new Object[] { debug0 }));
/*  61 */     ERROR_GAME_MODE_INVALID = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.options.mode.invalid", new Object[] { debug0 }));
/*  62 */     ERROR_ENTITY_TYPE_INVALID = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.options.type.invalid", new Object[] { debug0 }));
/*     */   }
/*     */   private static void register(String debug0, Modifier debug1, Predicate<EntitySelectorParser> debug2, Component debug3) {
/*  65 */     OPTIONS.put(debug0, new Option(debug1, debug2, debug3));
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/*  69 */     if (!OPTIONS.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     register("name", debug0 -> { int debug1 = debug0.getReader().getCursor(); boolean debug2 = debug0.shouldInvertValue(); String debug3 = debug0.getReader().readString(); if (debug0.hasNameNotEquals() && !debug2) { debug0.getReader().setCursor(debug1); throw ERROR_INAPPLICABLE_OPTION.createWithContext(debug0.getReader(), "name"); }  if (debug2) { debug0.setHasNameNotEquals(true); } else { debug0.setHasNameEquals(true); }  debug0.addPredicate(()); }debug0 -> !debug0.hasNameEquals(), (Component)new TranslatableComponent("argument.entity.options.name.description"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     register("distance", debug0 -> { int debug1 = debug0.getReader().getCursor(); MinMaxBounds.Floats debug2 = MinMaxBounds.Floats.fromReader(debug0.getReader()); if ((debug2.getMin() != null && ((Float)debug2.getMin()).floatValue() < 0.0F) || (debug2.getMax() != null && ((Float)debug2.getMax()).floatValue() < 0.0F)) { debug0.getReader().setCursor(debug1); throw ERROR_RANGE_NEGATIVE.createWithContext(debug0.getReader()); }  debug0.setDistance(debug2); debug0.setWorldLimited(); }debug0 -> debug0.getDistance().isAny(), (Component)new TranslatableComponent("argument.entity.options.distance.description"));
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
/* 100 */     register("level", debug0 -> { int debug1 = debug0.getReader().getCursor(); MinMaxBounds.Ints debug2 = MinMaxBounds.Ints.fromReader(debug0.getReader()); if ((debug2.getMin() != null && ((Integer)debug2.getMin()).intValue() < 0) || (debug2.getMax() != null && ((Integer)debug2.getMax()).intValue() < 0)) { debug0.getReader().setCursor(debug1); throw ERROR_LEVEL_NEGATIVE.createWithContext(debug0.getReader()); }  debug0.setLevel(debug2); debug0.setIncludesEntities(false); }debug0 -> debug0.getLevel().isAny(), (Component)new TranslatableComponent("argument.entity.options.level.description"));
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
/* 111 */     register("x", debug0 -> { debug0.setWorldLimited(); debug0.setX(debug0.getReader().readDouble()); }debug0 -> (debug0.getX() == null), (Component)new TranslatableComponent("argument.entity.options.x.description"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     register("y", debug0 -> { debug0.setWorldLimited(); debug0.setY(debug0.getReader().readDouble()); }debug0 -> (debug0.getY() == null), (Component)new TranslatableComponent("argument.entity.options.y.description"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     register("z", debug0 -> { debug0.setWorldLimited(); debug0.setZ(debug0.getReader().readDouble()); }debug0 -> (debug0.getZ() == null), (Component)new TranslatableComponent("argument.entity.options.z.description"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     register("dx", debug0 -> { debug0.setWorldLimited(); debug0.setDeltaX(debug0.getReader().readDouble()); }debug0 -> (debug0.getDeltaX() == null), (Component)new TranslatableComponent("argument.entity.options.dx.description"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     register("dy", debug0 -> { debug0.setWorldLimited(); debug0.setDeltaY(debug0.getReader().readDouble()); }debug0 -> (debug0.getDeltaY() == null), (Component)new TranslatableComponent("argument.entity.options.dy.description"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     register("dz", debug0 -> { debug0.setWorldLimited(); debug0.setDeltaZ(debug0.getReader().readDouble()); }debug0 -> (debug0.getDeltaZ() == null), (Component)new TranslatableComponent("argument.entity.options.dz.description"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     register("x_rotation", debug0 -> debug0.setRotX(WrappedMinMaxBounds.fromReader(debug0.getReader(), true, Mth::wrapDegrees)), debug0 -> (debug0.getRotX() == WrappedMinMaxBounds.ANY), (Component)new TranslatableComponent("argument.entity.options.x_rotation.description"));
/*     */ 
/*     */ 
/*     */     
/* 145 */     register("y_rotation", debug0 -> debug0.setRotY(WrappedMinMaxBounds.fromReader(debug0.getReader(), true, Mth::wrapDegrees)), debug0 -> (debug0.getRotY() == WrappedMinMaxBounds.ANY), (Component)new TranslatableComponent("argument.entity.options.y_rotation.description"));
/*     */ 
/*     */ 
/*     */     
/* 149 */     register("limit", debug0 -> {
/*     */           int debug1 = debug0.getReader().getCursor();
/*     */           int debug2 = debug0.getReader().readInt();
/*     */           if (debug2 < 1) {
/*     */             debug0.getReader().setCursor(debug1);
/*     */             throw ERROR_LIMIT_TOO_SMALL.createWithContext(debug0.getReader());
/*     */           } 
/*     */           debug0.setMaxResults(debug2);
/*     */           debug0.setLimited(true);
/* 158 */         }debug0 -> (!debug0.isCurrentEntity() && !debug0.isLimited()), (Component)new TranslatableComponent("argument.entity.options.limit.description"));
/*     */     
/* 160 */     register("sort", debug0 -> {
/*     */           BiConsumer<Vec3, List<? extends Entity>> debug3;
/*     */           int debug1 = debug0.getReader().getCursor();
/*     */           String debug2 = debug0.getReader().readUnquotedString();
/*     */           debug0.setSuggestions(());
/*     */           switch (debug2) {
/*     */             case "nearest":
/*     */               debug3 = EntitySelectorParser.ORDER_NEAREST;
/*     */               break;
/*     */             case "furthest":
/*     */               debug3 = EntitySelectorParser.ORDER_FURTHEST;
/*     */               break;
/*     */             case "random":
/*     */               debug3 = EntitySelectorParser.ORDER_RANDOM;
/*     */               break;
/*     */             case "arbitrary":
/*     */               debug3 = EntitySelectorParser.ORDER_ARBITRARY;
/*     */               break;
/*     */             default:
/*     */               debug0.getReader().setCursor(debug1);
/*     */               throw ERROR_SORT_UNKNOWN.createWithContext(debug0.getReader(), debug2);
/*     */           } 
/*     */           debug0.setOrder(debug3);
/*     */           debug0.setSorted(true);
/* 184 */         }debug0 -> (!debug0.isCurrentEntity() && !debug0.isSorted()), (Component)new TranslatableComponent("argument.entity.options.sort.description"));
/*     */     
/* 186 */     register("gamemode", debug0 -> { debug0.setSuggestions(()); int debug1 = debug0.getReader().getCursor(); boolean debug2 = debug0.shouldInvertValue(); if (debug0.hasGamemodeNotEquals() && !debug2) { debug0.getReader().setCursor(debug1); throw ERROR_INAPPLICABLE_OPTION.createWithContext(debug0.getReader(), "gamemode"); }  String debug3 = debug0.getReader().readUnquotedString(); GameType debug4 = GameType.byName(debug3, GameType.NOT_SET); if (debug4 == GameType.NOT_SET) { debug0.getReader().setCursor(debug1); throw ERROR_GAME_MODE_INVALID.createWithContext(debug0.getReader(), debug3); }  debug0.setIncludesEntities(false); debug0.addPredicate(()); if (debug2) { debug0.setHasGamemodeNotEquals(true); } else { debug0.setHasGamemodeEquals(true); }  }debug0 -> !debug0.hasGamemodeEquals(), (Component)new TranslatableComponent("argument.entity.options.gamemode.description"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     register("team", debug0 -> { boolean debug1 = debug0.shouldInvertValue(); String debug2 = debug0.getReader().readUnquotedString(); debug0.addPredicate(()); if (debug1) { debug0.setHasTeamNotEquals(true); } else { debug0.setHasTeamEquals(true); }  }debug0 -> !debug0.hasTeamEquals(), (Component)new TranslatableComponent("argument.entity.options.team.description"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     register("type", debug0 -> { debug0.setSuggestions(()); int debug1 = debug0.getReader().getCursor(); boolean debug2 = debug0.shouldInvertValue(); if (debug0.isTypeLimitedInversely() && !debug2) { debug0.getReader().setCursor(debug1); throw ERROR_INAPPLICABLE_OPTION.createWithContext(debug0.getReader(), "type"); }  if (debug2) debug0.setTypeLimitedInversely();  if (debug0.isTag()) { ResourceLocation debug3 = ResourceLocation.read(debug0.getReader()); debug0.addPredicate(()); } else { ResourceLocation debug3 = ResourceLocation.read(debug0.getReader()); EntityType<?> debug4 = (EntityType)Registry.ENTITY_TYPE.getOptional(debug3).orElseThrow(()); if (Objects.equals(EntityType.PLAYER, debug4) && !debug2) debug0.setIncludesEntities(false);  debug0.addPredicate(()); if (!debug2) debug0.limitToType(debug4);  }  }debug0 -> !debug0.isTypeLimited(), (Component)new TranslatableComponent("argument.entity.options.type.description"));
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
/* 302 */     register("tag", debug0 -> { boolean debug1 = debug0.shouldInvertValue(); String debug2 = debug0.getReader().readUnquotedString(); debug0.addPredicate(()); }debug0 -> true, (Component)new TranslatableComponent("argument.entity.options.tag.description"));
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
/*     */ 
/*     */     
/* 315 */     register("nbt", debug0 -> { boolean debug1 = debug0.shouldInvertValue(); CompoundTag debug2 = (new TagParser(debug0.getReader())).readStruct(); debug0.addPredicate(()); }debug0 -> true, (Component)new TranslatableComponent("argument.entity.options.nbt.description"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     register("scores", debug0 -> { StringReader debug1 = debug0.getReader(); Map<String, MinMaxBounds.Ints> debug2 = Maps.newHashMap(); debug1.expect('{'); debug1.skipWhitespace(); while (debug1.canRead() && debug1.peek() != '}') { debug1.skipWhitespace(); String debug3 = debug1.readUnquotedString(); debug1.skipWhitespace(); debug1.expect('='); debug1.skipWhitespace(); MinMaxBounds.Ints debug4 = MinMaxBounds.Ints.fromReader(debug1); debug2.put(debug3, debug4); debug1.skipWhitespace(); if (debug1.canRead() && debug1.peek() == ',') debug1.skip();  }  debug1.expect('}'); if (!debug2.isEmpty()) debug0.addPredicate(());  debug0.setHasScores(true); }debug0 -> !debug0.hasScores(), (Component)new TranslatableComponent("argument.entity.options.scores.description"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     register("advancements", debug0 -> { StringReader debug1 = debug0.getReader(); Map<ResourceLocation, Predicate<AdvancementProgress>> debug2 = Maps.newHashMap(); debug1.expect('{'); debug1.skipWhitespace(); while (debug1.canRead() && debug1.peek() != '}') { debug1.skipWhitespace(); ResourceLocation debug3 = ResourceLocation.read(debug1); debug1.skipWhitespace(); debug1.expect('='); debug1.skipWhitespace(); if (debug1.canRead() && debug1.peek() == '{') { Map<String, Predicate<CriterionProgress>> debug4 = Maps.newHashMap(); debug1.skipWhitespace(); debug1.expect('{'); debug1.skipWhitespace(); while (debug1.canRead() && debug1.peek() != '}') { debug1.skipWhitespace(); String debug5 = debug1.readUnquotedString(); debug1.skipWhitespace(); debug1.expect('='); debug1.skipWhitespace(); boolean debug6 = debug1.readBoolean(); debug4.put(debug5, ()); debug1.skipWhitespace(); if (debug1.canRead() && debug1.peek() == ',') debug1.skip();  }  debug1.skipWhitespace(); debug1.expect('}'); debug1.skipWhitespace(); debug2.put(debug3, ()); } else { boolean debug4 = debug1.readBoolean(); debug2.put(debug3, ()); }  debug1.skipWhitespace(); if (debug1.canRead() && debug1.peek() == ',') debug1.skip();  }  debug1.expect('}'); if (!debug2.isEmpty()) { debug0.addPredicate(()); debug0.setIncludesEntities(false); }  debug0.setHasAdvancements(true); }debug0 -> !debug0.hasAdvancements(), (Component)new TranslatableComponent("argument.entity.options.advancements.description"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 457 */     register("predicate", debug0 -> { boolean debug1 = debug0.shouldInvertValue(); ResourceLocation debug2 = ResourceLocation.read(debug0.getReader()); debug0.addPredicate(()); }debug0 -> true, (Component)new TranslatableComponent("argument.entity.options.predicate.description"));
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
/*     */   public static Modifier get(EntitySelectorParser debug0, String debug1, int debug2) throws CommandSyntaxException {
/* 480 */     Option debug3 = OPTIONS.get(debug1);
/* 481 */     if (debug3 != null) {
/* 482 */       if (debug3.predicate.test(debug0)) {
/* 483 */         return debug3.modifier;
/*     */       }
/* 485 */       throw ERROR_INAPPLICABLE_OPTION.createWithContext(debug0.getReader(), debug1);
/*     */     } 
/*     */     
/* 488 */     debug0.getReader().setCursor(debug2);
/* 489 */     throw ERROR_UNKNOWN_OPTION.createWithContext(debug0.getReader(), debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void suggestNames(EntitySelectorParser debug0, SuggestionsBuilder debug1) {
/* 494 */     String debug2 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 495 */     for (Map.Entry<String, Option> debug4 : OPTIONS.entrySet()) {
/* 496 */       if (((Option)debug4.getValue()).predicate.test(debug0) && ((String)debug4.getKey()).toLowerCase(Locale.ROOT).startsWith(debug2)) {
/* 497 */         debug1.suggest((String)debug4.getKey() + '=', (Message)((Option)debug4.getValue()).description);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class Option
/*     */   {
/*     */     public final EntitySelectorOptions.Modifier modifier;
/*     */     
/*     */     public final Predicate<EntitySelectorParser> predicate;
/*     */     
/*     */     public final Component description;
/*     */     
/*     */     private Option(EntitySelectorOptions.Modifier debug1, Predicate<EntitySelectorParser> debug2, Component debug3) {
/* 512 */       this.modifier = debug1;
/* 513 */       this.predicate = debug2;
/* 514 */       this.description = debug3;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Modifier {
/*     */     void handle(EntitySelectorParser param1EntitySelectorParser) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\selector\options\EntitySelectorOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */