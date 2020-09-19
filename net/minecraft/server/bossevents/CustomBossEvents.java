/*    */ package net.minecraft.server.bossevents;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class CustomBossEvents {
/* 14 */   private final Map<ResourceLocation, CustomBossEvent> events = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public CustomBossEvent get(ResourceLocation debug1) {
/* 21 */     return this.events.get(debug1);
/*    */   }
/*    */   
/*    */   public CustomBossEvent create(ResourceLocation debug1, Component debug2) {
/* 25 */     CustomBossEvent debug3 = new CustomBossEvent(debug1, debug2);
/* 26 */     this.events.put(debug1, debug3);
/* 27 */     return debug3;
/*    */   }
/*    */   
/*    */   public void remove(CustomBossEvent debug1) {
/* 31 */     this.events.remove(debug1.getTextId());
/*    */   }
/*    */   
/*    */   public Collection<ResourceLocation> getIds() {
/* 35 */     return this.events.keySet();
/*    */   }
/*    */   
/*    */   public Collection<CustomBossEvent> getEvents() {
/* 39 */     return this.events.values();
/*    */   }
/*    */   
/*    */   public CompoundTag save() {
/* 43 */     CompoundTag debug1 = new CompoundTag();
/*    */     
/* 45 */     for (CustomBossEvent debug3 : this.events.values()) {
/* 46 */       debug1.put(debug3.getTextId().toString(), (Tag)debug3.save());
/*    */     }
/*    */     
/* 49 */     return debug1;
/*    */   }
/*    */   
/*    */   public void load(CompoundTag debug1) {
/* 53 */     for (String debug3 : debug1.getAllKeys()) {
/* 54 */       ResourceLocation debug4 = new ResourceLocation(debug3);
/* 55 */       this.events.put(debug4, CustomBossEvent.load(debug1.getCompound(debug3), debug4));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onPlayerConnect(ServerPlayer debug1) {
/* 60 */     for (CustomBossEvent debug3 : this.events.values()) {
/* 61 */       debug3.onPlayerConnect(debug1);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onPlayerDisconnect(ServerPlayer debug1) {
/* 66 */     for (CustomBossEvent debug3 : this.events.values())
/* 67 */       debug3.onPlayerDisconnect(debug1); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\bossevents\CustomBossEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */