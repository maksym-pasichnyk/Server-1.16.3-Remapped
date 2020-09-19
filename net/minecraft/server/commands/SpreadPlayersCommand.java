/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ public class SpreadPlayersCommand
/*     */ {
/*     */   private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_TEAMS;
/*     */   private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_ENTITIES;
/*     */   
/*     */   static {
/*  45 */     ERROR_FAILED_TO_SPREAD_TEAMS = new Dynamic4CommandExceptionType((debug0, debug1, debug2, debug3) -> new TranslatableComponent("commands.spreadplayers.failed.teams", new Object[] { debug0, debug1, debug2, debug3 }));
/*  46 */     ERROR_FAILED_TO_SPREAD_ENTITIES = new Dynamic4CommandExceptionType((debug0, debug1, debug2, debug3) -> new TranslatableComponent("commands.spreadplayers.failed.entities", new Object[] { debug0, debug1, debug2, debug3 }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  49 */     debug0.register(
/*  50 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spreadplayers")
/*  51 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  52 */         .then(
/*  53 */           Commands.argument("center", (ArgumentType)Vec2Argument.vec2())
/*  54 */           .then(
/*  55 */             Commands.argument("spreadDistance", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  56 */             .then((
/*  57 */               (RequiredArgumentBuilder)Commands.argument("maxRange", (ArgumentType)FloatArgumentType.floatArg(1.0F))
/*  58 */               .then(
/*  59 */                 Commands.argument("respectTeams", (ArgumentType)BoolArgumentType.bool())
/*  60 */                 .then(
/*  61 */                   Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  62 */                   .executes(debug0 -> spreadPlayers((CommandSourceStack)debug0.getSource(), Vec2Argument.getVec2(debug0, "center"), FloatArgumentType.getFloat(debug0, "spreadDistance"), FloatArgumentType.getFloat(debug0, "maxRange"), 256, BoolArgumentType.getBool(debug0, "respectTeams"), EntityArgument.getEntities(debug0, "targets"))))))
/*     */ 
/*     */               
/*  65 */               .then(
/*  66 */                 Commands.literal("under")
/*  67 */                 .then(
/*  68 */                   Commands.argument("maxHeight", (ArgumentType)IntegerArgumentType.integer(0))
/*  69 */                   .then(
/*  70 */                     Commands.argument("respectTeams", (ArgumentType)BoolArgumentType.bool())
/*  71 */                     .then(
/*  72 */                       Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  73 */                       .executes(debug0 -> spreadPlayers((CommandSourceStack)debug0.getSource(), Vec2Argument.getVec2(debug0, "center"), FloatArgumentType.getFloat(debug0, "spreadDistance"), FloatArgumentType.getFloat(debug0, "maxRange"), IntegerArgumentType.getInteger(debug0, "maxHeight"), BoolArgumentType.getBool(debug0, "respectTeams"), EntityArgument.getEntities(debug0, "targets")))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int spreadPlayers(CommandSourceStack debug0, Vec2 debug1, float debug2, float debug3, int debug4, boolean debug5, Collection<? extends Entity> debug6) throws CommandSyntaxException {
/*  85 */     Random debug7 = new Random();
/*  86 */     double debug8 = (debug1.x - debug3);
/*  87 */     double debug10 = (debug1.y - debug3);
/*  88 */     double debug12 = (debug1.x + debug3);
/*  89 */     double debug14 = (debug1.y + debug3);
/*     */     
/*  91 */     Position[] debug16 = createInitialPositions(debug7, debug5 ? getNumberOfTeams(debug6) : debug6.size(), debug8, debug10, debug12, debug14);
/*  92 */     spreadPositions(debug1, debug2, debug0.getLevel(), debug7, debug8, debug10, debug12, debug14, debug4, debug16, debug5);
/*  93 */     double debug17 = setPlayerPositions(debug6, debug0.getLevel(), debug16, debug4, debug5);
/*     */     
/*  95 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.spreadplayers.success." + (debug5 ? "teams" : "entities"), new Object[] { Integer.valueOf(debug16.length), Float.valueOf(debug1.x), Float.valueOf(debug1.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug17) }) }), true);
/*  96 */     return debug16.length;
/*     */   }
/*     */   
/*     */   private static int getNumberOfTeams(Collection<? extends Entity> debug0) {
/* 100 */     Set<Team> debug1 = Sets.newHashSet();
/*     */     
/* 102 */     for (Entity debug3 : debug0) {
/* 103 */       if (debug3 instanceof net.minecraft.world.entity.player.Player) {
/* 104 */         debug1.add(debug3.getTeam()); continue;
/*     */       } 
/* 106 */       debug1.add(null);
/*     */     } 
/*     */ 
/*     */     
/* 110 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static void spreadPositions(Vec2 debug0, double debug1, ServerLevel debug3, Random debug4, double debug5, double debug7, double debug9, double debug11, int debug13, Position[] debug14, boolean debug15) throws CommandSyntaxException {
/* 114 */     boolean debug16 = true;
/*     */     
/* 116 */     double debug18 = 3.4028234663852886E38D;
/*     */     int debug17;
/* 118 */     for (debug17 = 0; debug17 < 10000 && debug16; debug17++) {
/* 119 */       debug16 = false;
/* 120 */       debug18 = 3.4028234663852886E38D;
/*     */       
/* 122 */       for (int debug20 = 0; debug20 < debug14.length; debug20++) {
/* 123 */         Position debug21 = debug14[debug20];
/* 124 */         int debug22 = 0;
/* 125 */         Position debug23 = new Position();
/*     */         
/* 127 */         for (int debug24 = 0; debug24 < debug14.length; debug24++) {
/* 128 */           if (debug20 != debug24) {
/*     */ 
/*     */             
/* 131 */             Position debug25 = debug14[debug24];
/*     */             
/* 133 */             double debug26 = debug21.dist(debug25);
/* 134 */             debug18 = Math.min(debug26, debug18);
/* 135 */             if (debug26 < debug1) {
/* 136 */               debug22++;
/* 137 */               debug23.x = debug23.x + debug25.x - debug21.x;
/* 138 */               debug23.z = debug23.z + debug25.z - debug21.z;
/*     */             } 
/*     */           } 
/*     */         } 
/* 142 */         if (debug22 > 0) {
/* 143 */           debug23.x = debug23.x / debug22;
/* 144 */           debug23.z = debug23.z / debug22;
/* 145 */           double d = debug23.getLength();
/*     */           
/* 147 */           if (d > 0.0D) {
/* 148 */             debug23.normalize();
/*     */             
/* 150 */             debug21.moveAway(debug23);
/*     */           } else {
/* 152 */             debug21.randomize(debug4, debug5, debug7, debug9, debug11);
/*     */           } 
/*     */           
/* 155 */           debug16 = true;
/*     */         } 
/*     */         
/* 158 */         if (debug21.clamp(debug5, debug7, debug9, debug11)) {
/* 159 */           debug16 = true;
/*     */         }
/*     */       } 
/*     */       
/* 163 */       if (!debug16) {
/* 164 */         for (Position debug23 : debug14) {
/* 165 */           if (!debug23.isSafe((BlockGetter)debug3, debug13)) {
/* 166 */             debug23.randomize(debug4, debug5, debug7, debug9, debug11);
/* 167 */             debug16 = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 174 */     if (debug18 == 3.4028234663852886E38D) {
/* 175 */       debug18 = 0.0D;
/*     */     }
/*     */     
/* 178 */     if (debug17 >= 10000) {
/* 179 */       if (debug15) {
/* 180 */         throw ERROR_FAILED_TO_SPREAD_TEAMS.create(Integer.valueOf(debug14.length), Float.valueOf(debug0.x), Float.valueOf(debug0.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug18) }));
/*     */       }
/* 182 */       throw ERROR_FAILED_TO_SPREAD_ENTITIES.create(Integer.valueOf(debug14.length), Float.valueOf(debug0.x), Float.valueOf(debug0.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(debug18) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static double setPlayerPositions(Collection<? extends Entity> debug0, ServerLevel debug1, Position[] debug2, int debug3, boolean debug4) {
/* 188 */     double debug5 = 0.0D;
/* 189 */     int debug7 = 0;
/* 190 */     Map<Team, Position> debug8 = Maps.newHashMap();
/*     */     
/* 192 */     for (Entity debug10 : debug0) {
/*     */       Position debug11;
/*     */       
/* 195 */       if (debug4) {
/* 196 */         Team team = (debug10 instanceof net.minecraft.world.entity.player.Player) ? debug10.getTeam() : null;
/*     */         
/* 198 */         if (!debug8.containsKey(team)) {
/* 199 */           debug8.put(team, debug2[debug7++]);
/*     */         }
/*     */         
/* 202 */         debug11 = debug8.get(team);
/*     */       } else {
/* 204 */         debug11 = debug2[debug7++];
/*     */       } 
/*     */       
/* 207 */       debug10.teleportToWithTicket(Mth.floor(debug11.x) + 0.5D, debug11.getSpawnY((BlockGetter)debug1, debug3), Mth.floor(debug11.z) + 0.5D);
/*     */       
/* 209 */       double debug12 = Double.MAX_VALUE;
/* 210 */       for (Position debug17 : debug2) {
/* 211 */         if (debug11 != debug17) {
/*     */ 
/*     */ 
/*     */           
/* 215 */           double debug18 = debug11.dist(debug17);
/* 216 */           debug12 = Math.min(debug18, debug12);
/*     */         } 
/* 218 */       }  debug5 += debug12;
/*     */     } 
/* 220 */     if (debug0.size() < 2) {
/* 221 */       return 0.0D;
/*     */     }
/*     */     
/* 224 */     debug5 /= debug0.size();
/* 225 */     return debug5;
/*     */   }
/*     */   
/*     */   private static Position[] createInitialPositions(Random debug0, int debug1, double debug2, double debug4, double debug6, double debug8) {
/* 229 */     Position[] debug10 = new Position[debug1];
/*     */     
/* 231 */     for (int debug11 = 0; debug11 < debug10.length; debug11++) {
/* 232 */       Position debug12 = new Position();
/* 233 */       debug12.randomize(debug0, debug2, debug4, debug6, debug8);
/* 234 */       debug10[debug11] = debug12;
/*     */     } 
/*     */     
/* 237 */     return debug10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Position
/*     */   {
/*     */     private double x;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private double z;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double dist(Position debug1) {
/* 258 */       double debug2 = this.x - debug1.x;
/* 259 */       double debug4 = this.z - debug1.z;
/*     */       
/* 261 */       return Math.sqrt(debug2 * debug2 + debug4 * debug4);
/*     */     }
/*     */     
/*     */     void normalize() {
/* 265 */       double debug1 = getLength();
/* 266 */       this.x /= debug1;
/* 267 */       this.z /= debug1;
/*     */     }
/*     */     
/*     */     float getLength() {
/* 271 */       return Mth.sqrt(this.x * this.x + this.z * this.z);
/*     */     }
/*     */     
/*     */     public void moveAway(Position debug1) {
/* 275 */       this.x -= debug1.x;
/* 276 */       this.z -= debug1.z;
/*     */     }
/*     */     
/*     */     public boolean clamp(double debug1, double debug3, double debug5, double debug7) {
/* 280 */       boolean debug9 = false;
/*     */       
/* 282 */       if (this.x < debug1) {
/* 283 */         this.x = debug1;
/* 284 */         debug9 = true;
/* 285 */       } else if (this.x > debug5) {
/* 286 */         this.x = debug5;
/* 287 */         debug9 = true;
/*     */       } 
/*     */       
/* 290 */       if (this.z < debug3) {
/* 291 */         this.z = debug3;
/* 292 */         debug9 = true;
/* 293 */       } else if (this.z > debug7) {
/* 294 */         this.z = debug7;
/* 295 */         debug9 = true;
/*     */       } 
/*     */       
/* 298 */       return debug9;
/*     */     }
/*     */     
/*     */     public int getSpawnY(BlockGetter debug1, int debug2) {
/* 302 */       BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos(this.x, (debug2 + 1), this.z);
/* 303 */       boolean debug4 = debug1.getBlockState((BlockPos)debug3).isAir();
/* 304 */       debug3.move(Direction.DOWN);
/* 305 */       boolean debug5 = debug1.getBlockState((BlockPos)debug3).isAir();
/* 306 */       while (debug3.getY() > 0) {
/* 307 */         debug3.move(Direction.DOWN);
/* 308 */         boolean debug6 = debug1.getBlockState((BlockPos)debug3).isAir();
/*     */         
/* 310 */         if (!debug6 && debug5 && debug4) {
/* 311 */           return debug3.getY() + 1;
/*     */         }
/* 313 */         debug4 = debug5;
/* 314 */         debug5 = debug6;
/*     */       } 
/*     */       
/* 317 */       return debug2 + 1;
/*     */     }
/*     */     
/*     */     public boolean isSafe(BlockGetter debug1, int debug2) {
/* 321 */       BlockPos debug3 = new BlockPos(this.x, (getSpawnY(debug1, debug2) - 1), this.z);
/* 322 */       BlockState debug4 = debug1.getBlockState(debug3);
/* 323 */       Material debug5 = debug4.getMaterial();
/* 324 */       return (debug3.getY() < debug2 && !debug5.isLiquid() && debug5 != Material.FIRE);
/*     */     }
/*     */     
/*     */     public void randomize(Random debug1, double debug2, double debug4, double debug6, double debug8) {
/* 328 */       this.x = Mth.nextDouble(debug1, debug2, debug6);
/* 329 */       this.z = Mth.nextDouble(debug1, debug4, debug8);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SpreadPlayersCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */