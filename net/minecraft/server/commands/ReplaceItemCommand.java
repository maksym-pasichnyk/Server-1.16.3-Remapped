/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ 
/*     */ public class ReplaceItemCommand
/*     */ {
/*     */   public static final DynamicCommandExceptionType ERROR_INAPPLICABLE_SLOT;
/*     */   public static final Dynamic2CommandExceptionType ERROR_ENTITY_SLOT;
/*  36 */   public static final SimpleCommandExceptionType ERROR_NOT_A_CONTAINER = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.replaceitem.block.failed")); static {
/*  37 */     ERROR_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.replaceitem.slot.inapplicable", new Object[] { debug0 }));
/*  38 */     ERROR_ENTITY_SLOT = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.replaceitem.entity.failed", new Object[] { debug0, debug1 }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  41 */     debug0.register(
/*  42 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("replaceitem")
/*  43 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  44 */         .then(
/*  45 */           Commands.literal("block")
/*  46 */           .then(
/*  47 */             Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  48 */             .then(
/*  49 */               Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/*  50 */               .then((
/*  51 */                 (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item())
/*  52 */                 .executes(debug0 -> setBlockItem((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), SlotArgument.getSlot(debug0, "slot"), ItemArgument.getItem(debug0, "item").createItemStack(1, false))))
/*  53 */                 .then(
/*  54 */                   Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 64))
/*  55 */                   .executes(debug0 -> setBlockItem((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), SlotArgument.getSlot(debug0, "slot"), ItemArgument.getItem(debug0, "item").createItemStack(IntegerArgumentType.getInteger(debug0, "count"), true)))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         .then(
/*  62 */           Commands.literal("entity")
/*  63 */           .then(
/*  64 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  65 */             .then(
/*  66 */               Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/*  67 */               .then((
/*  68 */                 (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item())
/*  69 */                 .executes(debug0 -> setEntityItem((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), SlotArgument.getSlot(debug0, "slot"), ItemArgument.getItem(debug0, "item").createItemStack(1, false))))
/*  70 */                 .then(
/*  71 */                   Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 64))
/*  72 */                   .executes(debug0 -> setEntityItem((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), SlotArgument.getSlot(debug0, "slot"), ItemArgument.getItem(debug0, "item").createItemStack(IntegerArgumentType.getInteger(debug0, "count"), true)))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int setBlockItem(CommandSourceStack debug0, BlockPos debug1, int debug2, ItemStack debug3) throws CommandSyntaxException {
/*  82 */     BlockEntity debug4 = debug0.getLevel().getBlockEntity(debug1);
/*  83 */     if (!(debug4 instanceof Container)) {
/*  84 */       throw ERROR_NOT_A_CONTAINER.create();
/*     */     }
/*  86 */     Container debug5 = (Container)debug4;
/*  87 */     if (debug2 < 0 || debug2 >= debug5.getContainerSize()) {
/*  88 */       throw ERROR_INAPPLICABLE_SLOT.create(Integer.valueOf(debug2));
/*     */     }
/*     */     
/*  91 */     debug5.setItem(debug2, debug3);
/*  92 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.replaceitem.block.success", new Object[] { Integer.valueOf(debug1.getX()), Integer.valueOf(debug1.getY()), Integer.valueOf(debug1.getZ()), debug3.getDisplayName() }), true);
/*  93 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setEntityItem(CommandSourceStack debug0, Collection<? extends Entity> debug1, int debug2, ItemStack debug3) throws CommandSyntaxException {
/*  97 */     List<Entity> debug4 = Lists.newArrayListWithCapacity(debug1.size());
/*     */     
/*  99 */     for (Entity debug6 : debug1) {
/* 100 */       if (debug6 instanceof ServerPlayer) {
/* 101 */         ((ServerPlayer)debug6).inventoryMenu.broadcastChanges();
/*     */       }
/* 103 */       if (debug6.setSlot(debug2, debug3.copy())) {
/* 104 */         debug4.add(debug6);
/* 105 */         if (debug6 instanceof ServerPlayer) {
/* 106 */           ((ServerPlayer)debug6).inventoryMenu.broadcastChanges();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (debug4.isEmpty()) {
/* 112 */       throw ERROR_ENTITY_SLOT.create(debug3.getDisplayName(), Integer.valueOf(debug2));
/*     */     }
/*     */     
/* 115 */     if (debug4.size() == 1) {
/* 116 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.replaceitem.entity.success.single", new Object[] { ((Entity)debug4.iterator().next()).getDisplayName(), debug3.getDisplayName() }), true);
/*     */     } else {
/* 118 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.replaceitem.entity.success.multiple", new Object[] { Integer.valueOf(debug4.size()), debug3.getDisplayName() }), true);
/*     */     } 
/*     */     
/* 121 */     return debug4.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ReplaceItemCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */