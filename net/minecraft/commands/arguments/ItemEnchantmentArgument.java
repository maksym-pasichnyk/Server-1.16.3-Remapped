/*    */ package net.minecraft.commands.arguments;
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
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ 
/*    */ public class ItemEnchantmentArgument implements ArgumentType<Enchantment> {
/*    */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENCHANTMENT;
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "unbreaking", "silk_touch" });
/*    */   static {
/* 24 */     ERROR_UNKNOWN_ENCHANTMENT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("enchantment.unknown", new Object[] { debug0 }));
/*    */   }
/*    */   public static ItemEnchantmentArgument enchantment() {
/* 27 */     return new ItemEnchantmentArgument();
/*    */   }
/*    */   
/*    */   public static Enchantment getEnchantment(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 31 */     return (Enchantment)debug0.getArgument(debug1, Enchantment.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Enchantment parse(StringReader debug1) throws CommandSyntaxException {
/* 36 */     ResourceLocation debug2 = ResourceLocation.read(debug1);
/* 37 */     return (Enchantment)Registry.ENCHANTMENT.getOptional(debug2).orElseThrow(() -> ERROR_UNKNOWN_ENCHANTMENT.create(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 42 */     return SharedSuggestionProvider.suggestResource(Registry.ENCHANTMENT.keySet(), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 47 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ItemEnchantmentArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */