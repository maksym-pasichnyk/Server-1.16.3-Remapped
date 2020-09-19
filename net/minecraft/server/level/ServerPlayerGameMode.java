/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockBreakAckPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerPlayerGameMode
/*     */ {
/*  31 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public ServerLevel level;
/*     */   
/*     */   public ServerPlayer player;
/*  36 */   private GameType gameModeForPlayer = GameType.NOT_SET;
/*  37 */   private GameType previousGameModeForPlayer = GameType.NOT_SET;
/*     */   
/*     */   private boolean isDestroyingBlock;
/*     */   private int destroyProgressStart;
/*  41 */   private BlockPos destroyPos = BlockPos.ZERO;
/*     */   
/*     */   private int gameTicks;
/*     */   private boolean hasDelayedDestroy;
/*  45 */   private BlockPos delayedDestroyPos = BlockPos.ZERO;
/*     */   private int delayedTickStart;
/*  47 */   private int lastSentState = -1;
/*     */   
/*     */   public ServerPlayerGameMode(ServerLevel debug1) {
/*  50 */     this.level = debug1;
/*     */   }
/*     */   
/*     */   public void setGameModeForPlayer(GameType debug1) {
/*  54 */     setGameModeForPlayer(debug1, (debug1 != this.gameModeForPlayer) ? this.gameModeForPlayer : this.previousGameModeForPlayer);
/*     */   }
/*     */   
/*     */   public void setGameModeForPlayer(GameType debug1, GameType debug2) {
/*  58 */     this.previousGameModeForPlayer = debug2;
/*  59 */     this.gameModeForPlayer = debug1;
/*     */     
/*  61 */     debug1.updatePlayerAbilities(this.player.abilities);
/*     */     
/*  63 */     this.player.onUpdateAbilities();
/*  64 */     this.player.server.getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_GAME_MODE, new ServerPlayer[] { this.player }));
/*  65 */     this.level.updateSleepingPlayerList();
/*     */   }
/*     */   
/*     */   public GameType getGameModeForPlayer() {
/*  69 */     return this.gameModeForPlayer;
/*     */   }
/*     */   
/*     */   public GameType getPreviousGameModeForPlayer() {
/*  73 */     return this.previousGameModeForPlayer;
/*     */   }
/*     */   
/*     */   public boolean isSurvival() {
/*  77 */     return this.gameModeForPlayer.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean isCreative() {
/*  81 */     return this.gameModeForPlayer.isCreative();
/*     */   }
/*     */   
/*     */   public void updateGameMode(GameType debug1) {
/*  85 */     if (this.gameModeForPlayer == GameType.NOT_SET) {
/*  86 */       this.gameModeForPlayer = debug1;
/*     */     }
/*  88 */     setGameModeForPlayer(this.gameModeForPlayer);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  92 */     this.gameTicks++;
/*     */     
/*  94 */     if (this.hasDelayedDestroy) {
/*  95 */       BlockState debug1 = this.level.getBlockState(this.delayedDestroyPos);
/*  96 */       if (debug1.isAir()) {
/*  97 */         this.hasDelayedDestroy = false;
/*     */       } else {
/*  99 */         float debug2 = incrementDestroyProgress(debug1, this.delayedDestroyPos, this.delayedTickStart);
/*     */         
/* 101 */         if (debug2 >= 1.0F) {
/* 102 */           this.hasDelayedDestroy = false;
/* 103 */           destroyBlock(this.delayedDestroyPos);
/*     */         } 
/*     */       } 
/* 106 */     } else if (this.isDestroyingBlock) {
/* 107 */       BlockState debug1 = this.level.getBlockState(this.destroyPos);
/*     */       
/* 109 */       if (debug1.isAir()) {
/* 110 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 111 */         this.lastSentState = -1;
/* 112 */         this.isDestroyingBlock = false;
/*     */       } else {
/* 114 */         incrementDestroyProgress(debug1, this.destroyPos, this.destroyProgressStart);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float incrementDestroyProgress(BlockState debug1, BlockPos debug2, int debug3) {
/* 120 */     int debug4 = this.gameTicks - debug3;
/* 121 */     float debug5 = debug1.getDestroyProgress(this.player, (BlockGetter)this.player.level, debug2) * (debug4 + 1);
/* 122 */     int debug6 = (int)(debug5 * 10.0F);
/*     */     
/* 124 */     if (debug6 != this.lastSentState) {
/* 125 */       this.level.destroyBlockProgress(this.player.getId(), debug2, debug6);
/* 126 */       this.lastSentState = debug6;
/*     */     } 
/* 128 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos debug1, ServerboundPlayerActionPacket.Action debug2, Direction debug3, int debug4) {
/* 133 */     double debug5 = this.player.getX() - debug1.getX() + 0.5D;
/*     */ 
/*     */ 
/*     */     
/* 137 */     double debug7 = this.player.getY() - debug1.getY() + 0.5D + 1.5D;
/* 138 */     double debug9 = this.player.getZ() - debug1.getZ() + 0.5D;
/* 139 */     double debug11 = debug5 * debug5 + debug7 * debug7 + debug9 * debug9;
/* 140 */     if (debug11 > 36.0D) {
/* 141 */       this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, false, "too far"));
/*     */       return;
/*     */     } 
/* 144 */     if (debug1.getY() >= debug4) {
/* 145 */       this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, false, "too high"));
/*     */       
/*     */       return;
/*     */     } 
/* 149 */     if (debug2 == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
/* 150 */       if (!this.level.mayInteract(this.player, debug1)) {
/* 151 */         this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, false, "may not interact"));
/*     */         return;
/*     */       } 
/* 154 */       if (isCreative()) {
/* 155 */         destroyAndAck(debug1, debug2, "creative destroy");
/*     */         
/*     */         return;
/*     */       } 
/* 159 */       if (this.player.blockActionRestricted(this.level, debug1, this.gameModeForPlayer)) {
/* 160 */         this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, false, "block action restricted"));
/*     */         
/*     */         return;
/*     */       } 
/* 164 */       this.destroyProgressStart = this.gameTicks;
/* 165 */       float debug13 = 1.0F;
/* 166 */       BlockState debug14 = this.level.getBlockState(debug1);
/* 167 */       if (!debug14.isAir()) {
/* 168 */         debug14.attack(this.level, debug1, this.player);
/* 169 */         debug13 = debug14.getDestroyProgress(this.player, (BlockGetter)this.player.level, debug1);
/*     */       } 
/*     */       
/* 172 */       if (!debug14.isAir() && debug13 >= 1.0F) {
/* 173 */         destroyAndAck(debug1, debug2, "insta mine");
/*     */       } else {
/* 175 */         if (this.isDestroyingBlock) {
/* 176 */           this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(this.destroyPos, this.level.getBlockState(this.destroyPos), ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)"));
/*     */         }
/* 178 */         this.isDestroyingBlock = true;
/* 179 */         this.destroyPos = debug1.immutable();
/*     */         
/* 181 */         int debug15 = (int)(debug13 * 10.0F);
/* 182 */         this.level.destroyBlockProgress(this.player.getId(), debug1, debug15);
/* 183 */         this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, true, "actual start of destroying"));
/* 184 */         this.lastSentState = debug15;
/*     */       } 
/* 186 */     } else if (debug2 == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
/*     */       
/* 188 */       if (debug1.equals(this.destroyPos)) {
/* 189 */         int debug13 = this.gameTicks - this.destroyProgressStart;
/*     */         
/* 191 */         BlockState debug14 = this.level.getBlockState(debug1);
/* 192 */         if (!debug14.isAir()) {
/* 193 */           float debug15 = debug14.getDestroyProgress(this.player, (BlockGetter)this.player.level, debug1) * (debug13 + 1);
/* 194 */           if (debug15 >= 0.7F) {
/* 195 */             this.isDestroyingBlock = false;
/* 196 */             this.level.destroyBlockProgress(this.player.getId(), debug1, -1);
/* 197 */             destroyAndAck(debug1, debug2, "destroyed"); return;
/*     */           } 
/* 199 */           if (!this.hasDelayedDestroy) {
/* 200 */             this.isDestroyingBlock = false;
/* 201 */             this.hasDelayedDestroy = true;
/* 202 */             this.delayedDestroyPos = debug1;
/* 203 */             this.delayedTickStart = this.destroyProgressStart;
/*     */           } 
/*     */         } 
/*     */       } 
/* 207 */       this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, true, "stopped destroying"));
/* 208 */     } else if (debug2 == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
/* 209 */       this.isDestroyingBlock = false;
/* 210 */       if (!Objects.equals(this.destroyPos, debug1)) {
/* 211 */         LOGGER.warn("Mismatch in destroy block pos: " + this.destroyPos + " " + debug1);
/* 212 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 213 */         this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(this.destroyPos, this.level.getBlockState(this.destroyPos), debug2, true, "aborted mismatched destroying"));
/*     */       } 
/*     */       
/* 216 */       this.level.destroyBlockProgress(this.player.getId(), debug1, -1);
/* 217 */       this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, true, "aborted destroying"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void destroyAndAck(BlockPos debug1, ServerboundPlayerActionPacket.Action debug2, String debug3) {
/* 222 */     if (destroyBlock(debug1)) {
/* 223 */       this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, true, debug3));
/*     */     } else {
/* 225 */       this.player.connection.send((Packet)new ClientboundBlockBreakAckPacket(debug1, this.level.getBlockState(debug1), debug2, false, debug3));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean destroyBlock(BlockPos debug1) {
/* 230 */     BlockState debug2 = this.level.getBlockState(debug1);
/* 231 */     if (!this.player.getMainHandItem().getItem().canAttackBlock(debug2, this.level, debug1, this.player)) {
/* 232 */       return false;
/*     */     }
/*     */     
/* 235 */     BlockEntity debug3 = this.level.getBlockEntity(debug1);
/* 236 */     Block debug4 = debug2.getBlock();
/*     */ 
/*     */     
/* 239 */     if ((debug4 instanceof net.minecraft.world.level.block.CommandBlock || debug4 instanceof net.minecraft.world.level.block.StructureBlock || debug4 instanceof net.minecraft.world.level.block.JigsawBlock) && !this.player.canUseGameMasterBlocks()) {
/* 240 */       this.level.sendBlockUpdated(debug1, debug2, debug2, 3);
/* 241 */       return false;
/*     */     } 
/*     */     
/* 244 */     if (this.player.blockActionRestricted(this.level, debug1, this.gameModeForPlayer)) {
/* 245 */       return false;
/*     */     }
/*     */     
/* 248 */     debug4.playerWillDestroy(this.level, debug1, debug2, this.player);
/*     */     
/* 250 */     boolean debug5 = this.level.removeBlock(debug1, false);
/*     */ 
/*     */ 
/*     */     
/* 254 */     if (debug5) {
/* 255 */       debug4.destroy((LevelAccessor)this.level, debug1, debug2);
/*     */     }
/*     */     
/* 258 */     if (isCreative()) {
/* 259 */       return true;
/*     */     }
/*     */     
/* 262 */     ItemStack debug6 = this.player.getMainHandItem();
/*     */     
/* 264 */     ItemStack debug7 = debug6.copy();
/* 265 */     boolean debug8 = this.player.hasCorrectToolForDrops(debug2);
/* 266 */     debug6.mineBlock(this.level, debug2, debug1, this.player);
/* 267 */     if (debug5 && debug8) {
/* 268 */       debug4.playerDestroy(this.level, this.player, debug1, debug2, debug3, debug7);
/*     */     }
/* 270 */     return true;
/*     */   }
/*     */   
/*     */   public InteractionResult useItem(ServerPlayer debug1, Level debug2, ItemStack debug3, InteractionHand debug4) {
/* 274 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 275 */       return InteractionResult.PASS;
/*     */     }
/* 277 */     if (debug1.getCooldowns().isOnCooldown(debug3.getItem())) {
/* 278 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 281 */     int debug5 = debug3.getCount();
/* 282 */     int debug6 = debug3.getDamageValue();
/* 283 */     InteractionResultHolder<ItemStack> debug7 = debug3.use(debug2, debug1, debug4);
/*     */     
/* 285 */     ItemStack debug8 = (ItemStack)debug7.getObject();
/* 286 */     if (debug8 == debug3 && debug8.getCount() == debug5 && debug8.getUseDuration() <= 0 && debug8.getDamageValue() == debug6) {
/* 287 */       return debug7.getResult();
/*     */     }
/*     */     
/* 290 */     if (debug7.getResult() == InteractionResult.FAIL && debug8.getUseDuration() > 0 && !debug1.isUsingItem()) {
/* 291 */       return debug7.getResult();
/*     */     }
/*     */     
/* 294 */     debug1.setItemInHand(debug4, debug8);
/* 295 */     if (isCreative()) {
/* 296 */       debug8.setCount(debug5);
/* 297 */       if (debug8.isDamageableItem() && debug8.getDamageValue() != debug6) {
/* 298 */         debug8.setDamageValue(debug6);
/*     */       }
/*     */     } 
/* 301 */     if (debug8.isEmpty()) {
/* 302 */       debug1.setItemInHand(debug4, ItemStack.EMPTY);
/*     */     }
/* 304 */     if (!debug1.isUsingItem()) {
/* 305 */       debug1.refreshContainer((AbstractContainerMenu)debug1.inventoryMenu);
/*     */     }
/* 307 */     return debug7.getResult();
/*     */   }
/*     */   public InteractionResult useItemOn(ServerPlayer debug1, Level debug2, ItemStack debug3, InteractionHand debug4, BlockHitResult debug5) {
/*     */     InteractionResult debug12;
/* 311 */     BlockPos debug6 = debug5.getBlockPos();
/*     */     
/* 313 */     BlockState debug7 = debug2.getBlockState(debug6);
/* 314 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 315 */       MenuProvider menuProvider = debug7.getMenuProvider(debug2, debug6);
/* 316 */       if (menuProvider != null) {
/* 317 */         debug1.openMenu(menuProvider);
/* 318 */         return InteractionResult.SUCCESS;
/*     */       } 
/* 320 */       return InteractionResult.PASS;
/*     */     } 
/*     */     
/* 323 */     boolean debug8 = (!debug1.getMainHandItem().isEmpty() || !debug1.getOffhandItem().isEmpty());
/* 324 */     boolean debug9 = (debug1.isSecondaryUseActive() && debug8);
/* 325 */     ItemStack debug10 = debug3.copy();
/*     */     
/* 327 */     if (!debug9) {
/* 328 */       InteractionResult interactionResult = debug7.use(debug2, debug1, debug4, debug5);
/* 329 */       if (interactionResult.consumesAction()) {
/* 330 */         CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(debug1, debug6, debug10);
/*     */         
/* 332 */         return interactionResult;
/*     */       } 
/*     */     } 
/*     */     
/* 336 */     if (debug3.isEmpty() || debug1.getCooldowns().isOnCooldown(debug3.getItem())) {
/* 337 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 340 */     UseOnContext debug11 = new UseOnContext(debug1, debug4, debug5);
/*     */     
/* 342 */     if (isCreative()) {
/*     */       
/* 344 */       int debug13 = debug3.getCount();
/* 345 */       debug12 = debug3.useOn(debug11);
/* 346 */       debug3.setCount(debug13);
/*     */     } else {
/* 348 */       debug12 = debug3.useOn(debug11);
/*     */     } 
/* 350 */     if (debug12.consumesAction()) {
/* 351 */       CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(debug1, debug6, debug10);
/*     */     }
/* 353 */     return debug12;
/*     */   }
/*     */   
/*     */   public void setLevel(ServerLevel debug1) {
/* 357 */     this.level = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ServerPlayerGameMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */