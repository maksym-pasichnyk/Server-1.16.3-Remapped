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
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ 
/*    */ public class MobEffectArgument implements ArgumentType<MobEffect> {
/*    */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_EFFECT;
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "spooky", "effect" });
/*    */   static {
/* 24 */     ERROR_UNKNOWN_EFFECT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("effect.effectNotFound", new Object[] { debug0 }));
/*    */   }
/*    */   public static MobEffectArgument effect() {
/* 27 */     return new MobEffectArgument();
/*    */   }
/*    */   
/*    */   public static MobEffect getEffect(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 31 */     return (MobEffect)debug0.getArgument(debug1, MobEffect.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public MobEffect parse(StringReader debug1) throws CommandSyntaxException {
/* 36 */     ResourceLocation debug2 = ResourceLocation.read(debug1);
/* 37 */     return (MobEffect)Registry.MOB_EFFECT.getOptional(debug2).orElseThrow(() -> ERROR_UNKNOWN_EFFECT.create(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 42 */     return SharedSuggestionProvider.suggestResource(Registry.MOB_EFFECT.keySet(), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 47 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\MobEffectArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */