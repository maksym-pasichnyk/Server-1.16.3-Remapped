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
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockDataAccessor
/*    */   implements DataAccessor
/*    */ {
/* 27 */   private static final SimpleCommandExceptionType ERROR_NOT_A_BLOCK_ENTITY = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.data.block.invalid"));
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = debug0 -> new DataCommands.DataProvider()
/*    */     {
/*    */       public DataAccessor access(CommandContext debug1) throws CommandSyntaxException {
/* 32 */         BlockPos debug2 = BlockPosArgument.getLoadedBlockPos(debug1, argPrefix + "Pos");
/* 33 */         BlockEntity debug3 = ((CommandSourceStack)debug1.getSource()).getLevel().getBlockEntity(debug2);
/* 34 */         if (debug3 == null) {
/* 35 */           throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
/*    */         }
/* 37 */         return new BlockDataAccessor(debug3, debug2);
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder debug1, Function<RequiredArgumentBuilder, ArgumentBuilder> debug2) {
/* 42 */         return debug1.then(Commands.literal("block").then(debug2.apply(Commands.argument(argPrefix + "Pos", (ArgumentType)BlockPosArgument.blockPos()))));
/*    */       }
/*    */     };
/*    */   
/*    */   private final BlockEntity entity;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public BlockDataAccessor(BlockEntity debug1, BlockPos debug2) {
/* 50 */     this.entity = debug1;
/* 51 */     this.pos = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag debug1) {
/* 56 */     debug1.putInt("x", this.pos.getX());
/* 57 */     debug1.putInt("y", this.pos.getY());
/* 58 */     debug1.putInt("z", this.pos.getZ());
/* 59 */     BlockState debug2 = this.entity.getLevel().getBlockState(this.pos);
/* 60 */     this.entity.load(debug2, debug1);
/* 61 */     this.entity.setChanged();
/* 62 */     this.entity.getLevel().sendBlockUpdated(this.pos, debug2, debug2, 3);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getData() {
/* 67 */     return this.entity.save(new CompoundTag());
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 72 */     return (Component)new TranslatableComponent("commands.data.block.modified", new Object[] { Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag debug1) {
/* 77 */     return (Component)new TranslatableComponent("commands.data.block.query", new Object[] { Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()), debug1.getPrettyDisplay() });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath debug1, double debug2, int debug4) {
/* 82 */     return (Component)new TranslatableComponent("commands.data.block.get", new Object[] { debug1, Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug2) }), Integer.valueOf(debug4) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\data\BlockDataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */