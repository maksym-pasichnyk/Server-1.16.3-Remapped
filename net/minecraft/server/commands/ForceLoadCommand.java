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
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ColumnPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ForceLoadCommand {
/*     */   private static final Dynamic2CommandExceptionType ERROR_TOO_MANY_CHUNKS;
/*     */   
/*     */   static {
/*  28 */     ERROR_TOO_MANY_CHUNKS = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.forceload.toobig", new Object[] { debug0, debug1 }));
/*  29 */     ERROR_NOT_TICKING = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.forceload.query.failure", new Object[] { debug0, debug1 }));
/*  30 */   } private static final Dynamic2CommandExceptionType ERROR_NOT_TICKING; private static final SimpleCommandExceptionType ERROR_ALL_ADDED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.forceload.added.failure"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_NONE_REMOVED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.forceload.removed.failure"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  34 */     debug0.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("forceload")
/*  36 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  37 */         .then(
/*  38 */           Commands.literal("add")
/*  39 */           .then((
/*  40 */             (RequiredArgumentBuilder)Commands.argument("from", (ArgumentType)ColumnPosArgument.columnPos())
/*  41 */             .executes(debug0 -> changeForceLoad((CommandSourceStack)debug0.getSource(), ColumnPosArgument.getColumnPos(debug0, "from"), ColumnPosArgument.getColumnPos(debug0, "from"), true)))
/*  42 */             .then(
/*  43 */               Commands.argument("to", (ArgumentType)ColumnPosArgument.columnPos())
/*  44 */               .executes(debug0 -> changeForceLoad((CommandSourceStack)debug0.getSource(), ColumnPosArgument.getColumnPos(debug0, "from"), ColumnPosArgument.getColumnPos(debug0, "to"), true))))))
/*     */         
/*  46 */         .then((
/*  47 */           (LiteralArgumentBuilder)Commands.literal("remove")
/*  48 */           .then((
/*  49 */             (RequiredArgumentBuilder)Commands.argument("from", (ArgumentType)ColumnPosArgument.columnPos())
/*  50 */             .executes(debug0 -> changeForceLoad((CommandSourceStack)debug0.getSource(), ColumnPosArgument.getColumnPos(debug0, "from"), ColumnPosArgument.getColumnPos(debug0, "from"), false)))
/*  51 */             .then(
/*  52 */               Commands.argument("to", (ArgumentType)ColumnPosArgument.columnPos())
/*  53 */               .executes(debug0 -> changeForceLoad((CommandSourceStack)debug0.getSource(), ColumnPosArgument.getColumnPos(debug0, "from"), ColumnPosArgument.getColumnPos(debug0, "to"), false)))))
/*  54 */           .then(
/*  55 */             Commands.literal("all")
/*  56 */             .executes(debug0 -> removeAll((CommandSourceStack)debug0.getSource())))))
/*     */ 
/*     */         
/*  59 */         .then((
/*  60 */           (LiteralArgumentBuilder)Commands.literal("query")
/*  61 */           .executes(debug0 -> listForceLoad((CommandSourceStack)debug0.getSource())))
/*  62 */           .then(
/*  63 */             Commands.argument("pos", (ArgumentType)ColumnPosArgument.columnPos())
/*  64 */             .executes(debug0 -> queryForceLoad((CommandSourceStack)debug0.getSource(), ColumnPosArgument.getColumnPos(debug0, "pos"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int queryForceLoad(CommandSourceStack debug0, ColumnPos debug1) throws CommandSyntaxException {
/*  71 */     ChunkPos debug2 = new ChunkPos(debug1.x >> 4, debug1.z >> 4);
/*  72 */     ServerLevel debug3 = debug0.getLevel();
/*  73 */     ResourceKey<Level> debug4 = debug3.dimension();
/*  74 */     boolean debug5 = debug3.getForcedChunks().contains(debug2.toLong());
/*     */     
/*  76 */     if (debug5) {
/*  77 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.forceload.query.success", new Object[] { debug2, debug4.location() }), false);
/*  78 */       return 1;
/*     */     } 
/*  80 */     throw ERROR_NOT_TICKING.create(debug2, debug4.location());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int listForceLoad(CommandSourceStack debug0) {
/*  85 */     ServerLevel debug1 = debug0.getLevel();
/*  86 */     ResourceKey<Level> debug2 = debug1.dimension();
/*  87 */     LongSet debug3 = debug1.getForcedChunks();
/*  88 */     int debug4 = debug3.size();
/*     */     
/*  90 */     if (debug4 > 0) {
/*  91 */       String debug5 = Joiner.on(", ").join(debug3.stream().sorted().map(ChunkPos::new).map(ChunkPos::toString).iterator());
/*     */       
/*  93 */       if (debug4 == 1) {
/*  94 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.forceload.list.single", new Object[] { debug2.location(), debug5 }), false);
/*     */       } else {
/*  96 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.forceload.list.multiple", new Object[] { Integer.valueOf(debug4), debug2.location(), debug5 }), false);
/*     */       } 
/*     */     } else {
/*  99 */       debug0.sendFailure((Component)new TranslatableComponent("commands.forceload.added.none", new Object[] { debug2.location() }));
/*     */     } 
/* 101 */     return debug4;
/*     */   }
/*     */   
/*     */   private static int removeAll(CommandSourceStack debug0) {
/* 105 */     ServerLevel debug1 = debug0.getLevel();
/* 106 */     ResourceKey<Level> debug2 = debug1.dimension();
/* 107 */     LongSet debug3 = debug1.getForcedChunks();
/* 108 */     debug3.forEach(debug1 -> debug0.setChunkForced(ChunkPos.getX(debug1), ChunkPos.getZ(debug1), false));
/* 109 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.forceload.removed.all", new Object[] { debug2.location() }), true);
/* 110 */     return 0;
/*     */   }
/*     */   
/*     */   private static int changeForceLoad(CommandSourceStack debug0, ColumnPos debug1, ColumnPos debug2, boolean debug3) throws CommandSyntaxException {
/* 114 */     int debug4 = Math.min(debug1.x, debug2.x);
/* 115 */     int debug5 = Math.min(debug1.z, debug2.z);
/* 116 */     int debug6 = Math.max(debug1.x, debug2.x);
/* 117 */     int debug7 = Math.max(debug1.z, debug2.z);
/*     */     
/* 119 */     if (debug4 < -30000000 || debug5 < -30000000 || debug6 >= 30000000 || debug7 >= 30000000)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 124 */       throw BlockPosArgument.ERROR_OUT_OF_WORLD.create();
/*     */     }
/*     */     
/* 127 */     int debug8 = debug4 >> 4;
/* 128 */     int debug9 = debug5 >> 4;
/* 129 */     int debug10 = debug6 >> 4;
/* 130 */     int debug11 = debug7 >> 4;
/*     */     
/* 132 */     long debug12 = ((debug10 - debug8) + 1L) * ((debug11 - debug9) + 1L);
/*     */     
/* 134 */     if (debug12 > 256L) {
/* 135 */       throw ERROR_TOO_MANY_CHUNKS.create(Integer.valueOf(256), Long.valueOf(debug12));
/*     */     }
/*     */     
/* 138 */     ServerLevel debug14 = debug0.getLevel();
/* 139 */     ResourceKey<Level> debug15 = debug14.dimension();
/*     */     
/* 141 */     ChunkPos debug16 = null;
/* 142 */     int debug17 = 0;
/* 143 */     for (int debug18 = debug8; debug18 <= debug10; debug18++) {
/* 144 */       for (int debug19 = debug9; debug19 <= debug11; debug19++) {
/* 145 */         boolean debug20 = debug14.setChunkForced(debug18, debug19, debug3);
/* 146 */         if (debug20) {
/* 147 */           debug17++;
/* 148 */           if (debug16 == null) {
/* 149 */             debug16 = new ChunkPos(debug18, debug19);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     if (debug17 == 0)
/* 156 */       throw (debug3 ? ERROR_ALL_ADDED : ERROR_NONE_REMOVED).create(); 
/* 157 */     if (debug17 == 1) {
/* 158 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.forceload." + (debug3 ? "added" : "removed") + ".single", new Object[] { debug16, debug15.location() }), true);
/*     */     } else {
/* 160 */       ChunkPos chunkPos1 = new ChunkPos(debug8, debug9);
/* 161 */       ChunkPos debug19 = new ChunkPos(debug10, debug11);
/* 162 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.forceload." + (debug3 ? "added" : "removed") + ".multiple", new Object[] { Integer.valueOf(debug17), debug15.location(), chunkPos1, debug19 }), true);
/*     */     } 
/*     */     
/* 165 */     return debug17;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ForceLoadCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */