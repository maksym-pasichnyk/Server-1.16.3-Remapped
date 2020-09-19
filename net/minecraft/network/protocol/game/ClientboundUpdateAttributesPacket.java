/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ public class ClientboundUpdateAttributesPacket implements Packet<ClientGamePacketListener> {
/*     */   private int entityId;
/*  19 */   private final List<AttributeSnapshot> attributes = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientboundUpdateAttributesPacket(int debug1, Collection<AttributeInstance> debug2) {
/*  25 */     this.entityId = debug1;
/*     */     
/*  27 */     for (AttributeInstance debug4 : debug2) {
/*  28 */       this.attributes.add(new AttributeSnapshot(debug4.getAttribute(), debug4.getBaseValue(), debug4.getModifiers()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  34 */     this.entityId = debug1.readVarInt();
/*     */     
/*  36 */     int debug2 = debug1.readInt();
/*  37 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  38 */       ResourceLocation debug4 = debug1.readResourceLocation();
/*  39 */       Attribute debug5 = (Attribute)Registry.ATTRIBUTE.get(debug4);
/*  40 */       double debug6 = debug1.readDouble();
/*  41 */       List<AttributeModifier> debug8 = Lists.newArrayList();
/*  42 */       int debug9 = debug1.readVarInt();
/*     */       
/*  44 */       for (int debug10 = 0; debug10 < debug9; debug10++) {
/*  45 */         UUID debug11 = debug1.readUUID();
/*  46 */         debug8.add(new AttributeModifier(debug11, "Unknown synced attribute modifier", debug1.readDouble(), AttributeModifier.Operation.fromValue(debug1.readByte())));
/*     */       } 
/*     */       
/*  49 */       this.attributes.add(new AttributeSnapshot(debug5, debug6, debug8));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  55 */     debug1.writeVarInt(this.entityId);
/*  56 */     debug1.writeInt(this.attributes.size());
/*     */     
/*  58 */     for (AttributeSnapshot debug3 : this.attributes) {
/*  59 */       debug1.writeResourceLocation(Registry.ATTRIBUTE.getKey(debug3.getAttribute()));
/*  60 */       debug1.writeDouble(debug3.getBase());
/*  61 */       debug1.writeVarInt(debug3.getModifiers().size());
/*     */       
/*  63 */       for (AttributeModifier debug5 : debug3.getModifiers()) {
/*  64 */         debug1.writeUUID(debug5.getId());
/*  65 */         debug1.writeDouble(debug5.getAmount());
/*  66 */         debug1.writeByte(debug5.getOperation().toValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/*  73 */     debug1.handleUpdateAttributes(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundUpdateAttributesPacket() {}
/*     */ 
/*     */   
/*     */   public class AttributeSnapshot
/*     */   {
/*     */     private final Attribute attribute;
/*     */     
/*     */     private final double base;
/*     */     
/*     */     private final Collection<AttributeModifier> modifiers;
/*     */ 
/*     */     
/*     */     public AttributeSnapshot(Attribute debug2, double debug3, Collection<AttributeModifier> debug5) {
/*  90 */       this.attribute = debug2;
/*  91 */       this.base = debug3;
/*  92 */       this.modifiers = debug5;
/*     */     }
/*     */     
/*     */     public Attribute getAttribute() {
/*  96 */       return this.attribute;
/*     */     }
/*     */     
/*     */     public double getBase() {
/* 100 */       return this.base;
/*     */     }
/*     */     
/*     */     public Collection<AttributeModifier> getModifiers() {
/* 104 */       return this.modifiers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateAttributesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */