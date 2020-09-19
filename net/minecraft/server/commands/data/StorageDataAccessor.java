/*    */ package net.minecraft.server.commands.data;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.storage.CommandStorage;
/*    */ 
/*    */ public class StorageDataAccessor implements DataAccessor {
/*    */   static {
/* 25 */     SUGGEST_STORAGE = ((debug0, debug1) -> SharedSuggestionProvider.suggestResource(getGlobalTags(debug0).keys(), debug1));
/*    */   }
/*    */   
/*    */   private static final SuggestionProvider<CommandSourceStack> SUGGEST_STORAGE;
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = debug0 -> new DataCommands.DataProvider() { public DataAccessor access(CommandContext debug1) {
/* 31 */         return new StorageDataAccessor(StorageDataAccessor.getGlobalTags(debug1), ResourceLocationArgument.getId(debug1, arg));
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder debug1, Function<RequiredArgumentBuilder, ArgumentBuilder> debug2) {
/* 36 */         return debug1.then(Commands.literal("storage").then(debug2.apply(Commands.argument(arg, (ArgumentType)ResourceLocationArgument.id()).suggests(StorageDataAccessor.SUGGEST_STORAGE))));
/*    */       } }
/*    */   ;
/*    */   private final CommandStorage storage;
/*    */   private static CommandStorage getGlobalTags(CommandContext<CommandSourceStack> debug0) {
/* 41 */     return ((CommandSourceStack)debug0.getSource()).getServer().getCommandStorage();
/*    */   }
/*    */ 
/*    */   
/*    */   private final ResourceLocation id;
/*    */   
/*    */   private StorageDataAccessor(CommandStorage debug1, ResourceLocation debug2) {
/* 48 */     this.storage = debug1;
/* 49 */     this.id = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag debug1) {
/* 54 */     this.storage.set(this.id, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getData() {
/* 59 */     return this.storage.get(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 64 */     return (Component)new TranslatableComponent("commands.data.storage.modified", new Object[] { this.id });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag debug1) {
/* 69 */     return (Component)new TranslatableComponent("commands.data.storage.query", new Object[] { this.id, debug1.getPrettyDisplay() });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath debug1, double debug2, int debug4) {
/* 74 */     return (Component)new TranslatableComponent("commands.data.storage.get", new Object[] { debug1, this.id, String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug2) }), Integer.valueOf(debug4) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\data\StorageDataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */