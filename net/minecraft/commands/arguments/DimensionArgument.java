/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class DimensionArgument implements ArgumentType<ResourceLocation> {
/*    */   private static final Collection<String> EXAMPLES;
/*    */   
/*    */   static {
/* 25 */     EXAMPLES = (Collection<String>)Stream.<ResourceKey>of(new ResourceKey[] { Level.OVERWORLD, Level.NETHER }).map(debug0 -> debug0.location().toString()).collect(Collectors.toList());
/*    */     
/* 27 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.dimension.invalid", new Object[] { debug0 }));
/*    */   }
/*    */   private static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   public ResourceLocation parse(StringReader debug1) throws CommandSyntaxException {
/* 31 */     return ResourceLocation.read(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 36 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 37 */       return SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)debug1.getSource()).levels().stream().map(ResourceKey::location), debug2);
/*    */     }
/* 39 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 44 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static DimensionArgument dimension() {
/* 48 */     return new DimensionArgument();
/*    */   }
/*    */   
/*    */   public static ServerLevel getDimension(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 52 */     ResourceLocation debug2 = (ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class);
/* 53 */     ResourceKey<Level> debug3 = ResourceKey.create(Registry.DIMENSION_REGISTRY, debug2);
/* 54 */     ServerLevel debug4 = ((CommandSourceStack)debug0.getSource()).getServer().getLevel(debug3);
/* 55 */     if (debug4 == null) {
/* 56 */       throw ERROR_INVALID_VALUE.create(debug2);
/*    */     }
/* 58 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\DimensionArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */