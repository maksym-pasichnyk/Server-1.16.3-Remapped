/*    */ package net.minecraft.world.entity.schedule;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class Timeline
/*    */ {
/* 12 */   private final List<Keyframe> keyframes = Lists.newArrayList();
/*    */ 
/*    */   
/*    */   private int previousIndex;
/*    */ 
/*    */ 
/*    */   
/*    */   public Timeline addKeyframe(int debug1, float debug2) {
/* 20 */     this.keyframes.add(new Keyframe(debug1, debug2));
/* 21 */     sortAndDeduplicateKeyframes();
/* 22 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void sortAndDeduplicateKeyframes() {
/* 32 */     Int2ObjectAVLTreeMap int2ObjectAVLTreeMap = new Int2ObjectAVLTreeMap();
/* 33 */     this.keyframes.forEach(debug1 -> (Keyframe)debug0.put(debug1.getTimeStamp(), debug1));
/*    */     
/* 35 */     this.keyframes.clear();
/* 36 */     this.keyframes.addAll((Collection<? extends Keyframe>)int2ObjectAVLTreeMap.values());
/*    */     
/* 38 */     this.previousIndex = 0;
/*    */   }
/*    */   
/*    */   public float getValueAt(int debug1) {
/* 42 */     if (this.keyframes.size() <= 0) {
/* 43 */       return 0.0F;
/*    */     }
/*    */     
/* 46 */     Keyframe debug2 = this.keyframes.get(this.previousIndex);
/* 47 */     Keyframe debug3 = this.keyframes.get(this.keyframes.size() - 1);
/* 48 */     boolean debug4 = (debug1 < debug2.getTimeStamp());
/*    */     
/* 50 */     int debug5 = debug4 ? 0 : this.previousIndex;
/* 51 */     float debug6 = debug4 ? debug3.getValue() : debug2.getValue();
/*    */     
/* 53 */     for (int debug7 = debug5; debug7 < this.keyframes.size(); debug7++) {
/* 54 */       Keyframe debug8 = this.keyframes.get(debug7);
/* 55 */       if (debug8.getTimeStamp() > debug1) {
/*    */         break;
/*    */       }
/* 58 */       this.previousIndex = debug7;
/* 59 */       debug6 = debug8.getValue();
/*    */     } 
/*    */     
/* 62 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\schedule\Timeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */