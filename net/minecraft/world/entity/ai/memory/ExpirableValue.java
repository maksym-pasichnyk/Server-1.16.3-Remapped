/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExpirableValue<T>
/*    */ {
/*    */   private final T value;
/*    */   private long timeToLive;
/*    */   
/*    */   public ExpirableValue(T debug1, long debug2) {
/* 18 */     this.value = debug1;
/* 19 */     this.timeToLive = debug2;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 23 */     if (canExpire()) {
/* 24 */       this.timeToLive--;
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T debug0) {
/* 29 */     return new ExpirableValue<>(debug0, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T debug0, long debug1) {
/* 33 */     return new ExpirableValue<>(debug0, debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T getValue() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 45 */     return (this.timeToLive <= 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return this.value.toString() + (canExpire() ? (" (ttl: " + this.timeToLive + ")") : "");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canExpire() {
/* 56 */     return (this.timeToLive != Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static <T> Codec<ExpirableValue<T>> codec(Codec<T> debug0) {
/* 60 */     return RecordCodecBuilder.create(debug1 -> debug1.group((App)debug0.fieldOf("value").forGetter(()), (App)Codec.LONG.optionalFieldOf("ttl").forGetter(())).apply((Applicative)debug1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\memory\ExpirableValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */