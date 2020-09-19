/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class DemoMode
/*     */   extends ServerPlayerGameMode
/*     */ {
/*     */   private boolean displayedIntro;
/*     */   private boolean demoHasEnded;
/*     */   private int demoEndedReminder;
/*     */   private int gameModeTicks;
/*     */   
/*     */   public DemoMode(ServerLevel debug1) {
/*  26 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  31 */     super.tick();
/*  32 */     this.gameModeTicks++;
/*     */     
/*  34 */     long debug1 = this.level.getGameTime();
/*  35 */     long debug3 = debug1 / 24000L + 1L;
/*     */     
/*  37 */     if (!this.displayedIntro && this.gameModeTicks > 20) {
/*  38 */       this.displayedIntro = true;
/*  39 */       this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0.0F));
/*     */     } 
/*     */     
/*  42 */     this.demoHasEnded = (debug1 > 120500L);
/*  43 */     if (this.demoHasEnded) {
/*  44 */       this.demoEndedReminder++;
/*     */     }
/*     */     
/*  47 */     if (debug1 % 24000L == 500L) {
/*  48 */       if (debug3 <= 6L) {
/*  49 */         if (debug3 == 6L) {
/*  50 */           this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 104.0F));
/*     */         } else {
/*  52 */           this.player.sendMessage((Component)new TranslatableComponent("demo.day." + debug3), Util.NIL_UUID);
/*     */         } 
/*     */       }
/*  55 */     } else if (debug3 == 1L) {
/*  56 */       if (debug1 == 100L) {
/*  57 */         this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 101.0F));
/*  58 */       } else if (debug1 == 175L) {
/*  59 */         this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 102.0F));
/*  60 */       } else if (debug1 == 250L) {
/*  61 */         this.player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 103.0F));
/*     */       } 
/*  63 */     } else if (debug3 == 5L && 
/*  64 */       debug1 % 24000L == 22000L) {
/*  65 */       this.player.sendMessage((Component)new TranslatableComponent("demo.day.warning"), Util.NIL_UUID);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void outputDemoReminder() {
/*  71 */     if (this.demoEndedReminder > 100) {
/*  72 */       this.player.sendMessage((Component)new TranslatableComponent("demo.reminder"), Util.NIL_UUID);
/*  73 */       this.demoEndedReminder = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos debug1, ServerboundPlayerActionPacket.Action debug2, Direction debug3, int debug4) {
/*  79 */     if (this.demoHasEnded) {
/*  80 */       outputDemoReminder();
/*     */       return;
/*     */     } 
/*  83 */     super.handleBlockBreakAction(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useItem(ServerPlayer debug1, Level debug2, ItemStack debug3, InteractionHand debug4) {
/*  88 */     if (this.demoHasEnded) {
/*  89 */       outputDemoReminder();
/*  90 */       return InteractionResult.PASS;
/*     */     } 
/*  92 */     return super.useItem(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useItemOn(ServerPlayer debug1, Level debug2, ItemStack debug3, InteractionHand debug4, BlockHitResult debug5) {
/*  97 */     if (this.demoHasEnded) {
/*  98 */       outputDemoReminder();
/*  99 */       return InteractionResult.PASS;
/*     */     } 
/* 101 */     return super.useItemOn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\DemoMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */