/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class CloneCommands {
/*     */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
/*  38 */   private static final SimpleCommandExceptionType ERROR_OVERLAP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.clone.overlap")); static {
/*  39 */     ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.clone.toobig", new Object[] { debug0, debug1 }));
/*  40 */   } private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.clone.failed")); static {
/*  41 */     FILTER_AIR = (debug0 -> !debug0.getState().isAir());
/*     */   } public static final Predicate<BlockInWorld> FILTER_AIR;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  44 */     debug0.register(
/*  45 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clone")
/*  46 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  47 */         .then(
/*  48 */           Commands.argument("begin", (ArgumentType)BlockPosArgument.blockPos())
/*  49 */           .then(
/*  50 */             Commands.argument("end", (ArgumentType)BlockPosArgument.blockPos())
/*  51 */             .then((
/*  52 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("destination", (ArgumentType)BlockPosArgument.blockPos())
/*  53 */               .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), (), Mode.NORMAL)))
/*  54 */               .then((
/*  55 */                 (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("replace")
/*  56 */                 .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), (), Mode.NORMAL)))
/*  57 */                 .then(
/*  58 */                   Commands.literal("force")
/*  59 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), (), Mode.FORCE))))
/*     */                 
/*  61 */                 .then(
/*  62 */                   Commands.literal("move")
/*  63 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), (), Mode.MOVE))))
/*     */                 
/*  65 */                 .then(
/*  66 */                   Commands.literal("normal")
/*  67 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), (), Mode.NORMAL)))))
/*     */ 
/*     */               
/*  70 */               .then((
/*  71 */                 (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("masked")
/*  72 */                 .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), FILTER_AIR, Mode.NORMAL)))
/*  73 */                 .then(
/*  74 */                   Commands.literal("force")
/*  75 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), FILTER_AIR, Mode.FORCE))))
/*     */                 
/*  77 */                 .then(
/*  78 */                   Commands.literal("move")
/*  79 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), FILTER_AIR, Mode.MOVE))))
/*     */                 
/*  81 */                 .then(
/*  82 */                   Commands.literal("normal")
/*  83 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), FILTER_AIR, Mode.NORMAL)))))
/*     */ 
/*     */               
/*  86 */               .then(
/*  87 */                 Commands.literal("filtered")
/*  88 */                 .then((
/*  89 */                   (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("filter", (ArgumentType)BlockPredicateArgument.blockPredicate())
/*  90 */                   .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), BlockPredicateArgument.getBlockPredicate(debug0, "filter"), Mode.NORMAL)))
/*  91 */                   .then(
/*  92 */                     Commands.literal("force")
/*  93 */                     .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), BlockPredicateArgument.getBlockPredicate(debug0, "filter"), Mode.FORCE))))
/*     */                   
/*  95 */                   .then(
/*  96 */                     Commands.literal("move")
/*  97 */                     .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), BlockPredicateArgument.getBlockPredicate(debug0, "filter"), Mode.MOVE))))
/*     */                   
/*  99 */                   .then(
/* 100 */                     Commands.literal("normal")
/* 101 */                     .executes(debug0 -> clone((CommandSourceStack)debug0.getSource(), BlockPosArgument.getLoadedBlockPos(debug0, "begin"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), BlockPredicateArgument.getBlockPredicate(debug0, "filter"), Mode.NORMAL)))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int clone(CommandSourceStack debug0, BlockPos debug1, BlockPos debug2, BlockPos debug3, Predicate<BlockInWorld> debug4, Mode debug5) throws CommandSyntaxException {
/* 112 */     BoundingBox debug6 = new BoundingBox((Vec3i)debug1, (Vec3i)debug2);
/* 113 */     BlockPos debug7 = debug3.offset(debug6.getLength());
/* 114 */     BoundingBox debug8 = new BoundingBox((Vec3i)debug3, (Vec3i)debug7);
/* 115 */     if (!debug5.canOverlap() && debug8.intersects(debug6)) {
/* 116 */       throw ERROR_OVERLAP.create();
/*     */     }
/* 118 */     int debug9 = debug6.getXSpan() * debug6.getYSpan() * debug6.getZSpan();
/* 119 */     if (debug9 > 32768) {
/* 120 */       throw ERROR_AREA_TOO_LARGE.create(Integer.valueOf(32768), Integer.valueOf(debug9));
/*     */     }
/* 122 */     ServerLevel debug10 = debug0.getLevel();
/* 123 */     if (!debug10.hasChunksAt(debug1, debug2) || !debug10.hasChunksAt(debug3, debug7)) {
/* 124 */       throw BlockPosArgument.ERROR_NOT_LOADED.create();
/*     */     }
/*     */     
/* 127 */     List<CloneBlockInfo> debug11 = Lists.newArrayList();
/* 128 */     List<CloneBlockInfo> debug12 = Lists.newArrayList();
/* 129 */     List<CloneBlockInfo> debug13 = Lists.newArrayList();
/* 130 */     Deque<BlockPos> debug14 = Lists.newLinkedList();
/*     */     
/* 132 */     BlockPos debug15 = new BlockPos(debug8.x0 - debug6.x0, debug8.y0 - debug6.y0, debug8.z0 - debug6.z0);
/* 133 */     for (int i = debug6.z0; i <= debug6.z1; i++) {
/* 134 */       for (int j = debug6.y0; j <= debug6.y1; j++) {
/* 135 */         for (int k = debug6.x0; k <= debug6.x1; k++) {
/* 136 */           BlockPos debug19 = new BlockPos(k, j, i);
/* 137 */           BlockPos debug20 = debug19.offset((Vec3i)debug15);
/* 138 */           BlockInWorld debug21 = new BlockInWorld((LevelReader)debug10, debug19, false);
/* 139 */           BlockState debug22 = debug21.getState();
/* 140 */           if (debug4.test(debug21)) {
/*     */ 
/*     */ 
/*     */             
/* 144 */             BlockEntity debug23 = debug10.getBlockEntity(debug19);
/* 145 */             if (debug23 != null) {
/* 146 */               CompoundTag debug24 = debug23.save(new CompoundTag());
/* 147 */               debug12.add(new CloneBlockInfo(debug20, debug22, debug24));
/* 148 */               debug14.addLast(debug19);
/* 149 */             } else if (debug22.isSolidRender((BlockGetter)debug10, debug19) || debug22.isCollisionShapeFullBlock((BlockGetter)debug10, debug19)) {
/* 150 */               debug11.add(new CloneBlockInfo(debug20, debug22, null));
/* 151 */               debug14.addLast(debug19);
/*     */             } else {
/* 153 */               debug13.add(new CloneBlockInfo(debug20, debug22, null));
/* 154 */               debug14.addFirst(debug19);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 160 */     if (debug5 == Mode.MOVE) {
/* 161 */       for (BlockPos blockPos : debug14) {
/* 162 */         BlockEntity blockEntity = debug10.getBlockEntity(blockPos);
/* 163 */         Clearable.tryClear(blockEntity);
/* 164 */         debug10.setBlock(blockPos, Blocks.BARRIER.defaultBlockState(), 2);
/*     */       } 
/* 166 */       for (BlockPos blockPos : debug14) {
/* 167 */         debug10.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
/*     */       }
/*     */     } 
/*     */     
/* 171 */     List<CloneBlockInfo> debug16 = Lists.newArrayList();
/* 172 */     debug16.addAll(debug11);
/* 173 */     debug16.addAll(debug12);
/* 174 */     debug16.addAll(debug13);
/*     */     
/* 176 */     List<CloneBlockInfo> debug17 = Lists.reverse(debug16);
/* 177 */     for (CloneBlockInfo debug19 : debug17) {
/* 178 */       BlockEntity debug20 = debug10.getBlockEntity(debug19.pos);
/* 179 */       Clearable.tryClear(debug20);
/* 180 */       debug10.setBlock(debug19.pos, Blocks.BARRIER.defaultBlockState(), 2);
/*     */     } 
/*     */     
/* 183 */     int debug18 = 0;
/* 184 */     for (CloneBlockInfo debug20 : debug16) {
/* 185 */       if (debug10.setBlock(debug20.pos, debug20.state, 2)) {
/* 186 */         debug18++;
/*     */       }
/*     */     } 
/* 189 */     for (CloneBlockInfo debug20 : debug12) {
/* 190 */       BlockEntity debug21 = debug10.getBlockEntity(debug20.pos);
/* 191 */       if (debug20.tag != null && debug21 != null) {
/* 192 */         debug20.tag.putInt("x", debug20.pos.getX());
/* 193 */         debug20.tag.putInt("y", debug20.pos.getY());
/* 194 */         debug20.tag.putInt("z", debug20.pos.getZ());
/* 195 */         debug21.load(debug20.state, debug20.tag);
/* 196 */         debug21.setChanged();
/*     */       } 
/* 198 */       debug10.setBlock(debug20.pos, debug20.state, 2);
/*     */     } 
/*     */     
/* 201 */     for (CloneBlockInfo debug20 : debug17) {
/* 202 */       debug10.blockUpdated(debug20.pos, debug20.state.getBlock());
/*     */     }
/*     */     
/* 205 */     debug10.getBlockTicks().copy(debug6, debug15);
/*     */     
/* 207 */     if (debug18 == 0) {
/* 208 */       throw ERROR_FAILED.create();
/*     */     }
/*     */     
/* 211 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.clone.success", new Object[] { Integer.valueOf(debug18) }), true);
/*     */     
/* 213 */     return debug18;
/*     */   }
/*     */   
/*     */   enum Mode {
/* 217 */     FORCE(true),
/* 218 */     MOVE(true),
/* 219 */     NORMAL(false);
/*     */     
/*     */     private final boolean canOverlap;
/*     */ 
/*     */     
/*     */     Mode(boolean debug3) {
/* 225 */       this.canOverlap = debug3;
/*     */     }
/*     */     
/*     */     public boolean canOverlap() {
/* 229 */       return this.canOverlap;
/*     */     }
/*     */   }
/*     */   
/*     */   static class CloneBlockInfo {
/*     */     public final BlockPos pos;
/*     */     public final BlockState state;
/*     */     @Nullable
/*     */     public final CompoundTag tag;
/*     */     
/*     */     public CloneBlockInfo(BlockPos debug1, BlockState debug2, @Nullable CompoundTag debug3) {
/* 240 */       this.pos = debug1;
/* 241 */       this.state = debug2;
/* 242 */       this.tag = debug3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\CloneCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */