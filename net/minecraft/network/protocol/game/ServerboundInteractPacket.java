/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerboundInteractPacket
/*     */   implements Packet<ServerGamePacketListener>
/*     */ {
/*     */   private int entityId;
/*     */   private Action action;
/*     */   private Vec3 location;
/*     */   private InteractionHand hand;
/*     */   private boolean usingSecondaryAction;
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  46 */     this.entityId = debug1.readVarInt();
/*  47 */     this.action = (Action)debug1.readEnum(Action.class);
/*  48 */     if (this.action == Action.INTERACT_AT) {
/*  49 */       this.location = new Vec3(debug1.readFloat(), debug1.readFloat(), debug1.readFloat());
/*     */     }
/*  51 */     if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT) {
/*  52 */       this.hand = (InteractionHand)debug1.readEnum(InteractionHand.class);
/*     */     }
/*  54 */     this.usingSecondaryAction = debug1.readBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  59 */     debug1.writeVarInt(this.entityId);
/*  60 */     debug1.writeEnum(this.action);
/*  61 */     if (this.action == Action.INTERACT_AT) {
/*  62 */       debug1.writeFloat((float)this.location.x);
/*  63 */       debug1.writeFloat((float)this.location.y);
/*  64 */       debug1.writeFloat((float)this.location.z);
/*     */     } 
/*  66 */     if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT) {
/*  67 */       debug1.writeEnum((Enum)this.hand);
/*     */     }
/*  69 */     debug1.writeBoolean(this.usingSecondaryAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ServerGamePacketListener debug1) {
/*  74 */     debug1.handleInteract(this);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Entity getTarget(Level debug1) {
/*  79 */     return debug1.getEntity(this.entityId);
/*     */   }
/*     */   
/*     */   public Action getAction() {
/*  83 */     return this.action;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public InteractionHand getHand() {
/*  88 */     return this.hand;
/*     */   }
/*     */   
/*     */   public Vec3 getLocation() {
/*  92 */     return this.location;
/*     */   }
/*     */   
/*     */   public boolean isUsingSecondaryAction() {
/*  96 */     return this.usingSecondaryAction;
/*     */   }
/*     */   
/*     */   public enum Action {
/* 100 */     INTERACT,
/* 101 */     ATTACK,
/* 102 */     INTERACT_AT;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */