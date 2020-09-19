/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Locale;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.critereon.NbtPredicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityDataAccessor
/*    */   implements DataAccessor
/*    */ {
/* 27 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYERS = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.data.entity.invalid"));
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = debug0 -> new DataCommands.DataProvider()
/*    */     {
/*    */       public DataAccessor access(CommandContext debug1) throws CommandSyntaxException {
/* 32 */         return new EntityDataAccessor(EntityArgument.getEntity(debug1, arg));
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder debug1, Function<RequiredArgumentBuilder, ArgumentBuilder> debug2) {
/* 37 */         return debug1.then(Commands.literal("entity").then(debug2.apply(Commands.argument(arg, (ArgumentType)EntityArgument.entity()))));
/*    */       }
/*    */     };
/*    */   
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityDataAccessor(Entity debug1) {
/* 44 */     this.entity = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag debug1) throws CommandSyntaxException {
/* 49 */     if (this.entity instanceof net.minecraft.world.entity.player.Player) {
/* 50 */       throw ERROR_NO_PLAYERS.create();
/*    */     }
/* 52 */     UUID debug2 = this.entity.getUUID();
/* 53 */     this.entity.load(debug1);
/* 54 */     this.entity.setUUID(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getData() {
/* 59 */     return NbtPredicate.getEntityTagToCompare(this.entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 64 */     return (Component)new TranslatableComponent("commands.data.entity.modified", new Object[] { this.entity.getDisplayName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag debug1) {
/* 69 */     return (Component)new TranslatableComponent("commands.data.entity.query", new Object[] { this.entity.getDisplayName(), debug1.getPrettyDisplay() });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath debug1, double debug2, int debug4) {
/* 74 */     return (Component)new TranslatableComponent("commands.data.entity.get", new Object[] { debug1, this.entity.getDisplayName(), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug2) }), Integer.valueOf(debug4) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\data\EntityDataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */