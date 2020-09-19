/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ 
/*    */ public class MapIndex
/*    */   extends SavedData
/*    */ {
/* 12 */   private final Object2IntMap<String> usedAuxIds = (Object2IntMap<String>)new Object2IntOpenHashMap();
/*    */   
/*    */   public MapIndex() {
/* 15 */     super("idcounts");
/* 16 */     this.usedAuxIds.defaultReturnValue(-1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(CompoundTag debug1) {
/* 21 */     this.usedAuxIds.clear();
/*    */     
/* 23 */     for (String debug3 : debug1.getAllKeys()) {
/* 24 */       if (debug1.contains(debug3, 99)) {
/* 25 */         this.usedAuxIds.put(debug3, debug1.getInt(debug3));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 32 */     for (ObjectIterator<Object2IntMap.Entry<String>> objectIterator = this.usedAuxIds.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<String> debug3 = objectIterator.next();
/* 33 */       debug1.putInt((String)debug3.getKey(), debug3.getIntValue()); }
/*    */     
/* 35 */     return debug1;
/*    */   }
/*    */   
/*    */   public int getFreeAuxValueForMap() {
/* 39 */     int debug1 = this.usedAuxIds.getInt("map") + 1;
/* 40 */     this.usedAuxIds.put("map", debug1);
/* 41 */     setDirty();
/* 42 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */