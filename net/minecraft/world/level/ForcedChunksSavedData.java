/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ 
/*    */ public class ForcedChunksSavedData
/*    */   extends SavedData
/*    */ {
/* 11 */   private LongSet chunks = (LongSet)new LongOpenHashSet();
/*    */   
/*    */   public ForcedChunksSavedData() {
/* 14 */     super("chunks");
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(CompoundTag debug1) {
/* 19 */     this.chunks = (LongSet)new LongOpenHashSet(debug1.getLongArray("Forced"));
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 24 */     debug1.putLongArray("Forced", this.chunks.toLongArray());
/* 25 */     return debug1;
/*    */   }
/*    */   
/*    */   public LongSet getChunks() {
/* 29 */     return this.chunks;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ForcedChunksSavedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */