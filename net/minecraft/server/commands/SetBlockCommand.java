/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*    */ import net.minecraft.commands.arguments.blocks.BlockStateArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Clearable;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class SetBlockCommand {
/* 29 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.setblock.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 32 */     debug0.register(
/* 33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setblock")
/* 34 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 35 */         .then(
/* 36 */           Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("block", (ArgumentType)BlockStateArgument.block())
/* 39 */             .executes(debug0 -> setBlock((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, null)))
/* 40 */             .then(
/* 41 */               Commands.literal("destroy")
/* 42 */               .executes(debug0 -> setBlock((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), BlockStateArgument.getBlock(debug0, "block"), Mode.DESTROY, null))))
/*    */             
/* 44 */             .then(
/* 45 */               Commands.literal("keep")
/* 46 */               .executes(debug0 -> setBlock((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, ()))))
/*    */             
/* 48 */             .then(
/* 49 */               Commands.literal("replace")
/* 50 */               .executes(debug0 -> setBlock((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, null))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setBlock(CommandSourceStack debug0, BlockPos debug1, BlockInput debug2, Mode debug3, @Nullable Predicate<BlockInWorld> debug4) throws CommandSyntaxException {
/*    */     boolean debug6;
/* 58 */     ServerLevel debug5 = debug0.getLevel();
/* 59 */     if (debug4 != null && !debug4.test(new BlockInWorld((LevelReader)debug5, debug1, true))) {
/* 60 */       throw ERROR_FAILED.create();
/*    */     }
/*    */ 
/*    */     
/* 64 */     if (debug3 == Mode.DESTROY) {
/* 65 */       debug5.destroyBlock(debug1, true);
/* 66 */       debug6 = (!debug2.getState().isAir() || !debug5.getBlockState(debug1).isAir());
/*    */     } else {
/* 68 */       BlockEntity debug7 = debug5.getBlockEntity(debug1);
/* 69 */       Clearable.tryClear(debug7);
/* 70 */       debug6 = true;
/*    */     } 
/* 72 */     if (debug6 && !debug2.place(debug5, debug1, 2)) {
/* 73 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 76 */     debug5.blockUpdated(debug1, debug2.getState().getBlock());
/* 77 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.setblock.success", new Object[] { Integer.valueOf(debug1.getX()), Integer.valueOf(debug1.getY()), Integer.valueOf(debug1.getZ()) }), true);
/* 78 */     return 1;
/*    */   } public static interface Filter {
/*    */     @Nullable
/*    */     BlockInput filter(BoundingBox param1BoundingBox, BlockPos param1BlockPos, BlockInput param1BlockInput, ServerLevel param1ServerLevel); }
/* 82 */   public enum Mode { REPLACE,
/* 83 */     DESTROY; }
/*    */ 
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SetBlockCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */