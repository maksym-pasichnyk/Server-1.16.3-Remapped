/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ 
/*     */ public class ClientboundPlayerPositionPacket
/*     */   implements Packet<ClientGamePacketListener> {
/*     */   private double x;
/*     */   private double y;
/*     */   private double z;
/*     */   private float yRot;
/*     */   private float xRot;
/*     */   private Set<RelativeArgument> relativeArguments;
/*     */   private int id;
/*     */   
/*     */   public ClientboundPlayerPositionPacket() {}
/*     */   
/*     */   public ClientboundPlayerPositionPacket(double debug1, double debug3, double debug5, float debug7, float debug8, Set<RelativeArgument> debug9, int debug10) {
/*  23 */     this.x = debug1;
/*  24 */     this.y = debug3;
/*  25 */     this.z = debug5;
/*  26 */     this.yRot = debug7;
/*  27 */     this.xRot = debug8;
/*  28 */     this.relativeArguments = debug9;
/*  29 */     this.id = debug10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  34 */     this.x = debug1.readDouble();
/*  35 */     this.y = debug1.readDouble();
/*  36 */     this.z = debug1.readDouble();
/*  37 */     this.yRot = debug1.readFloat();
/*  38 */     this.xRot = debug1.readFloat();
/*  39 */     this.relativeArguments = RelativeArgument.unpack(debug1.readUnsignedByte());
/*  40 */     this.id = debug1.readVarInt();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  45 */     debug1.writeDouble(this.x);
/*  46 */     debug1.writeDouble(this.y);
/*  47 */     debug1.writeDouble(this.z);
/*  48 */     debug1.writeFloat(this.yRot);
/*  49 */     debug1.writeFloat(this.xRot);
/*  50 */     debug1.writeByte(RelativeArgument.pack(this.relativeArguments));
/*  51 */     debug1.writeVarInt(this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/*  56 */     debug1.handleMovePlayer(this);
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
/*     */   public enum RelativeArgument
/*     */   {
/*  88 */     X(0),
/*  89 */     Y(1),
/*  90 */     Z(2),
/*  91 */     Y_ROT(3),
/*  92 */     X_ROT(4);
/*     */     
/*     */     private final int bit;
/*     */ 
/*     */     
/*     */     RelativeArgument(int debug3) {
/*  98 */       this.bit = debug3;
/*     */     }
/*     */     
/*     */     private int getMask() {
/* 102 */       return 1 << this.bit;
/*     */     }
/*     */     
/*     */     private boolean isSet(int debug1) {
/* 106 */       return ((debug1 & getMask()) == getMask());
/*     */     }
/*     */     
/*     */     public static Set<RelativeArgument> unpack(int debug0) {
/* 110 */       Set<RelativeArgument> debug1 = EnumSet.noneOf(RelativeArgument.class);
/*     */       
/* 112 */       for (RelativeArgument debug5 : values()) {
/* 113 */         if (debug5.isSet(debug0)) {
/* 114 */           debug1.add(debug5);
/*     */         }
/*     */       } 
/*     */       
/* 118 */       return debug1;
/*     */     }
/*     */     
/*     */     public static int pack(Set<RelativeArgument> debug0) {
/* 122 */       int debug1 = 0;
/*     */       
/* 124 */       for (RelativeArgument debug3 : debug0) {
/* 125 */         debug1 |= debug3.getMask();
/*     */       }
/*     */       
/* 128 */       return debug1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerPositionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */