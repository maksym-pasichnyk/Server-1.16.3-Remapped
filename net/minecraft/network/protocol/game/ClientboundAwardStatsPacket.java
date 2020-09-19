/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.StatType;
/*    */ 
/*    */ public class ClientboundAwardStatsPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private Object2IntMap<Stat<?>> stats;
/*    */   
/*    */   public ClientboundAwardStatsPacket() {}
/*    */   
/*    */   public ClientboundAwardStatsPacket(Object2IntMap<Stat<?>> debug1) {
/* 21 */     this.stats = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 26 */     debug1.handleAwardStats(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 33 */     int debug2 = debug1.readVarInt();
/* 34 */     this.stats = (Object2IntMap<Stat<?>>)new Object2IntOpenHashMap(debug2);
/*    */     
/* 36 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 37 */       readStat((StatType)Registry.STAT_TYPE.byId(debug1.readVarInt()), debug1);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> void readStat(StatType<T> debug1, FriendlyByteBuf debug2) {
/* 43 */     int debug3 = debug2.readVarInt();
/* 44 */     int debug4 = debug2.readVarInt();
/* 45 */     this.stats.put(debug1.get(debug1.getRegistry().byId(debug3)), debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 50 */     debug1.writeVarInt(this.stats.size());
/* 51 */     for (ObjectIterator<Object2IntMap.Entry<Stat<?>>> objectIterator = this.stats.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Stat<?>> debug3 = objectIterator.next();
/* 52 */       Stat<?> debug4 = (Stat)debug3.getKey();
/* 53 */       debug1.writeVarInt(Registry.STAT_TYPE.getId(debug4.getType()));
/* 54 */       debug1.writeVarInt(getId(debug4));
/* 55 */       debug1.writeVarInt(debug3.getIntValue()); }
/*    */   
/*    */   }
/*    */   
/*    */   private <T> int getId(Stat<T> debug1) {
/* 60 */     return debug1.getType().getRegistry().getId(debug1.getValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAwardStatsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */