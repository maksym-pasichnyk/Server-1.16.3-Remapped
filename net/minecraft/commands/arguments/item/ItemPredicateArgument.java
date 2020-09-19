/*     */ package net.minecraft.commands.arguments.item;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ItemPredicateArgument implements ArgumentType<ItemPredicateArgument.Result> {
/*  27 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stick", "minecraft:stick", "#stick", "#stick{foo=bar}" }); static {
/*  28 */     ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.item.tag.unknown", new Object[] { debug0 }));
/*     */   } private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
/*     */   public static ItemPredicateArgument itemPredicate() {
/*  31 */     return new ItemPredicateArgument();
/*     */   }
/*     */ 
/*     */   
/*     */   public Result parse(StringReader debug1) throws CommandSyntaxException {
/*  36 */     ItemParser debug2 = (new ItemParser(debug1, true)).parse();
/*     */     
/*  38 */     if (debug2.getItem() != null) {
/*  39 */       ItemPredicate itemPredicate = new ItemPredicate(debug2.getItem(), debug2.getNbt());
/*  40 */       return debug1 -> debug0;
/*     */     } 
/*  42 */     ResourceLocation debug3 = debug2.getTag();
/*  43 */     return debug2 -> {
/*     */         Tag<Item> debug3 = ((CommandSourceStack)debug2.getSource()).getServer().getTags().getItems().getTag(debug0);
/*     */         if (debug3 == null) {
/*     */           throw ERROR_UNKNOWN_TAG.create(debug0.toString());
/*     */         }
/*     */         return new TagPredicate(debug3, debug1.getNbt());
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<ItemStack> getItemPredicate(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  54 */     return ((Result)debug0.getArgument(debug1, Result.class)).create(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/*  59 */     StringReader debug3 = new StringReader(debug2.getInput());
/*  60 */     debug3.setCursor(debug2.getStart());
/*  61 */     ItemParser debug4 = new ItemParser(debug3, true);
/*     */     try {
/*  63 */       debug4.parse();
/*  64 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*     */     
/*  66 */     return debug4.fillSuggestions(debug2, ItemTags.getAllTags());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/*  71 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static interface Result {
/*     */     Predicate<ItemStack> create(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   static class ItemPredicate implements Predicate<ItemStack> {
/*     */     private final Item item;
/*     */     @Nullable
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public ItemPredicate(Item debug1, @Nullable CompoundTag debug2) {
/*  84 */       this.item = debug1;
/*  85 */       this.nbt = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(ItemStack debug1) {
/*  90 */       return (debug1.getItem() == this.item && NbtUtils.compareNbt((Tag)this.nbt, (Tag)debug1.getTag(), true));
/*     */     }
/*     */   }
/*     */   
/*     */   static class TagPredicate implements Predicate<ItemStack> {
/*     */     private final Tag<Item> tag;
/*     */     @Nullable
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public TagPredicate(Tag<Item> debug1, @Nullable CompoundTag debug2) {
/* 100 */       this.tag = debug1;
/* 101 */       this.nbt = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(ItemStack debug1) {
/* 106 */       return (this.tag.contains(debug1.getItem()) && NbtUtils.compareNbt((Tag)this.nbt, (Tag)debug1.getTag(), true));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\item\ItemPredicateArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */