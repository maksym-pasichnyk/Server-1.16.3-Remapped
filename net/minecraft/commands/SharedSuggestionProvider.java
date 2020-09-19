/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public interface SharedSuggestionProvider
/*     */ {
/*     */   public static class TextCoordinates {
/*  27 */     public static final TextCoordinates DEFAULT_LOCAL = new TextCoordinates("^", "^", "^");
/*     */     
/*  29 */     public static final TextCoordinates DEFAULT_GLOBAL = new TextCoordinates("~", "~", "~");
/*     */     
/*     */     public final String x;
/*     */     
/*     */     public final String y;
/*     */     
/*     */     public final String z;
/*     */     
/*     */     public TextCoordinates(String debug1, String debug2, String debug3) {
/*  38 */       this.x = debug1;
/*  39 */       this.y = debug2;
/*  40 */       this.z = debug3;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default Collection<String> getSelectedEntities() {
/*  47 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Collection<TextCoordinates> getRelevantCoordinates() {
/*  59 */     return Collections.singleton(TextCoordinates.DEFAULT_GLOBAL);
/*     */   }
/*     */   
/*     */   default Collection<TextCoordinates> getAbsoluteCoordinates() {
/*  63 */     return Collections.singleton(TextCoordinates.DEFAULT_GLOBAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> void filterResources(Iterable<T> debug0, String debug1, Function<T, ResourceLocation> debug2, Consumer<T> debug3) {
/*  73 */     boolean debug4 = (debug1.indexOf(':') > -1);
/*  74 */     for (T debug6 : debug0) {
/*  75 */       ResourceLocation debug7 = debug2.apply(debug6);
/*  76 */       if (debug4) {
/*  77 */         String debug8 = debug7.toString();
/*  78 */         if (matchesSubStr(debug1, debug8))
/*  79 */           debug3.accept(debug6); 
/*     */         continue;
/*     */       } 
/*  82 */       if (matchesSubStr(debug1, debug7.getNamespace()) || (debug7.getNamespace().equals("minecraft") && matchesSubStr(debug1, debug7.getPath()))) {
/*  83 */         debug3.accept(debug6);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> void filterResources(Iterable<T> debug0, String debug1, String debug2, Function<T, ResourceLocation> debug3, Consumer<T> debug4) {
/*  90 */     if (debug1.isEmpty()) {
/*  91 */       debug0.forEach(debug4);
/*     */     } else {
/*  93 */       String debug5 = Strings.commonPrefix(debug1, debug2);
/*  94 */       if (!debug5.isEmpty()) {
/*  95 */         String debug6 = debug1.substring(debug5.length());
/*  96 */         filterResources(debug0, debug6, debug3, debug4);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestResource(Iterable<ResourceLocation> debug0, SuggestionsBuilder debug1, String debug2) {
/* 102 */     String debug3 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 103 */     filterResources(debug0, debug3, debug2, debug0 -> debug0, debug2 -> debug0.suggest(debug1 + debug2));
/* 104 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestResource(Iterable<ResourceLocation> debug0, SuggestionsBuilder debug1) {
/* 108 */     String debug2 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 109 */     filterResources(debug0, debug2, debug0 -> debug0, debug1 -> debug0.suggest(debug1.toString()));
/* 110 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   static <T> CompletableFuture<Suggestions> suggestResource(Iterable<T> debug0, SuggestionsBuilder debug1, Function<T, ResourceLocation> debug2, Function<T, Message> debug3) {
/* 114 */     String debug4 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 115 */     filterResources(debug0, debug4, debug2, debug3 -> debug0.suggest(((ResourceLocation)debug1.apply(debug3)).toString(), debug2.apply(debug3)));
/* 116 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestResource(Stream<ResourceLocation> debug0, SuggestionsBuilder debug1) {
/* 120 */     return suggestResource(debug0::iterator, debug1);
/*     */   }
/*     */   
/*     */   static <T> CompletableFuture<Suggestions> suggestResource(Stream<T> debug0, SuggestionsBuilder debug1, Function<T, ResourceLocation> debug2, Function<T, Message> debug3) {
/* 124 */     return suggestResource(debug0::iterator, debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggestCoordinates(String debug0, Collection<TextCoordinates> debug1, SuggestionsBuilder debug2, Predicate<String> debug3) {
/* 128 */     List<String> debug4 = Lists.newArrayList();
/*     */     
/* 130 */     if (Strings.isNullOrEmpty(debug0)) {
/* 131 */       for (TextCoordinates debug6 : debug1) {
/* 132 */         String debug7 = debug6.x + " " + debug6.y + " " + debug6.z;
/* 133 */         if (debug3.test(debug7)) {
/* 134 */           debug4.add(debug6.x);
/* 135 */           debug4.add(debug6.x + " " + debug6.y);
/* 136 */           debug4.add(debug7);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 140 */       String[] debug5 = debug0.split(" ");
/*     */       
/* 142 */       if (debug5.length == 1) {
/* 143 */         for (TextCoordinates debug7 : debug1) {
/* 144 */           String debug8 = debug5[0] + " " + debug7.y + " " + debug7.z;
/* 145 */           if (debug3.test(debug8)) {
/* 146 */             debug4.add(debug5[0] + " " + debug7.y);
/* 147 */             debug4.add(debug8);
/*     */           } 
/*     */         } 
/* 150 */       } else if (debug5.length == 2) {
/* 151 */         for (TextCoordinates debug7 : debug1) {
/* 152 */           String debug8 = debug5[0] + " " + debug5[1] + " " + debug7.z;
/* 153 */           if (debug3.test(debug8)) {
/* 154 */             debug4.add(debug8);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 159 */     return suggest(debug4, debug2);
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest2DCoordinates(String debug0, Collection<TextCoordinates> debug1, SuggestionsBuilder debug2, Predicate<String> debug3) {
/* 163 */     List<String> debug4 = Lists.newArrayList();
/*     */     
/* 165 */     if (Strings.isNullOrEmpty(debug0)) {
/* 166 */       for (TextCoordinates debug6 : debug1) {
/* 167 */         String debug7 = debug6.x + " " + debug6.z;
/* 168 */         if (debug3.test(debug7)) {
/* 169 */           debug4.add(debug6.x);
/* 170 */           debug4.add(debug7);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 174 */       String[] debug5 = debug0.split(" ");
/* 175 */       if (debug5.length == 1) {
/* 176 */         for (TextCoordinates debug7 : debug1) {
/* 177 */           String debug8 = debug5[0] + " " + debug7.z;
/* 178 */           if (debug3.test(debug8)) {
/* 179 */             debug4.add(debug8);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 184 */     return suggest(debug4, debug2);
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest(Iterable<String> debug0, SuggestionsBuilder debug1) {
/* 188 */     String debug2 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 189 */     for (String debug4 : debug0) {
/* 190 */       if (matchesSubStr(debug2, debug4.toLowerCase(Locale.ROOT))) {
/* 191 */         debug1.suggest(debug4);
/*     */       }
/*     */     } 
/* 194 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest(Stream<String> debug0, SuggestionsBuilder debug1) {
/* 198 */     String debug2 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 199 */     debug0.filter(debug1 -> matchesSubStr(debug0, debug1.toLowerCase(Locale.ROOT))).forEach(debug1::suggest);
/* 200 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   static CompletableFuture<Suggestions> suggest(String[] debug0, SuggestionsBuilder debug1) {
/* 204 */     String debug2 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 205 */     for (String debug6 : debug0) {
/* 206 */       if (matchesSubStr(debug2, debug6.toLowerCase(Locale.ROOT))) {
/* 207 */         debug1.suggest(debug6);
/*     */       }
/*     */     } 
/* 210 */     return debug1.buildFuture();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean matchesSubStr(String debug0, String debug1) {
/* 225 */     int debug2 = 0;
/* 226 */     while (!debug1.startsWith(debug0, debug2)) {
/* 227 */       debug2 = debug1.indexOf('_', debug2);
/* 228 */       if (debug2 < 0) {
/* 229 */         return false;
/*     */       }
/*     */       
/* 232 */       debug2++;
/*     */     } 
/*     */     
/* 235 */     return true;
/*     */   }
/*     */   
/*     */   Collection<String> getOnlinePlayerNames();
/*     */   
/*     */   Collection<String> getAllTeams();
/*     */   
/*     */   Collection<ResourceLocation> getAvailableSoundEvents();
/*     */   
/*     */   Stream<ResourceLocation> getRecipeNames();
/*     */   
/*     */   CompletableFuture<Suggestions> customSuggestion(CommandContext<SharedSuggestionProvider> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder);
/*     */   
/*     */   Set<ResourceKey<Level>> levels();
/*     */   
/*     */   RegistryAccess registryAccess();
/*     */   
/*     */   boolean hasPermission(int paramInt);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\SharedSuggestionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */