/*     */ package net.minecraft.commands.arguments.item;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.TagCollection;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ItemParser {
/*  26 */   public static final SimpleCommandExceptionType ERROR_NO_TAGS_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.item.tag.disallowed")); public static final DynamicCommandExceptionType ERROR_UNKNOWN_ITEM; static {
/*  27 */     ERROR_UNKNOWN_ITEM = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.item.id.invalid", new Object[] { debug0 }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     SUGGEST_NOTHING = ((debug0, debug1) -> debug0.buildFuture());
/*     */   }
/*     */   private static final BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>> SUGGEST_NOTHING; private final StringReader reader;
/*     */   private final boolean forTesting;
/*  36 */   private final Map<Property<?>, Comparable<?>> properties = Maps.newHashMap();
/*     */   private Item item;
/*     */   @Nullable
/*     */   private CompoundTag nbt;
/*  40 */   private ResourceLocation tag = new ResourceLocation("");
/*     */   private int tagCursor;
/*  42 */   private BiFunction<SuggestionsBuilder, TagCollection<Item>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   
/*     */   public ItemParser(StringReader debug1, boolean debug2) {
/*  45 */     this.reader = debug1;
/*  46 */     this.forTesting = debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItem() {
/*  54 */     return this.item;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getNbt() {
/*  59 */     return this.nbt;
/*     */   }
/*     */   
/*     */   public ResourceLocation getTag() {
/*  63 */     return this.tag;
/*     */   }
/*     */   
/*     */   public void readItem() throws CommandSyntaxException {
/*  67 */     int debug1 = this.reader.getCursor();
/*  68 */     ResourceLocation debug2 = ResourceLocation.read(this.reader);
/*  69 */     this.item = (Item)Registry.ITEM.getOptional(debug2).orElseThrow(() -> {
/*     */           this.reader.setCursor(debug1);
/*     */           return ERROR_UNKNOWN_ITEM.createWithContext((ImmutableStringReader)this.reader, debug2.toString());
/*     */         });
/*     */   }
/*     */   
/*     */   public void readTag() throws CommandSyntaxException {
/*  76 */     if (!this.forTesting) {
/*  77 */       throw ERROR_NO_TAGS_ALLOWED.create();
/*     */     }
/*     */     
/*  80 */     this.suggestions = this::suggestTag;
/*  81 */     this.reader.expect('#');
/*  82 */     this.tagCursor = this.reader.getCursor();
/*  83 */     this.tag = ResourceLocation.read(this.reader);
/*     */   }
/*     */   
/*     */   public void readNbt() throws CommandSyntaxException {
/*  87 */     this.nbt = (new TagParser(this.reader)).readStruct();
/*     */   }
/*     */   
/*     */   public ItemParser parse() throws CommandSyntaxException {
/*  91 */     this.suggestions = this::suggestItemIdOrTag;
/*  92 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/*  93 */       readTag();
/*     */     } else {
/*  95 */       readItem();
/*  96 */       this.suggestions = this::suggestOpenNbt;
/*     */     } 
/*  98 */     if (this.reader.canRead() && this.reader.peek() == '{') {
/*  99 */       this.suggestions = SUGGEST_NOTHING;
/* 100 */       readNbt();
/*     */     } 
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenNbt(SuggestionsBuilder debug1, TagCollection<Item> debug2) {
/* 106 */     if (debug1.getRemaining().isEmpty()) {
/* 107 */       debug1.suggest(String.valueOf('{'));
/*     */     }
/* 109 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestTag(SuggestionsBuilder debug1, TagCollection<Item> debug2) {
/* 113 */     return SharedSuggestionProvider.suggestResource(debug2.getAvailableTags(), debug1.createOffset(this.tagCursor));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestItemIdOrTag(SuggestionsBuilder debug1, TagCollection<Item> debug2) {
/* 117 */     if (this.forTesting) {
/* 118 */       SharedSuggestionProvider.suggestResource(debug2.getAvailableTags(), debug1, String.valueOf('#'));
/*     */     }
/* 120 */     return SharedSuggestionProvider.suggestResource(Registry.ITEM.keySet(), debug1);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder debug1, TagCollection<Item> debug2) {
/* 124 */     return this.suggestions.apply(debug1.createOffset(this.reader.getCursor()), debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\item\ItemParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */