/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public class SuggestionProviders {
/* 21 */   private static final Map<ResourceLocation, SuggestionProvider<SharedSuggestionProvider>> PROVIDERS_BY_NAME = Maps.newHashMap();
/* 22 */   private static final ResourceLocation DEFAULT_NAME = new ResourceLocation("ask_server"); public static final SuggestionProvider<SharedSuggestionProvider> ASK_SERVER; public static final SuggestionProvider<CommandSourceStack> ALL_RECIPES;
/*    */   static {
/* 24 */     ASK_SERVER = register(DEFAULT_NAME, (debug0, debug1) -> ((SharedSuggestionProvider)debug0.getSource()).customSuggestion(debug0, debug1));
/* 25 */     ALL_RECIPES = register(new ResourceLocation("all_recipes"), (debug0, debug1) -> SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)debug0.getSource()).getRecipeNames(), debug1));
/* 26 */     AVAILABLE_SOUNDS = register(new ResourceLocation("available_sounds"), (debug0, debug1) -> SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)debug0.getSource()).getAvailableSoundEvents(), debug1));
/* 27 */     AVAILABLE_BIOMES = register(new ResourceLocation("available_biomes"), (debug0, debug1) -> SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)debug0.getSource()).registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).keySet(), debug1));
/* 28 */     SUMMONABLE_ENTITIES = register(new ResourceLocation("summonable_entities"), (debug0, debug1) -> SharedSuggestionProvider.suggestResource(Registry.ENTITY_TYPE.stream().filter(EntityType::canSummon), debug1, EntityType::getKey, ()));
/*    */   }
/*    */   public static final SuggestionProvider<CommandSourceStack> AVAILABLE_SOUNDS; public static final SuggestionProvider<CommandSourceStack> AVAILABLE_BIOMES; public static final SuggestionProvider<CommandSourceStack> SUMMONABLE_ENTITIES;
/*    */   public static <S extends SharedSuggestionProvider> SuggestionProvider<S> register(ResourceLocation debug0, SuggestionProvider<SharedSuggestionProvider> debug1) {
/* 32 */     if (PROVIDERS_BY_NAME.containsKey(debug0)) {
/* 33 */       throw new IllegalArgumentException("A command suggestion provider is already registered with the name " + debug0);
/*    */     }
/* 35 */     PROVIDERS_BY_NAME.put(debug0, debug1);
/* 36 */     return new Wrapper(debug0, debug1);
/*    */   }
/*    */   
/*    */   public static SuggestionProvider<SharedSuggestionProvider> getProvider(ResourceLocation debug0) {
/* 40 */     return PROVIDERS_BY_NAME.getOrDefault(debug0, ASK_SERVER);
/*    */   }
/*    */   
/*    */   public static ResourceLocation getName(SuggestionProvider<SharedSuggestionProvider> debug0) {
/* 44 */     if (debug0 instanceof Wrapper) {
/* 45 */       return ((Wrapper)debug0).name;
/*    */     }
/* 47 */     return DEFAULT_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public static SuggestionProvider<SharedSuggestionProvider> safelySwap(SuggestionProvider<SharedSuggestionProvider> debug0) {
/* 52 */     if (debug0 instanceof Wrapper) {
/* 53 */       return debug0;
/*    */     }
/* 55 */     return ASK_SERVER;
/*    */   }
/*    */   
/*    */   public static class Wrapper
/*    */     implements SuggestionProvider<SharedSuggestionProvider> {
/*    */     private final SuggestionProvider<SharedSuggestionProvider> delegate;
/*    */     private final ResourceLocation name;
/*    */     
/*    */     public Wrapper(ResourceLocation debug1, SuggestionProvider<SharedSuggestionProvider> debug2) {
/* 64 */       this.delegate = debug2;
/* 65 */       this.name = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public CompletableFuture<Suggestions> getSuggestions(CommandContext<SharedSuggestionProvider> debug1, SuggestionsBuilder debug2) throws CommandSyntaxException {
/* 70 */       return this.delegate.getSuggestions(debug1, debug2);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\SuggestionProviders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */