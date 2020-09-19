/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ 
/*    */ public class StructureFeatureIndexSavedData
/*    */   extends SavedData
/*    */ {
/* 11 */   private LongSet all = (LongSet)new LongOpenHashSet();
/* 12 */   private LongSet remaining = (LongSet)new LongOpenHashSet();
/*    */   
/*    */   public StructureFeatureIndexSavedData(String debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(CompoundTag debug1) {
/* 20 */     this.all = (LongSet)new LongOpenHashSet(debug1.getLongArray("All"));
/* 21 */     this.remaining = (LongSet)new LongOpenHashSet(debug1.getLongArray("Remaining"));
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 26 */     debug1.putLongArray("All", this.all.toLongArray());
/* 27 */     debug1.putLongArray("Remaining", this.remaining.toLongArray());
/* 28 */     return debug1;
/*    */   }
/*    */   
/*    */   public void addIndex(long debug1) {
/* 32 */     this.all.add(debug1);
/* 33 */     this.remaining.add(debug1);
/*    */   }
/*    */   
/*    */   public boolean hasStartIndex(long debug1) {
/* 37 */     return this.all.contains(debug1);
/*    */   }
/*    */   
/*    */   public boolean hasUnhandledIndex(long debug1) {
/* 41 */     return this.remaining.contains(debug1);
/*    */   }
/*    */   
/*    */   public void removeIndex(long debug1) {
/* 45 */     this.remaining.remove(debug1);
/*    */   }
/*    */   
/*    */   public LongSet getAll() {
/* 49 */     return this.all;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureFeatureIndexSavedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */