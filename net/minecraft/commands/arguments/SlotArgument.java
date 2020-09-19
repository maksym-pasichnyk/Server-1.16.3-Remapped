/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ 
/*    */ public class SlotArgument
/*    */   implements ArgumentType<Integer>
/*    */ {
/* 27 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "container.5", "12", "weapon" }); static {
/* 28 */     ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("slot.unknown", new Object[] { debug0 }));
/* 29 */     SLOTS = (Map<String, Integer>)Util.make(Maps.newHashMap(), debug0 -> {
/*    */           int debug1;
/*    */           for (debug1 = 0; debug1 < 54; debug1++) {
/*    */             debug0.put("container." + debug1, Integer.valueOf(debug1));
/*    */           }
/*    */           for (debug1 = 0; debug1 < 9; debug1++)
/*    */             debug0.put("hotbar." + debug1, Integer.valueOf(debug1)); 
/*    */           for (debug1 = 0; debug1 < 27; debug1++)
/*    */             debug0.put("inventory." + debug1, Integer.valueOf(9 + debug1)); 
/*    */           for (debug1 = 0; debug1 < 27; debug1++)
/*    */             debug0.put("enderchest." + debug1, Integer.valueOf(200 + debug1)); 
/*    */           for (debug1 = 0; debug1 < 8; debug1++)
/*    */             debug0.put("villager." + debug1, Integer.valueOf(300 + debug1)); 
/*    */           for (debug1 = 0; debug1 < 15; debug1++)
/*    */             debug0.put("horse." + debug1, Integer.valueOf(500 + debug1)); 
/*    */           debug0.put("weapon", Integer.valueOf(98));
/*    */           debug0.put("weapon.mainhand", Integer.valueOf(98));
/*    */           debug0.put("weapon.offhand", Integer.valueOf(99));
/*    */           debug0.put("armor.head", Integer.valueOf(100 + EquipmentSlot.HEAD.getIndex()));
/*    */           debug0.put("armor.chest", Integer.valueOf(100 + EquipmentSlot.CHEST.getIndex()));
/*    */           debug0.put("armor.legs", Integer.valueOf(100 + EquipmentSlot.LEGS.getIndex()));
/*    */           debug0.put("armor.feet", Integer.valueOf(100 + EquipmentSlot.FEET.getIndex()));
/*    */           debug0.put("horse.saddle", Integer.valueOf(400));
/*    */           debug0.put("horse.armor", Integer.valueOf(401));
/*    */           debug0.put("horse.chest", Integer.valueOf(499));
/*    */         });
/*    */   }
/*    */   
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT;
/*    */   private static final Map<String, Integer> SLOTS;
/*    */   
/*    */   public static SlotArgument slot() {
/* 61 */     return new SlotArgument();
/*    */   }
/*    */   
/*    */   public static int getSlot(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 65 */     return ((Integer)debug0.getArgument(debug1, Integer.class)).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader debug1) throws CommandSyntaxException {
/* 70 */     String debug2 = debug1.readUnquotedString();
/* 71 */     if (!SLOTS.containsKey(debug2)) {
/* 72 */       throw ERROR_UNKNOWN_SLOT.create(debug2);
/*    */     }
/* 74 */     return SLOTS.get(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 79 */     return SharedSuggestionProvider.suggest(SLOTS.keySet(), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 84 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\SlotArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */