/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementProgress;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class ClientboundUpdateAdvancementsPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private boolean reset;
/*    */   private Map<ResourceLocation, Advancement.Builder> added;
/*    */   private Set<ResourceLocation> removed;
/*    */   private Map<ResourceLocation, AdvancementProgress> progress;
/*    */   
/*    */   public ClientboundUpdateAdvancementsPacket() {}
/*    */   
/*    */   public ClientboundUpdateAdvancementsPacket(boolean debug1, Collection<Advancement> debug2, Set<ResourceLocation> debug3, Map<ResourceLocation, AdvancementProgress> debug4) {
/* 26 */     this.reset = debug1;
/* 27 */     this.added = Maps.newHashMap();
/* 28 */     for (Advancement debug6 : debug2) {
/* 29 */       this.added.put(debug6.getId(), debug6.deconstruct());
/*    */     }
/* 31 */     this.removed = debug3;
/* 32 */     this.progress = Maps.newHashMap(debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 37 */     debug1.handleUpdateAdvancementsPacket(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 42 */     this.reset = debug1.readBoolean();
/* 43 */     this.added = Maps.newHashMap();
/* 44 */     this.removed = Sets.newLinkedHashSet();
/* 45 */     this.progress = Maps.newHashMap();
/*    */     
/* 47 */     int debug2 = debug1.readVarInt(); int debug3;
/* 48 */     for (debug3 = 0; debug3 < debug2; debug3++) {
/* 49 */       ResourceLocation debug4 = debug1.readResourceLocation();
/* 50 */       Advancement.Builder debug5 = Advancement.Builder.fromNetwork(debug1);
/* 51 */       this.added.put(debug4, debug5);
/*    */     } 
/*    */     
/* 54 */     debug2 = debug1.readVarInt();
/* 55 */     for (debug3 = 0; debug3 < debug2; debug3++) {
/* 56 */       ResourceLocation debug4 = debug1.readResourceLocation();
/* 57 */       this.removed.add(debug4);
/*    */     } 
/*    */     
/* 60 */     debug2 = debug1.readVarInt();
/* 61 */     for (debug3 = 0; debug3 < debug2; debug3++) {
/* 62 */       ResourceLocation debug4 = debug1.readResourceLocation();
/* 63 */       this.progress.put(debug4, AdvancementProgress.fromNetwork(debug1));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 69 */     debug1.writeBoolean(this.reset);
/*    */     
/* 71 */     debug1.writeVarInt(this.added.size());
/* 72 */     for (Map.Entry<ResourceLocation, Advancement.Builder> debug3 : this.added.entrySet()) {
/* 73 */       ResourceLocation debug4 = debug3.getKey();
/* 74 */       Advancement.Builder debug5 = debug3.getValue();
/* 75 */       debug1.writeResourceLocation(debug4);
/* 76 */       debug5.serializeToNetwork(debug1);
/*    */     } 
/*    */     
/* 79 */     debug1.writeVarInt(this.removed.size());
/* 80 */     for (ResourceLocation debug3 : this.removed) {
/* 81 */       debug1.writeResourceLocation(debug3);
/*    */     }
/*    */     
/* 84 */     debug1.writeVarInt(this.progress.size());
/* 85 */     for (Map.Entry<ResourceLocation, AdvancementProgress> debug3 : this.progress.entrySet()) {
/* 86 */       debug1.writeResourceLocation(debug3.getKey());
/* 87 */       ((AdvancementProgress)debug3.getValue()).serializeToNetwork(debug1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateAdvancementsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */