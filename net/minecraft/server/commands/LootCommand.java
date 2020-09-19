/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.LootTables;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class LootCommand
/*     */ {
/*     */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_LOOT_TABLE;
/*     */   private static final DynamicCommandExceptionType ERROR_NO_HELD_ITEMS;
/*     */   private static final DynamicCommandExceptionType ERROR_NO_LOOT_TABLE;
/*     */   
/*     */   static {
/*  61 */     SUGGEST_LOOT_TABLE = ((debug0, debug1) -> {
/*     */         LootTables debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getLootTables();
/*     */         
/*     */         return SharedSuggestionProvider.suggestResource(debug2.getIds(), debug1);
/*     */       });
/*  66 */     ERROR_NO_HELD_ITEMS = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.drop.no_held_items", new Object[] { debug0 }));
/*  67 */     ERROR_NO_LOOT_TABLE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.drop.no_loot_table", new Object[] { debug0 }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  70 */     debug0.register(
/*  71 */         (LiteralArgumentBuilder)addTargets(
/*  72 */           Commands.literal("loot")
/*  73 */           .requires(debug0 -> debug0.hasPermission(2)), (debug0, debug1) -> debug0.then(Commands.literal("fish").then(Commands.argument("loot_table", (ArgumentType)ResourceLocationArgument.id()).suggests(SUGGEST_LOOT_TABLE).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()).executes(())).then(Commands.argument("tool", (ArgumentType)ItemArgument.item()).executes(()))).then(Commands.literal("mainhand").executes(()))).then(Commands.literal("offhand").executes(()))))).then(Commands.literal("loot").then(Commands.argument("loot_table", (ArgumentType)ResourceLocationArgument.id()).suggests(SUGGEST_LOOT_TABLE).executes(()))).then(Commands.literal("kill").then(Commands.argument("target", (ArgumentType)EntityArgument.entity()).executes(()))).then(Commands.literal("mine").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()).executes(())).then(Commands.argument("tool", (ArgumentType)ItemArgument.item()).executes(()))).then(Commands.literal("mainhand").executes(()))).then(Commands.literal("offhand").executes(()))))));
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
/*     */   private static <T extends ArgumentBuilder<CommandSourceStack, T>> T addTargets(T debug0, TailProvider debug1) {
/* 151 */     return (T)debug0
/* 152 */       .then((
/* 153 */         (LiteralArgumentBuilder)Commands.literal("replace")
/* 154 */         .then(Commands.literal("entity")
/* 155 */           .then(
/* 156 */             Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/* 157 */             .then(debug1
/* 158 */               .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("slot", (ArgumentType)SlotArgument.slot()), (debug0, debug1, debug2) -> entityReplace(EntityArgument.getEntities(debug0, "entities"), SlotArgument.getSlot(debug0, "slot"), debug1.size(), debug1, debug2))
/*     */ 
/*     */               
/* 161 */               .then(debug1
/* 162 */                 .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0)), (debug0, debug1, debug2) -> entityReplace(EntityArgument.getEntities(debug0, "entities"), SlotArgument.getSlot(debug0, "slot"), IntegerArgumentType.getInteger(debug0, "count"), debug1, debug2)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 169 */         .then(Commands.literal("block")
/* 170 */           .then(
/* 171 */             Commands.argument("targetPos", (ArgumentType)BlockPosArgument.blockPos())
/* 172 */             .then(debug1
/* 173 */               .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("slot", (ArgumentType)SlotArgument.slot()), (debug0, debug1, debug2) -> blockReplace((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "targetPos"), SlotArgument.getSlot(debug0, "slot"), debug1.size(), debug1, debug2))
/*     */ 
/*     */               
/* 176 */               .then(debug1
/* 177 */                 .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0)), (debug0, debug1, debug2) -> blockReplace((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "targetPos"), IntegerArgumentType.getInteger(debug0, "slot"), IntegerArgumentType.getInteger(debug0, "count"), debug1, debug2)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 185 */       .then(
/* 186 */         Commands.literal("insert")
/* 187 */         .then(debug1
/* 188 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targetPos", (ArgumentType)BlockPosArgument.blockPos()), (debug0, debug1, debug2) -> blockDistribute((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "targetPos"), debug1, debug2))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 193 */       .then(
/* 194 */         Commands.literal("give")
/* 195 */         .then(debug1
/* 196 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("players", (ArgumentType)EntityArgument.players()), (debug0, debug1, debug2) -> playerGive(EntityArgument.getPlayers(debug0, "players"), debug1, debug2))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       .then(
/* 202 */         Commands.literal("spawn")
/* 203 */         .then(debug1
/* 204 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targetPos", (ArgumentType)Vec3Argument.vec3()), (debug0, debug1, debug2) -> dropInWorld((CommandSourceStack)debug0.getSource(), Vec3Argument.getVec3(debug0, "targetPos"), debug1, debug2))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Container getContainer(CommandSourceStack debug0, BlockPos debug1) throws CommandSyntaxException {
/* 212 */     BlockEntity debug2 = debug0.getLevel().getBlockEntity(debug1);
/* 213 */     if (!(debug2 instanceof Container)) {
/* 214 */       throw ReplaceItemCommand.ERROR_NOT_A_CONTAINER.create();
/*     */     }
/*     */     
/* 217 */     return (Container)debug2;
/*     */   }
/*     */   
/*     */   private static int blockDistribute(CommandSourceStack debug0, BlockPos debug1, List<ItemStack> debug2, Callback debug3) throws CommandSyntaxException {
/* 221 */     Container debug4 = getContainer(debug0, debug1);
/*     */     
/* 223 */     List<ItemStack> debug5 = Lists.newArrayListWithCapacity(debug2.size());
/* 224 */     for (ItemStack debug7 : debug2) {
/* 225 */       if (distributeToContainer(debug4, debug7.copy())) {
/* 226 */         debug4.setChanged();
/* 227 */         debug5.add(debug7);
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     debug3.accept(debug5);
/* 232 */     return debug5.size();
/*     */   }
/*     */   
/*     */   private static boolean distributeToContainer(Container debug0, ItemStack debug1) {
/* 236 */     boolean debug2 = false;
/*     */     
/* 238 */     for (int debug3 = 0; debug3 < debug0.getContainerSize() && !debug1.isEmpty(); debug3++) {
/* 239 */       ItemStack debug4 = debug0.getItem(debug3);
/*     */       
/* 241 */       if (debug0.canPlaceItem(debug3, debug1)) {
/* 242 */         if (debug4.isEmpty()) {
/* 243 */           debug0.setItem(debug3, debug1);
/* 244 */           debug2 = true; break;
/*     */         } 
/* 246 */         if (canMergeItems(debug4, debug1)) {
/* 247 */           int debug5 = debug1.getMaxStackSize() - debug4.getCount();
/* 248 */           int debug6 = Math.min(debug1.getCount(), debug5);
/*     */           
/* 250 */           debug1.shrink(debug6);
/* 251 */           debug4.grow(debug6);
/* 252 */           debug2 = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     return debug2;
/*     */   }
/*     */   
/*     */   private static int blockReplace(CommandSourceStack debug0, BlockPos debug1, int debug2, int debug3, List<ItemStack> debug4, Callback debug5) throws CommandSyntaxException {
/* 261 */     Container debug6 = getContainer(debug0, debug1);
/*     */     
/* 263 */     int debug7 = debug6.getContainerSize();
/* 264 */     if (debug2 < 0 || debug2 >= debug7) {
/* 265 */       throw ReplaceItemCommand.ERROR_INAPPLICABLE_SLOT.create(Integer.valueOf(debug2));
/*     */     }
/*     */     
/* 268 */     List<ItemStack> debug8 = Lists.newArrayListWithCapacity(debug4.size());
/*     */     
/* 270 */     for (int debug9 = 0; debug9 < debug3; debug9++) {
/* 271 */       int debug10 = debug2 + debug9;
/* 272 */       ItemStack debug11 = (debug9 < debug4.size()) ? debug4.get(debug9) : ItemStack.EMPTY;
/*     */       
/* 274 */       if (debug6.canPlaceItem(debug10, debug11)) {
/* 275 */         debug6.setItem(debug10, debug11);
/* 276 */         debug8.add(debug11);
/*     */       } 
/*     */     } 
/*     */     
/* 280 */     debug5.accept(debug8);
/* 281 */     return debug8.size();
/*     */   }
/*     */   
/*     */   private static boolean canMergeItems(ItemStack debug0, ItemStack debug1) {
/* 285 */     return (debug0.getItem() == debug1.getItem() && debug0
/* 286 */       .getDamageValue() == debug1.getDamageValue() && debug0
/* 287 */       .getCount() <= debug0.getMaxStackSize() && 
/* 288 */       Objects.equals(debug0.getTag(), debug1.getTag()));
/*     */   }
/*     */   
/*     */   private static int playerGive(Collection<ServerPlayer> debug0, List<ItemStack> debug1, Callback debug2) throws CommandSyntaxException {
/* 292 */     List<ItemStack> debug3 = Lists.newArrayListWithCapacity(debug1.size());
/* 293 */     for (ItemStack debug5 : debug1) {
/* 294 */       for (ServerPlayer debug7 : debug0) {
/* 295 */         if (debug7.inventory.add(debug5.copy())) {
/* 296 */           debug3.add(debug5);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 301 */     debug2.accept(debug3);
/* 302 */     return debug3.size();
/*     */   }
/*     */   
/*     */   private static void setSlots(Entity debug0, List<ItemStack> debug1, int debug2, int debug3, List<ItemStack> debug4) {
/* 306 */     for (int debug5 = 0; debug5 < debug3; debug5++) {
/* 307 */       ItemStack debug6 = (debug5 < debug1.size()) ? debug1.get(debug5) : ItemStack.EMPTY;
/* 308 */       if (debug0.setSlot(debug2 + debug5, debug6.copy())) {
/* 309 */         debug4.add(debug6);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int entityReplace(Collection<? extends Entity> debug0, int debug1, int debug2, List<ItemStack> debug3, Callback debug4) throws CommandSyntaxException {
/* 315 */     List<ItemStack> debug5 = Lists.newArrayListWithCapacity(debug3.size());
/*     */     
/* 317 */     for (Entity debug7 : debug0) {
/* 318 */       if (debug7 instanceof ServerPlayer) {
/* 319 */         ServerPlayer debug8 = (ServerPlayer)debug7;
/* 320 */         debug8.inventoryMenu.broadcastChanges();
/* 321 */         setSlots(debug7, debug3, debug1, debug2, debug5);
/* 322 */         debug8.inventoryMenu.broadcastChanges(); continue;
/*     */       } 
/* 324 */       setSlots(debug7, debug3, debug1, debug2, debug5);
/*     */     } 
/*     */ 
/*     */     
/* 328 */     debug4.accept(debug5);
/* 329 */     return debug5.size();
/*     */   }
/*     */   
/*     */   private static int dropInWorld(CommandSourceStack debug0, Vec3 debug1, List<ItemStack> debug2, Callback debug3) throws CommandSyntaxException {
/* 333 */     ServerLevel debug4 = debug0.getLevel();
/* 334 */     debug2.forEach(debug2 -> {
/*     */           ItemEntity debug3 = new ItemEntity((Level)debug0, debug1.x, debug1.y, debug1.z, debug2.copy());
/*     */           
/*     */           debug3.setDefaultPickUpDelay();
/*     */           debug0.addFreshEntity((Entity)debug3);
/*     */         });
/* 340 */     debug3.accept(debug2);
/* 341 */     return debug2.size();
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack debug0, List<ItemStack> debug1) {
/* 345 */     if (debug1.size() == 1) {
/* 346 */       ItemStack debug2 = debug1.get(0);
/* 347 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.drop.success.single", new Object[] { Integer.valueOf(debug2.getCount()), debug2.getDisplayName() }), false);
/*     */     } else {
/* 349 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.drop.success.multiple", new Object[] { Integer.valueOf(debug1.size()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack debug0, List<ItemStack> debug1, ResourceLocation debug2) {
/* 354 */     if (debug1.size() == 1) {
/* 355 */       ItemStack debug3 = debug1.get(0);
/* 356 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.drop.success.single_with_table", new Object[] { Integer.valueOf(debug3.getCount()), debug3.getDisplayName(), debug2 }), false);
/*     */     } else {
/* 358 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.drop.success.multiple_with_table", new Object[] { Integer.valueOf(debug1.size()), debug2 }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ItemStack getSourceHandItem(CommandSourceStack debug0, EquipmentSlot debug1) throws CommandSyntaxException {
/* 363 */     Entity debug2 = debug0.getEntityOrException();
/* 364 */     if (debug2 instanceof LivingEntity) {
/* 365 */       return ((LivingEntity)debug2).getItemBySlot(debug1);
/*     */     }
/* 367 */     throw ERROR_NO_HELD_ITEMS.create(debug2.getDisplayName());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int dropBlockLoot(CommandContext<CommandSourceStack> debug0, BlockPos debug1, ItemStack debug2, DropConsumer debug3) throws CommandSyntaxException {
/* 372 */     CommandSourceStack debug4 = (CommandSourceStack)debug0.getSource();
/* 373 */     ServerLevel debug5 = debug4.getLevel();
/* 374 */     BlockState debug6 = debug5.getBlockState(debug1);
/* 375 */     BlockEntity debug7 = debug5.getBlockEntity(debug1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     LootContext.Builder debug8 = (new LootContext.Builder(debug5)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)debug1)).withParameter(LootContextParams.BLOCK_STATE, debug6).withOptionalParameter(LootContextParams.BLOCK_ENTITY, debug7).withOptionalParameter(LootContextParams.THIS_ENTITY, debug4.getEntity()).withParameter(LootContextParams.TOOL, debug2);
/*     */     
/* 384 */     List<ItemStack> debug9 = debug6.getDrops(debug8);
/* 385 */     return debug3.accept(debug0, debug9, debug2 -> callback(debug0, debug2, debug1.getBlock().getLootTable()));
/*     */   }
/*     */   
/*     */   private static int dropKillLoot(CommandContext<CommandSourceStack> debug0, Entity debug1, DropConsumer debug2) throws CommandSyntaxException {
/* 389 */     if (!(debug1 instanceof LivingEntity)) {
/* 390 */       throw ERROR_NO_LOOT_TABLE.create(debug1.getDisplayName());
/*     */     }
/*     */     
/* 393 */     ResourceLocation debug3 = ((LivingEntity)debug1).getLootTable();
/* 394 */     CommandSourceStack debug4 = (CommandSourceStack)debug0.getSource();
/*     */     
/* 396 */     LootContext.Builder debug5 = new LootContext.Builder(debug4.getLevel());
/* 397 */     Entity debug6 = debug4.getEntity();
/* 398 */     if (debug6 instanceof net.minecraft.world.entity.player.Player) {
/* 399 */       debug5.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, debug6);
/*     */     }
/* 401 */     debug5.withParameter(LootContextParams.DAMAGE_SOURCE, DamageSource.MAGIC);
/* 402 */     debug5.withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, debug6);
/* 403 */     debug5.withOptionalParameter(LootContextParams.KILLER_ENTITY, debug6);
/* 404 */     debug5.withParameter(LootContextParams.THIS_ENTITY, debug1);
/* 405 */     debug5.withParameter(LootContextParams.ORIGIN, debug4.getPosition());
/*     */     
/* 407 */     LootTable debug7 = debug4.getServer().getLootTables().get(debug3);
/* 408 */     List<ItemStack> debug8 = debug7.getRandomItems(debug5.create(LootContextParamSets.ENTITY));
/* 409 */     return debug2.accept(debug0, debug8, debug2 -> callback(debug0, debug2, debug1));
/*     */   }
/*     */   
/*     */   private static int dropChestLoot(CommandContext<CommandSourceStack> debug0, ResourceLocation debug1, DropConsumer debug2) throws CommandSyntaxException {
/* 413 */     CommandSourceStack debug3 = (CommandSourceStack)debug0.getSource();
/*     */ 
/*     */ 
/*     */     
/* 417 */     LootContext.Builder debug4 = (new LootContext.Builder(debug3.getLevel())).withOptionalParameter(LootContextParams.THIS_ENTITY, debug3.getEntity()).withParameter(LootContextParams.ORIGIN, debug3.getPosition());
/*     */     
/* 419 */     return drop(debug0, debug1, debug4.create(LootContextParamSets.CHEST), debug2);
/*     */   }
/*     */   
/*     */   private static int dropFishingLoot(CommandContext<CommandSourceStack> debug0, ResourceLocation debug1, BlockPos debug2, ItemStack debug3, DropConsumer debug4) throws CommandSyntaxException {
/* 423 */     CommandSourceStack debug5 = (CommandSourceStack)debug0.getSource();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 429 */     LootContext debug6 = (new LootContext.Builder(debug5.getLevel())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)debug2)).withParameter(LootContextParams.TOOL, debug3).withOptionalParameter(LootContextParams.THIS_ENTITY, debug5.getEntity()).create(LootContextParamSets.FISHING);
/*     */     
/* 431 */     return drop(debug0, debug1, debug6, debug4);
/*     */   }
/*     */   
/*     */   private static int drop(CommandContext<CommandSourceStack> debug0, ResourceLocation debug1, LootContext debug2, DropConsumer debug3) throws CommandSyntaxException {
/* 435 */     CommandSourceStack debug4 = (CommandSourceStack)debug0.getSource();
/* 436 */     LootTable debug5 = debug4.getServer().getLootTables().get(debug1);
/* 437 */     List<ItemStack> debug6 = debug5.getRandomItems(debug2);
/* 438 */     return debug3.accept(debug0, debug6, debug1 -> callback(debug0, debug1));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface TailProvider {
/*     */     ArgumentBuilder<CommandSourceStack, ?> construct(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, LootCommand.DropConsumer param1DropConsumer);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface DropConsumer {
/*     */     int accept(CommandContext<CommandSourceStack> param1CommandContext, List<ItemStack> param1List, LootCommand.Callback param1Callback) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface Callback {
/*     */     void accept(List<ItemStack> param1List) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\LootCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */