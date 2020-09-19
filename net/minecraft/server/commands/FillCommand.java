/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class FillCommand {
/*     */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
/*     */   
/*     */   static {
/*  38 */     ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.fill.toobig", new Object[] { debug0, debug1 }));
/*  39 */   } private static final BlockInput HOLLOW_CORE = new BlockInput(Blocks.AIR.defaultBlockState(), Collections.emptySet(), null);
/*  40 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.fill.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  43 */     debug0.register(
/*  44 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fill")
/*  45 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  46 */         .then(
/*  47 */           Commands.argument("from", (ArgumentType)BlockPosArgument.blockPos())
/*  48 */           .then(
/*  49 */             Commands.argument("to", (ArgumentType)BlockPosArgument.blockPos())
/*  50 */             .then((
/*  51 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("block", (ArgumentType)BlockStateArgument.block())
/*  52 */               .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, null)))
/*  53 */               .then((
/*  54 */                 (LiteralArgumentBuilder)Commands.literal("replace")
/*  55 */                 .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, null)))
/*  56 */                 .then(
/*  57 */                   Commands.argument("filter", (ArgumentType)BlockPredicateArgument.blockPredicate())
/*  58 */                   .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, BlockPredicateArgument.getBlockPredicate(debug0, "filter"))))))
/*     */ 
/*     */               
/*  61 */               .then(
/*  62 */                 Commands.literal("keep")
/*  63 */                 .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.REPLACE, ()))))
/*     */               
/*  65 */               .then(
/*  66 */                 Commands.literal("outline")
/*  67 */                 .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.OUTLINE, null))))
/*     */               
/*  69 */               .then(
/*  70 */                 Commands.literal("hollow")
/*  71 */                 .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.HOLLOW, null))))
/*     */               
/*  73 */               .then(
/*  74 */                 Commands.literal("destroy")
/*  75 */                 .executes(debug0 -> fillBlocks((CommandSourceStack)debug0.getSource(), new BoundingBox((Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "from"), (Vec3i)BlockPosArgument.getLoadedBlockPos(debug0, "to")), BlockStateArgument.getBlock(debug0, "block"), Mode.DESTROY, null)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int fillBlocks(CommandSourceStack debug0, BoundingBox debug1, BlockInput debug2, Mode debug3, @Nullable Predicate<BlockInWorld> debug4) throws CommandSyntaxException {
/*  84 */     int debug5 = debug1.getXSpan() * debug1.getYSpan() * debug1.getZSpan();
/*  85 */     if (debug5 > 32768) {
/*  86 */       throw ERROR_AREA_TOO_LARGE.create(Integer.valueOf(32768), Integer.valueOf(debug5));
/*     */     }
/*     */     
/*  89 */     List<BlockPos> debug6 = Lists.newArrayList();
/*  90 */     ServerLevel debug7 = debug0.getLevel();
/*  91 */     int debug8 = 0;
/*     */     
/*  93 */     for (BlockPos debug10 : BlockPos.betweenClosed(debug1.x0, debug1.y0, debug1.z0, debug1.x1, debug1.y1, debug1.z1)) {
/*  94 */       if (debug4 != null && !debug4.test(new BlockInWorld((LevelReader)debug7, debug10, true))) {
/*     */         continue;
/*     */       }
/*  97 */       BlockInput debug11 = debug3.filter.filter(debug1, debug10, debug2, debug7);
/*  98 */       if (debug11 == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 102 */       BlockEntity debug12 = debug7.getBlockEntity(debug10);
/* 103 */       Clearable.tryClear(debug12);
/*     */       
/* 105 */       if (!debug11.place(debug7, debug10, 2)) {
/*     */         continue;
/*     */       }
/*     */       
/* 109 */       debug6.add(debug10.immutable());
/* 110 */       debug8++;
/*     */     } 
/*     */     
/* 113 */     for (BlockPos debug10 : debug6) {
/* 114 */       Block debug11 = debug7.getBlockState(debug10).getBlock();
/* 115 */       debug7.blockUpdated(debug10, debug11);
/*     */     } 
/*     */     
/* 118 */     if (debug8 == 0) {
/* 119 */       throw ERROR_FAILED.create();
/*     */     }
/*     */     
/* 122 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.fill.success", new Object[] { Integer.valueOf(debug8) }), true);
/*     */     
/* 124 */     return debug8;
/*     */   }
/*     */   enum Mode { REPLACE, OUTLINE, HOLLOW, DESTROY;
/*     */     static {
/* 128 */       REPLACE = new Mode("REPLACE", 0, (debug0, debug1, debug2, debug3) -> debug2);
/* 129 */       OUTLINE = new Mode("OUTLINE", 1, (debug0, debug1, debug2, debug3) -> 
/* 130 */           (debug1.getX() == debug0.x0 || debug1.getX() == debug0.x1 || debug1.getY() == debug0.y0 || debug1.getY() == debug0.y1 || debug1.getZ() == debug0.z0 || debug1.getZ() == debug0.z1) ? debug2 : null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 136 */       HOLLOW = new Mode("HOLLOW", 2, (debug0, debug1, debug2, debug3) -> 
/* 137 */           (debug1.getX() == debug0.x0 || debug1.getX() == debug0.x1 || debug1.getY() == debug0.y0 || debug1.getY() == debug0.y1 || debug1.getZ() == debug0.z0 || debug1.getZ() == debug0.z1) ? debug2 : FillCommand.HOLLOW_CORE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       DESTROY = new Mode("DESTROY", 3, (debug0, debug1, debug2, debug3) -> {
/*     */             debug3.destroyBlock(debug1, true);
/*     */             return debug2;
/*     */           });
/*     */     }
/*     */     public final SetBlockCommand.Filter filter;
/*     */     
/*     */     Mode(SetBlockCommand.Filter debug3) {
/* 151 */       this.filter = debug3;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\FillCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */