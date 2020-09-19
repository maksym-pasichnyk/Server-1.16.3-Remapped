/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ItemEnchantmentArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ 
/*    */ public class EnchantCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY;
/*    */   private static final DynamicCommandExceptionType ERROR_NO_ITEM;
/*    */   
/*    */   static {
/* 29 */     ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.enchant.failed.entity", new Object[] { debug0 }));
/* 30 */     ERROR_NO_ITEM = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.enchant.failed.itemless", new Object[] { debug0 }));
/* 31 */     ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.enchant.failed.incompatible", new Object[] { debug0 }));
/* 32 */     ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.enchant.failed.level", new Object[] { debug0, debug1 }));
/* 33 */   } private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE; private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH; private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.enchant.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 36 */     debug0.register(
/* 37 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("enchant")
/* 38 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 39 */         .then(
/* 40 */           Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 41 */           .then((
/* 42 */             (RequiredArgumentBuilder)Commands.argument("enchantment", (ArgumentType)ItemEnchantmentArgument.enchantment())
/* 43 */             .executes(debug0 -> enchant((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ItemEnchantmentArgument.getEnchantment(debug0, "enchantment"), 1)))
/* 44 */             .then(
/* 45 */               Commands.argument("level", (ArgumentType)IntegerArgumentType.integer(0))
/* 46 */               .executes(debug0 -> enchant((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ItemEnchantmentArgument.getEnchantment(debug0, "enchantment"), IntegerArgumentType.getInteger(debug0, "level")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int enchant(CommandSourceStack debug0, Collection<? extends Entity> debug1, Enchantment debug2, int debug3) throws CommandSyntaxException {
/* 54 */     if (debug3 > debug2.getMaxLevel()) {
/* 55 */       throw ERROR_LEVEL_TOO_HIGH.create(Integer.valueOf(debug3), Integer.valueOf(debug2.getMaxLevel()));
/*    */     }
/*    */     
/* 58 */     int debug4 = 0;
/*    */     
/* 60 */     for (Entity debug6 : debug1) {
/* 61 */       if (debug6 instanceof LivingEntity) {
/* 62 */         LivingEntity debug7 = (LivingEntity)debug6;
/*    */         
/* 64 */         ItemStack debug8 = debug7.getMainHandItem();
/* 65 */         if (!debug8.isEmpty()) {
/* 66 */           if (debug2.canEnchant(debug8) && EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantments(debug8).keySet(), debug2)) {
/* 67 */             debug8.enchant(debug2, debug3);
/* 68 */             debug4++; continue;
/* 69 */           }  if (debug1.size() == 1)
/* 70 */             throw ERROR_INCOMPATIBLE.create(debug8.getItem().getName(debug8).getString());  continue;
/*    */         } 
/* 72 */         if (debug1.size() == 1)
/* 73 */           throw ERROR_NO_ITEM.create(debug7.getName().getString());  continue;
/*    */       } 
/* 75 */       if (debug1.size() == 1) {
/* 76 */         throw ERROR_NOT_LIVING_ENTITY.create(debug6.getName().getString());
/*    */       }
/*    */     } 
/*    */     
/* 80 */     if (debug4 == 0)
/* 81 */       throw ERROR_NOTHING_HAPPENED.create(); 
/* 82 */     if (debug1.size() == 1) {
/* 83 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.enchant.success.single", new Object[] { debug2.getFullname(debug3), ((Entity)debug1.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 85 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.enchant.success.multiple", new Object[] { debug2.getFullname(debug3), Integer.valueOf(debug1.size()) }), true);
/*    */     } 
/*    */     
/* 88 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\EnchantCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */