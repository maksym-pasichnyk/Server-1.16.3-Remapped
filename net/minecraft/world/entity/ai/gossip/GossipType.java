/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ public enum GossipType
/*    */ {
/* 12 */   MAJOR_NEGATIVE("major_negative", -5, 100, 10, 10),
/* 13 */   MINOR_NEGATIVE("minor_negative", -1, 200, 20, 20),
/*    */   
/* 15 */   MINOR_POSITIVE("minor_positive", 1, 200, 1, 5),
/* 16 */   MAJOR_POSITIVE("major_positive", 5, 100, 0, 100),
/*    */   
/* 18 */   TRADING("trading", 1, 25, 2, 20);
/*    */   
/*    */   public final String id;
/*    */   
/*    */   public final int weight;
/*    */   
/*    */   public final int max;
/*    */   
/*    */   public final int decayPerDay;
/*    */   public final int decayPerTransfer;
/*    */   private static final Map<String, GossipType> BY_ID;
/*    */   
/*    */   static {
/* 31 */     BY_ID = (Map<String, GossipType>)Stream.<GossipType>of(values()).collect(ImmutableMap.toImmutableMap(debug0 -> debug0.id, Function.identity()));
/*    */   }
/*    */   GossipType(String debug3, int debug4, int debug5, int debug6, int debug7) {
/* 34 */     this.id = debug3;
/* 35 */     this.weight = debug4;
/* 36 */     this.max = debug5;
/* 37 */     this.decayPerDay = debug6;
/* 38 */     this.decayPerTransfer = debug7;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static GossipType byId(String debug0) {
/* 43 */     return BY_ID.get(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\gossip\GossipType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */