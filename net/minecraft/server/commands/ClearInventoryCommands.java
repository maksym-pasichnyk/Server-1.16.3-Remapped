/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClearInventoryCommands {
/*    */   private static final DynamicCommandExceptionType ERROR_SINGLE;
/*    */   
/*    */   static {
/* 26 */     ERROR_SINGLE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("clear.failed.single", new Object[] { debug0 }));
/* 27 */     ERROR_MULTIPLE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("clear.failed.multiple", new Object[] { debug0 }));
/*    */   } private static final DynamicCommandExceptionType ERROR_MULTIPLE;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 30 */     debug0.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clear")
/* 32 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 33 */         .executes(debug0 -> clearInventory((CommandSourceStack)debug0.getSource(), Collections.singleton(((CommandSourceStack)debug0.getSource()).getPlayerOrException()), (), -1)))
/* 34 */         .then((
/* 35 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 36 */           .executes(debug0 -> clearInventory((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), (), -1)))
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemPredicateArgument.itemPredicate())
/* 39 */             .executes(debug0 -> clearInventory((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ItemPredicateArgument.getItemPredicate(debug0, "item"), -1)))
/* 40 */             .then(
/* 41 */               Commands.argument("maxCount", (ArgumentType)IntegerArgumentType.integer(0))
/* 42 */               .executes(debug0 -> clearInventory((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ItemPredicateArgument.getItemPredicate(debug0, "item"), IntegerArgumentType.getInteger(debug0, "maxCount")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int clearInventory(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Predicate<ItemStack> debug2, int debug3) throws CommandSyntaxException {
/* 50 */     int debug4 = 0;
/*    */     
/* 52 */     for (ServerPlayer debug6 : debug1) {
/* 53 */       debug4 += debug6.inventory.clearOrCountMatchingItems(debug2, debug3, (Container)debug6.inventoryMenu.getCraftSlots());
/* 54 */       debug6.containerMenu.broadcastChanges();
/*    */ 
/*    */       
/* 57 */       debug6.inventoryMenu.slotsChanged((Container)debug6.inventory);
/*    */       
/* 59 */       debug6.broadcastCarriedItem();
/*    */     } 
/*    */     
/* 62 */     if (debug4 == 0) {
/* 63 */       if (debug1.size() == 1) {
/* 64 */         throw ERROR_SINGLE.create(((ServerPlayer)debug1.iterator().next()).getName());
/*    */       }
/* 66 */       throw ERROR_MULTIPLE.create(Integer.valueOf(debug1.size()));
/*    */     } 
/*    */ 
/*    */     
/* 70 */     if (debug3 == 0) {
/* 71 */       if (debug1.size() == 1) {
/* 72 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.clear.test.single", new Object[] { Integer.valueOf(debug4), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*    */       } else {
/* 74 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.clear.test.multiple", new Object[] { Integer.valueOf(debug4), Integer.valueOf(debug1.size()) }), true);
/*    */       }
/*    */     
/* 77 */     } else if (debug1.size() == 1) {
/* 78 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.clear.success.single", new Object[] { Integer.valueOf(debug4), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 80 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.clear.success.multiple", new Object[] { Integer.valueOf(debug4), Integer.valueOf(debug1.size()) }), true);
/*    */     } 
/*    */ 
/*    */     
/* 84 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ClearInventoryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */