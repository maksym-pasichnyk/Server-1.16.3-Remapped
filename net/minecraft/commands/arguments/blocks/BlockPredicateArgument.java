/*     */ package net.minecraft.commands.arguments.blocks;
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
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.tags.TagContainer;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockPredicateArgument implements ArgumentType<BlockPredicateArgument.Result> {
/*  33 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stone", "minecraft:stone", "stone[foo=bar]", "#stone", "#stone[foo=bar]{baz=nbt}" }); static {
/*  34 */     ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.block.tag.unknown", new Object[] { debug0 }));
/*     */   } private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
/*     */   public static BlockPredicateArgument blockPredicate() {
/*  37 */     return new BlockPredicateArgument();
/*     */   }
/*     */ 
/*     */   
/*     */   public Result parse(StringReader debug1) throws CommandSyntaxException {
/*  42 */     BlockStateParser debug2 = (new BlockStateParser(debug1, true)).parse(true);
/*     */     
/*  44 */     if (debug2.getState() != null) {
/*  45 */       BlockPredicate blockPredicate = new BlockPredicate(debug2.getState(), debug2.getProperties().keySet(), debug2.getNbt());
/*  46 */       return debug1 -> debug0;
/*     */     } 
/*  48 */     ResourceLocation debug3 = debug2.getTag();
/*  49 */     return debug2 -> {
/*     */         Tag<Block> debug3 = debug2.getBlocks().getTag(debug0);
/*     */         if (debug3 == null) {
/*     */           throw ERROR_UNKNOWN_TAG.create(debug0.toString());
/*     */         }
/*     */         return new TagPredicate(debug3, debug1.getVagueProperties(), debug1.getNbt());
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<BlockInWorld> getBlockPredicate(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  60 */     return ((Result)debug0.getArgument(debug1, Result.class)).create(((CommandSourceStack)debug0.getSource()).getServer().getTags());
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/*  65 */     StringReader debug3 = new StringReader(debug2.getInput());
/*  66 */     debug3.setCursor(debug2.getStart());
/*  67 */     BlockStateParser debug4 = new BlockStateParser(debug3, true);
/*     */     try {
/*  69 */       debug4.parse(true);
/*  70 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*     */     
/*  72 */     return debug4.fillSuggestions(debug2, BlockTags.getAllTags());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/*  77 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static interface Result {
/*     */     Predicate<BlockInWorld> create(TagContainer param1TagContainer) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   static class BlockPredicate implements Predicate<BlockInWorld> {
/*     */     private final BlockState state;
/*     */     private final Set<Property<?>> properties;
/*     */     @Nullable
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public BlockPredicate(BlockState debug1, Set<Property<?>> debug2, @Nullable CompoundTag debug3) {
/*  91 */       this.state = debug1;
/*  92 */       this.properties = debug2;
/*  93 */       this.nbt = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(BlockInWorld debug1) {
/*  98 */       BlockState debug2 = debug1.getState();
/*     */       
/* 100 */       if (!debug2.is(this.state.getBlock())) {
/* 101 */         return false;
/*     */       }
/*     */       
/* 104 */       for (Property<?> debug4 : this.properties) {
/* 105 */         if (debug2.getValue(debug4) != this.state.getValue(debug4)) {
/* 106 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 110 */       if (this.nbt != null) {
/* 111 */         BlockEntity debug3 = debug1.getEntity();
/* 112 */         return (debug3 != null && NbtUtils.compareNbt((Tag)this.nbt, (Tag)debug3.save(new CompoundTag()), true));
/*     */       } 
/*     */       
/* 115 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static class TagPredicate implements Predicate<BlockInWorld> {
/*     */     private final Tag<Block> tag;
/*     */     @Nullable
/*     */     private final CompoundTag nbt;
/*     */     private final Map<String, String> vagueProperties;
/*     */     
/*     */     private TagPredicate(Tag<Block> debug1, Map<String, String> debug2, @Nullable CompoundTag debug3) {
/* 126 */       this.tag = debug1;
/* 127 */       this.vagueProperties = debug2;
/* 128 */       this.nbt = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(BlockInWorld debug1) {
/* 133 */       BlockState debug2 = debug1.getState();
/*     */       
/* 135 */       if (!debug2.is(this.tag)) {
/* 136 */         return false;
/*     */       }
/*     */       
/* 139 */       for (Map.Entry<String, String> debug4 : this.vagueProperties.entrySet()) {
/* 140 */         Property<?> debug5 = debug2.getBlock().getStateDefinition().getProperty(debug4.getKey());
/* 141 */         if (debug5 == null) {
/* 142 */           return false;
/*     */         }
/* 144 */         Comparable<?> debug6 = debug5.getValue(debug4.getValue()).orElse(null);
/* 145 */         if (debug6 == null) {
/* 146 */           return false;
/*     */         }
/* 148 */         if (debug2.getValue(debug5) != debug6) {
/* 149 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 153 */       if (this.nbt != null) {
/* 154 */         BlockEntity debug3 = debug1.getEntity();
/* 155 */         return (debug3 != null && NbtUtils.compareNbt((Tag)this.nbt, (Tag)debug3.save(new CompoundTag()), true));
/*     */       } 
/*     */       
/* 158 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */