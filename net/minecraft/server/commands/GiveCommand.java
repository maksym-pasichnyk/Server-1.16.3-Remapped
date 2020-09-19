/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemInput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class GiveCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 28 */     debug0.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("give")
/* 30 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 31 */         .then(
/* 32 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 33 */           .then((
/* 34 */             (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item())
/* 35 */             .executes(debug0 -> giveItem((CommandSourceStack)debug0.getSource(), ItemArgument.getItem(debug0, "item"), EntityArgument.getPlayers(debug0, "targets"), 1)))
/* 36 */             .then(
/* 37 */               Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1))
/* 38 */               .executes(debug0 -> giveItem((CommandSourceStack)debug0.getSource(), ItemArgument.getItem(debug0, "item"), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "count")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int giveItem(CommandSourceStack debug0, ItemInput debug1, Collection<ServerPlayer> debug2, int debug3) throws CommandSyntaxException {
/* 46 */     for (ServerPlayer debug5 : debug2) {
/* 47 */       int debug6 = debug3;
/* 48 */       while (debug6 > 0) {
/* 49 */         int debug7 = Math.min(debug1.getItem().getMaxStackSize(), debug6);
/* 50 */         debug6 -= debug7;
/*    */         
/* 52 */         ItemStack debug8 = debug1.createItemStack(debug7, false);
/* 53 */         boolean debug9 = debug5.inventory.add(debug8);
/*    */         
/* 55 */         if (!debug9 || !debug8.isEmpty()) {
/* 56 */           ItemEntity itemEntity = debug5.drop(debug8, false);
/* 57 */           if (itemEntity != null) {
/* 58 */             itemEntity.setNoPickUpDelay();
/* 59 */             itemEntity.setOwner(debug5.getUUID());
/*    */           } 
/*    */           continue;
/*    */         } 
/* 63 */         debug8.setCount(1);
/* 64 */         ItemEntity debug10 = debug5.drop(debug8, false);
/* 65 */         if (debug10 != null) {
/* 66 */           debug10.makeFakeItem();
/*    */         }
/* 68 */         debug5.level.playSound(null, debug5.getX(), debug5.getY(), debug5.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((debug5.getRandom().nextFloat() - debug5.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 69 */         debug5.inventoryMenu.broadcastChanges();
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 74 */     if (debug2.size() == 1) {
/* 75 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.give.success.single", new Object[] { Integer.valueOf(debug3), debug1.createItemStack(debug3, false).getDisplayName(), ((ServerPlayer)debug2.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 77 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.give.success.single", new Object[] { Integer.valueOf(debug3), debug1.createItemStack(debug3, false).getDisplayName(), Integer.valueOf(debug2.size()) }), true);
/*    */     } 
/*    */     
/* 80 */     return debug2.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\GiveCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */