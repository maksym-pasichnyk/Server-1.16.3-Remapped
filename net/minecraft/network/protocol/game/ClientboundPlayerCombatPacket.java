/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.damagesource.CombatTracker;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class ClientboundPlayerCombatPacket implements Packet<ClientGamePacketListener> {
/*    */   public Event event;
/*    */   public int playerId;
/*    */   public int killerId;
/*    */   public int duration;
/*    */   public Component message;
/*    */   
/*    */   public enum Event {
/* 14 */     ENTER_COMBAT,
/* 15 */     END_COMBAT,
/* 16 */     ENTITY_DIED;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundPlayerCombatPacket() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundPlayerCombatPacket(CombatTracker debug1, Event debug2) {
/* 30 */     this(debug1, debug2, TextComponent.EMPTY);
/*    */   }
/*    */   
/*    */   public ClientboundPlayerCombatPacket(CombatTracker debug1, Event debug2, Component debug3) {
/* 34 */     this.event = debug2;
/*    */     
/* 36 */     LivingEntity debug4 = debug1.getKiller();
/*    */     
/* 38 */     switch (debug2) {
/*    */       case END_COMBAT:
/* 40 */         this.duration = debug1.getCombatDuration();
/* 41 */         this.killerId = (debug4 == null) ? -1 : debug4.getId();
/*    */         break;
/*    */       case ENTITY_DIED:
/* 44 */         this.playerId = debug1.getMob().getId();
/* 45 */         this.killerId = (debug4 == null) ? -1 : debug4.getId();
/* 46 */         this.message = debug3;
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 53 */     this.event = (Event)debug1.readEnum(Event.class);
/*    */     
/* 55 */     if (this.event == Event.END_COMBAT) {
/* 56 */       this.duration = debug1.readVarInt();
/* 57 */       this.killerId = debug1.readInt();
/* 58 */     } else if (this.event == Event.ENTITY_DIED) {
/* 59 */       this.playerId = debug1.readVarInt();
/* 60 */       this.killerId = debug1.readInt();
/* 61 */       this.message = debug1.readComponent();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 67 */     debug1.writeEnum(this.event);
/*    */     
/* 69 */     if (this.event == Event.END_COMBAT) {
/* 70 */       debug1.writeVarInt(this.duration);
/* 71 */       debug1.writeInt(this.killerId);
/* 72 */     } else if (this.event == Event.ENTITY_DIED) {
/* 73 */       debug1.writeVarInt(this.playerId);
/* 74 */       debug1.writeInt(this.killerId);
/* 75 */       debug1.writeComponent(this.message);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 81 */     debug1.handlePlayerCombat(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSkippable() {
/* 86 */     return (this.event == Event.ENTITY_DIED);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerCombatPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */