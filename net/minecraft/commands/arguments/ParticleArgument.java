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
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleType;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class ParticleArgument implements ArgumentType<ParticleOptions> {
/*    */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE;
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "particle with options" }); static {
/* 24 */     ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("particle.notFound", new Object[] { debug0 }));
/*    */   }
/*    */   public static ParticleArgument particle() {
/* 27 */     return new ParticleArgument();
/*    */   }
/*    */   
/*    */   public static ParticleOptions getParticle(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 31 */     return (ParticleOptions)debug0.getArgument(debug1, ParticleOptions.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleOptions parse(StringReader debug1) throws CommandSyntaxException {
/* 36 */     return readParticle(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 41 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static ParticleOptions readParticle(StringReader debug0) throws CommandSyntaxException {
/* 45 */     ResourceLocation debug1 = ResourceLocation.read(debug0);
/* 46 */     ParticleType<?> debug2 = (ParticleType)Registry.PARTICLE_TYPE.getOptional(debug1).orElseThrow(() -> ERROR_UNKNOWN_PARTICLE.create(debug0));
/* 47 */     return (ParticleOptions)readParticle(debug0, debug2);
/*    */   }
/*    */   
/*    */   private static <T extends ParticleOptions> T readParticle(StringReader debug0, ParticleType<T> debug1) throws CommandSyntaxException {
/* 51 */     return (T)debug1.getDeserializer().fromCommand(debug1, debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 56 */     return SharedSuggestionProvider.suggestResource(Registry.PARTICLE_TYPE.keySet(), debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ParticleArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */