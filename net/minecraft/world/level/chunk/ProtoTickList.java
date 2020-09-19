/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortListIterator;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.TickList;
/*    */ import net.minecraft.world.level.TickPriority;
/*    */ import net.minecraft.world.level.chunk.storage.ChunkSerializer;
/*    */ 
/*    */ public class ProtoTickList<T>
/*    */   implements TickList<T>
/*    */ {
/*    */   protected final Predicate<T> ignore;
/*    */   private final ChunkPos chunkPos;
/* 19 */   private final ShortList[] toBeTicked = new ShortList[16];
/*    */   
/*    */   public ProtoTickList(Predicate<T> debug1, ChunkPos debug2) {
/* 22 */     this(debug1, debug2, new ListTag());
/*    */   }
/*    */   
/*    */   public ProtoTickList(Predicate<T> debug1, ChunkPos debug2, ListTag debug3) {
/* 26 */     this.ignore = debug1;
/* 27 */     this.chunkPos = debug2;
/* 28 */     for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 29 */       ListTag debug5 = debug3.getList(debug4);
/* 30 */       for (int debug6 = 0; debug6 < debug5.size(); debug6++) {
/* 31 */         ChunkAccess.getOrCreateOffsetList(this.toBeTicked, debug4).add(debug5.getShort(debug6));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public ListTag save() {
/* 37 */     return ChunkSerializer.packOffsets(this.toBeTicked);
/*    */   }
/*    */   
/*    */   public void copyOut(TickList<T> debug1, Function<BlockPos, T> debug2) {
/* 41 */     for (int debug3 = 0; debug3 < this.toBeTicked.length; debug3++) {
/* 42 */       if (this.toBeTicked[debug3] != null) {
/* 43 */         for (ShortListIterator<Short> shortListIterator = this.toBeTicked[debug3].iterator(); shortListIterator.hasNext(); ) { Short debug5 = shortListIterator.next();
/* 44 */           BlockPos debug6 = ProtoChunk.unpackOffsetCoordinates(debug5.shortValue(), debug3, this.chunkPos);
/* 45 */           debug1.scheduleTick(debug6, debug2.apply(debug6), 0); }
/*    */         
/* 47 */         this.toBeTicked[debug3].clear();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos debug1, T debug2) {
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void scheduleTick(BlockPos debug1, T debug2, int debug3, TickPriority debug4) {
/* 59 */     ChunkAccess.getOrCreateOffsetList(this.toBeTicked, debug1.getY() >> 4).add(ProtoChunk.packOffsetCoordinates(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean willTickThisTick(BlockPos debug1, T debug2) {
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ProtoTickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */