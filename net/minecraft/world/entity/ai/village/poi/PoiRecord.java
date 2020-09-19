/*    */ package net.minecraft.world.entity.ai.village.poi;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class PoiRecord {
/*    */   public static Codec<PoiRecord> codec(Runnable debug0) {
/* 13 */     return RecordCodecBuilder.create(debug1 -> debug1.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(()), (App)Registry.POINT_OF_INTEREST_TYPE.fieldOf("type").forGetter(()), (App)Codec.INT.fieldOf("free_tickets").orElse(Integer.valueOf(0)).forGetter(()), (App)RecordCodecBuilder.point(debug0)).apply((Applicative)debug1, PoiRecord::new));
/*    */   }
/*    */ 
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final PoiType poiType;
/*    */   
/*    */   private int freeTickets;
/*    */   
/*    */   private final Runnable setDirty;
/*    */ 
/*    */   
/*    */   private PoiRecord(BlockPos debug1, PoiType debug2, int debug3, Runnable debug4) {
/* 27 */     this.pos = debug1.immutable();
/* 28 */     this.poiType = debug2;
/* 29 */     this.freeTickets = debug3;
/* 30 */     this.setDirty = debug4;
/*    */   }
/*    */   
/*    */   public PoiRecord(BlockPos debug1, PoiType debug2, Runnable debug3) {
/* 34 */     this(debug1, debug2, debug2.getMaxTickets(), debug3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean acquireTicket() {
/* 44 */     if (this.freeTickets <= 0) {
/* 45 */       return false;
/*    */     }
/*    */     
/* 48 */     this.freeTickets--;
/* 49 */     this.setDirty.run();
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   protected boolean releaseTicket() {
/* 54 */     if (this.freeTickets >= this.poiType.getMaxTickets()) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     this.freeTickets++;
/* 59 */     this.setDirty.run();
/* 60 */     return true;
/*    */   }
/*    */   
/*    */   public boolean hasSpace() {
/* 64 */     return (this.freeTickets > 0);
/*    */   }
/*    */   
/*    */   public boolean isOccupied() {
/* 68 */     return (this.freeTickets != this.poiType.getMaxTickets());
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 72 */     return this.pos;
/*    */   }
/*    */   
/*    */   public PoiType getPoiType() {
/* 76 */     return this.poiType;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 81 */     if (this == debug1) {
/* 82 */       return true;
/*    */     }
/* 84 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 85 */       return false;
/*    */     }
/*    */     
/* 88 */     return Objects.equals(this.pos, ((PoiRecord)debug1).pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 93 */     return this.pos.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */