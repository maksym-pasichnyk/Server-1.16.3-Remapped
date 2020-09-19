/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GameRules {
/*     */   public enum Category {
/*  31 */     PLAYER("gamerule.category.player"),
/*  32 */     MOBS("gamerule.category.mobs"),
/*  33 */     SPAWNING("gamerule.category.spawning"),
/*  34 */     DROPS("gamerule.category.drops"),
/*  35 */     UPDATES("gamerule.category.updates"),
/*  36 */     CHAT("gamerule.category.chat"),
/*  37 */     MISC("gamerule.category.misc");
/*     */     
/*     */     private final String descriptionId;
/*     */ 
/*     */     
/*     */     Category(String debug3) {
/*  43 */       this.descriptionId = debug3;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final Logger LOGGER = LogManager.getLogger(); private static final Map<Key<?>, Type<?>> GAME_RULE_TYPES;
/*     */   static {
/*  53 */     GAME_RULE_TYPES = Maps.newTreeMap(Comparator.comparing(debug0 -> debug0.id));
/*     */   }
/*     */   private static <T extends Value<T>> Key<T> register(String debug0, Category debug1, Type<T> debug2) {
/*  56 */     Key<T> debug3 = new Key<>(debug0, debug1);
/*  57 */     Type<?> debug4 = GAME_RULE_TYPES.put(debug3, debug2);
/*  58 */     if (debug4 != null) {
/*  59 */       throw new IllegalStateException("Duplicate game rule registration for " + debug0);
/*     */     }
/*  61 */     return debug3;
/*     */   }
/*     */   
/*  64 */   public static final Key<BooleanValue> RULE_DOFIRETICK = register("doFireTick", Category.UPDATES, BooleanValue.create(true));
/*  65 */   public static final Key<BooleanValue> RULE_MOBGRIEFING = register("mobGriefing", Category.MOBS, BooleanValue.create(true));
/*  66 */   public static final Key<BooleanValue> RULE_KEEPINVENTORY = register("keepInventory", Category.PLAYER, BooleanValue.create(false));
/*  67 */   public static final Key<BooleanValue> RULE_DOMOBSPAWNING = register("doMobSpawning", Category.SPAWNING, BooleanValue.create(true));
/*  68 */   public static final Key<BooleanValue> RULE_DOMOBLOOT = register("doMobLoot", Category.DROPS, BooleanValue.create(true));
/*  69 */   public static final Key<BooleanValue> RULE_DOBLOCKDROPS = register("doTileDrops", Category.DROPS, BooleanValue.create(true));
/*  70 */   public static final Key<BooleanValue> RULE_DOENTITYDROPS = register("doEntityDrops", Category.DROPS, BooleanValue.create(true));
/*  71 */   public static final Key<BooleanValue> RULE_COMMANDBLOCKOUTPUT = register("commandBlockOutput", Category.CHAT, BooleanValue.create(true));
/*  72 */   public static final Key<BooleanValue> RULE_NATURAL_REGENERATION = register("naturalRegeneration", Category.PLAYER, BooleanValue.create(true));
/*  73 */   public static final Key<BooleanValue> RULE_DAYLIGHT = register("doDaylightCycle", Category.UPDATES, BooleanValue.create(true));
/*  74 */   public static final Key<BooleanValue> RULE_LOGADMINCOMMANDS = register("logAdminCommands", Category.CHAT, BooleanValue.create(true));
/*  75 */   public static final Key<BooleanValue> RULE_SHOWDEATHMESSAGES = register("showDeathMessages", Category.CHAT, BooleanValue.create(true));
/*  76 */   public static final Key<IntegerValue> RULE_RANDOMTICKING = register("randomTickSpeed", Category.UPDATES, IntegerValue.create(3)); public static final Key<BooleanValue> RULE_REDUCEDDEBUGINFO;
/*  77 */   public static final Key<BooleanValue> RULE_SENDCOMMANDFEEDBACK = register("sendCommandFeedback", Category.CHAT, BooleanValue.create(true)); static {
/*  78 */     RULE_REDUCEDDEBUGINFO = register("reducedDebugInfo", Category.MISC, BooleanValue.create(false, (debug0, debug1) -> {
/*     */             byte debug2 = debug1.get() ? 22 : 23;
/*     */             for (ServerPlayer debug4 : debug0.getPlayerList().getPlayers())
/*     */               debug4.connection.send((Packet)new ClientboundEntityEventPacket((Entity)debug4, debug2)); 
/*     */           }));
/*     */   }
/*  84 */   public static final Key<BooleanValue> RULE_SPECTATORSGENERATECHUNKS = register("spectatorsGenerateChunks", Category.PLAYER, BooleanValue.create(true));
/*  85 */   public static final Key<IntegerValue> RULE_SPAWN_RADIUS = register("spawnRadius", Category.PLAYER, IntegerValue.create(10));
/*  86 */   public static final Key<BooleanValue> RULE_DISABLE_ELYTRA_MOVEMENT_CHECK = register("disableElytraMovementCheck", Category.PLAYER, BooleanValue.create(false));
/*  87 */   public static final Key<IntegerValue> RULE_MAX_ENTITY_CRAMMING = register("maxEntityCramming", Category.MOBS, IntegerValue.create(24));
/*  88 */   public static final Key<BooleanValue> RULE_WEATHER_CYCLE = register("doWeatherCycle", Category.UPDATES, BooleanValue.create(true));
/*  89 */   public static final Key<BooleanValue> RULE_LIMITED_CRAFTING = register("doLimitedCrafting", Category.PLAYER, BooleanValue.create(false));
/*  90 */   public static final Key<IntegerValue> RULE_MAX_COMMAND_CHAIN_LENGTH = register("maxCommandChainLength", Category.MISC, IntegerValue.create(65536));
/*  91 */   public static final Key<BooleanValue> RULE_ANNOUNCE_ADVANCEMENTS = register("announceAdvancements", Category.CHAT, BooleanValue.create(true));
/*  92 */   public static final Key<BooleanValue> RULE_DISABLE_RAIDS = register("disableRaids", Category.MOBS, BooleanValue.create(false));
/*  93 */   public static final Key<BooleanValue> RULE_DOINSOMNIA = register("doInsomnia", Category.SPAWNING, BooleanValue.create(true)); public static final Key<BooleanValue> RULE_DO_IMMEDIATE_RESPAWN; static {
/*  94 */     RULE_DO_IMMEDIATE_RESPAWN = register("doImmediateRespawn", Category.PLAYER, BooleanValue.create(false, (debug0, debug1) -> {
/*     */             for (ServerPlayer debug3 : debug0.getPlayerList().getPlayers())
/*     */               debug3.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.IMMEDIATE_RESPAWN, debug1.get() ? 1.0F : 0.0F)); 
/*     */           }));
/*     */   }
/*  99 */   public static final Key<BooleanValue> RULE_DROWNING_DAMAGE = register("drowningDamage", Category.PLAYER, BooleanValue.create(true));
/* 100 */   public static final Key<BooleanValue> RULE_FALL_DAMAGE = register("fallDamage", Category.PLAYER, BooleanValue.create(true));
/* 101 */   public static final Key<BooleanValue> RULE_FIRE_DAMAGE = register("fireDamage", Category.PLAYER, BooleanValue.create(true));
/* 102 */   public static final Key<BooleanValue> RULE_DO_PATROL_SPAWNING = register("doPatrolSpawning", Category.SPAWNING, BooleanValue.create(true));
/* 103 */   public static final Key<BooleanValue> RULE_DO_TRADER_SPAWNING = register("doTraderSpawning", Category.SPAWNING, BooleanValue.create(true));
/* 104 */   public static final Key<BooleanValue> RULE_FORGIVE_DEAD_PLAYERS = register("forgiveDeadPlayers", Category.MOBS, BooleanValue.create(true));
/* 105 */   public static final Key<BooleanValue> RULE_UNIVERSAL_ANGER = register("universalAnger", Category.MOBS, BooleanValue.create(false));
/*     */   
/*     */   private final Map<Key<?>, Value<?>> rules;
/*     */   
/*     */   public GameRules(DynamicLike<?> debug1) {
/* 110 */     this();
/* 111 */     loadFromTag(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GameRules() {
/* 120 */     this.rules = (Map<Key<?>, Value<?>>)GAME_RULE_TYPES.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, debug0 -> ((Type<Value>)debug0.getValue()).createRule()));
/*     */   }
/*     */   
/*     */   private GameRules(Map<Key<?>, Value<?>> debug1) {
/* 124 */     this.rules = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Value<T>> T getRule(Key<T> debug1) {
/* 129 */     return (T)this.rules.get(debug1);
/*     */   }
/*     */   
/*     */   public CompoundTag createTag() {
/* 133 */     CompoundTag debug1 = new CompoundTag();
/* 134 */     this.rules.forEach((debug1, debug2) -> debug0.putString(debug1.id, debug2.serialize()));
/* 135 */     return debug1;
/*     */   }
/*     */   
/*     */   private void loadFromTag(DynamicLike<?> debug1) {
/* 139 */     this.rules.forEach((debug1, debug2) -> debug0.get(debug1.id).asString().result().ifPresent(debug2::deserialize));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GameRules copy() {
/* 145 */     return new GameRules((Map<Key<?>, Value<?>>)this.rules.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, debug0 -> ((Value<Value>)debug0.getValue()).copy())));
/*     */   }
/*     */ 
/*     */   
/*     */   static interface VisitorCaller<T extends Value<T>>
/*     */   {
/*     */     void call(GameRules.GameRuleTypeVisitor param1GameRuleTypeVisitor, GameRules.Key<T> param1Key, GameRules.Type<T> param1Type);
/*     */   }
/*     */   
/*     */   public static interface GameRuleTypeVisitor
/*     */   {
/*     */     default <T extends GameRules.Value<T>> void visit(GameRules.Key<T> debug1, GameRules.Type<T> debug2) {}
/*     */     
/*     */     default void visitBoolean(GameRules.Key<GameRules.BooleanValue> debug1, GameRules.Type<GameRules.BooleanValue> debug2) {}
/*     */     
/*     */     default void visitInteger(GameRules.Key<GameRules.IntegerValue> debug1, GameRules.Type<GameRules.IntegerValue> debug2) {}
/*     */   }
/*     */   
/*     */   public static void visitGameRuleTypes(GameRuleTypeVisitor debug0) {
/* 164 */     GAME_RULE_TYPES.forEach((debug1, debug2) -> callVisitorCap(debug0, debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Value<T>> void callVisitorCap(GameRuleTypeVisitor debug0, Key<?> debug1, Type<?> debug2) {
/* 169 */     Key<T> debug3 = (Key)debug1;
/* 170 */     Type<T> debug4 = (Type)debug2;
/* 171 */     debug0.visit(debug3, debug4);
/* 172 */     debug4.callVisitor(debug0, debug3);
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
/*     */   public boolean getBoolean(Key<BooleanValue> debug1) {
/* 185 */     return ((BooleanValue)getRule(debug1)).get();
/*     */   }
/*     */   
/*     */   public int getInt(Key<IntegerValue> debug1) {
/* 189 */     return ((IntegerValue)getRule(debug1)).get();
/*     */   }
/*     */   
/*     */   public static final class Key<T extends Value<T>> {
/*     */     private final String id;
/*     */     private final GameRules.Category category;
/*     */     
/*     */     public Key(String debug1, GameRules.Category debug2) {
/* 197 */       this.id = debug1;
/* 198 */       this.category = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 203 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 208 */       if (this == debug1) return true; 
/* 209 */       return (debug1 instanceof Key && ((Key)debug1).id.equals(this.id));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 214 */       return this.id.hashCode();
/*     */     }
/*     */     
/*     */     public String getId() {
/* 218 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getDescriptionId() {
/* 222 */       return "gamerule." + this.id;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Type<T extends Value<T>>
/*     */   {
/*     */     private final Supplier<ArgumentType<?>> argument;
/*     */     
/*     */     private final Function<Type<T>, T> constructor;
/*     */     
/*     */     private final BiConsumer<MinecraftServer, T> callback;
/*     */     private final GameRules.VisitorCaller<T> visitorCaller;
/*     */     
/*     */     private Type(Supplier<ArgumentType<?>> debug1, Function<Type<T>, T> debug2, BiConsumer<MinecraftServer, T> debug3, GameRules.VisitorCaller<T> debug4) {
/* 237 */       this.argument = debug1;
/* 238 */       this.constructor = debug2;
/* 239 */       this.callback = debug3;
/* 240 */       this.visitorCaller = debug4;
/*     */     }
/*     */     
/*     */     public RequiredArgumentBuilder<CommandSourceStack, ?> createArgument(String debug1) {
/* 244 */       return Commands.argument(debug1, this.argument.get());
/*     */     }
/*     */     
/*     */     public T createRule() {
/* 248 */       return this.constructor.apply(this);
/*     */     }
/*     */     
/*     */     public void callVisitor(GameRules.GameRuleTypeVisitor debug1, GameRules.Key<T> debug2) {
/* 252 */       this.visitorCaller.call(debug1, debug2, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Value<T extends Value<T>> {
/*     */     protected final GameRules.Type<T> type;
/*     */     
/*     */     public Value(GameRules.Type<T> debug1) {
/* 260 */       this.type = debug1;
/*     */     }
/*     */     
/*     */     protected abstract void updateFromArgument(CommandContext<CommandSourceStack> param1CommandContext, String param1String);
/*     */     
/*     */     public void setFromArgument(CommandContext<CommandSourceStack> debug1, String debug2) {
/* 266 */       updateFromArgument(debug1, debug2);
/* 267 */       onChanged(((CommandSourceStack)debug1.getSource()).getServer());
/*     */     }
/*     */     
/*     */     protected void onChanged(@Nullable MinecraftServer debug1) {
/* 271 */       if (debug1 != null) {
/* 272 */         this.type.callback.accept(debug1, getSelf());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract void deserialize(String param1String);
/*     */     
/*     */     public abstract String serialize();
/*     */     
/*     */     public String toString() {
/* 282 */       return serialize();
/*     */     }
/*     */     
/*     */     public abstract int getCommandResult();
/*     */     
/*     */     protected abstract T getSelf();
/*     */     
/*     */     protected abstract T copy();
/*     */   }
/*     */   
/*     */   public static class IntegerValue extends Value<IntegerValue> {
/*     */     private int value;
/*     */     
/*     */     private static GameRules.Type<IntegerValue> create(int debug0, BiConsumer<MinecraftServer, IntegerValue> debug1) {
/* 296 */       return new GameRules.Type<>(IntegerArgumentType::integer, debug1 -> new IntegerValue(debug1, debug0), debug1, GameRules.GameRuleTypeVisitor::visitInteger);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static GameRules.Type<IntegerValue> create(int debug0) {
/* 305 */       return create(debug0, (debug0, debug1) -> {
/*     */           
/*     */           });
/*     */     }
/*     */     
/*     */     public IntegerValue(GameRules.Type<IntegerValue> debug1, int debug2) {
/* 311 */       super(debug1);
/* 312 */       this.value = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateFromArgument(CommandContext<CommandSourceStack> debug1, String debug2) {
/* 317 */       this.value = IntegerArgumentType.getInteger(debug1, debug2);
/*     */     }
/*     */     
/*     */     public int get() {
/* 321 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String serialize() {
/* 331 */       return Integer.toString(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void deserialize(String debug1) {
/* 336 */       this.value = safeParse(debug1);
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
/*     */     
/*     */     private static int safeParse(String debug0) {
/* 350 */       if (!debug0.isEmpty()) {
/*     */         try {
/* 352 */           return Integer.parseInt(debug0);
/* 353 */         } catch (NumberFormatException debug1) {
/* 354 */           GameRules.LOGGER.warn("Failed to parse integer {}", debug0);
/*     */         } 
/*     */       }
/* 357 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCommandResult() {
/* 362 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     protected IntegerValue getSelf() {
/* 367 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected IntegerValue copy() {
/* 372 */       return new IntegerValue(this.type, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class BooleanValue
/*     */     extends Value<BooleanValue>
/*     */   {
/*     */     private boolean value;
/*     */ 
/*     */     
/*     */     private static GameRules.Type<BooleanValue> create(boolean debug0, BiConsumer<MinecraftServer, BooleanValue> debug1) {
/* 384 */       return new GameRules.Type<>(BoolArgumentType::bool, debug1 -> new BooleanValue(debug1, debug0), debug1, GameRules.GameRuleTypeVisitor::visitBoolean);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static GameRules.Type<BooleanValue> create(boolean debug0) {
/* 393 */       return create(debug0, (debug0, debug1) -> {
/*     */           
/*     */           });
/*     */     }
/*     */     
/*     */     public BooleanValue(GameRules.Type<BooleanValue> debug1, boolean debug2) {
/* 399 */       super(debug1);
/* 400 */       this.value = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateFromArgument(CommandContext<CommandSourceStack> debug1, String debug2) {
/* 405 */       this.value = BoolArgumentType.getBool(debug1, debug2);
/*     */     }
/*     */     
/*     */     public boolean get() {
/* 409 */       return this.value;
/*     */     }
/*     */     
/*     */     public void set(boolean debug1, @Nullable MinecraftServer debug2) {
/* 413 */       this.value = debug1;
/* 414 */       onChanged(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public String serialize() {
/* 419 */       return Boolean.toString(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void deserialize(String debug1) {
/* 424 */       this.value = Boolean.parseBoolean(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCommandResult() {
/* 429 */       return this.value ? 1 : 0;
/*     */     }
/*     */ 
/*     */     
/*     */     protected BooleanValue getSelf() {
/* 434 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected BooleanValue copy() {
/* 439 */       return new BooleanValue(this.type, this.value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\GameRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */