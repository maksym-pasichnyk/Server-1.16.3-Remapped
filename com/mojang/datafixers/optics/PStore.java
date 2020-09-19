/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Functor;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface PStore<I, J, X>
/*    */   extends App<PStore.Mu<I, J>, X>
/*    */ {
/*    */   public static final class Mu<I, J>
/*    */     implements K1 {}
/*    */   
/*    */   static <I, J, X> PStore<I, J, X> unbox(App<Mu<I, J>, X> box) {
/* 15 */     return (PStore)box;
/*    */   }
/*    */   
/*    */   X peek(J paramJ);
/*    */   
/*    */   I pos();
/*    */   
/*    */   public static final class Instance<I, J>
/*    */     implements Functor<Mu<I, J>, Instance.Mu<I, J>> {
/*    */     public static final class Mu<I, J> implements Functor.Mu {}
/*    */     
/*    */     public <T, R> App<PStore.Mu<I, J>, R> map(Function<? super T, ? extends R> func, App<PStore.Mu<I, J>, T> ts) {
/* 27 */       PStore<I, J, T> input = PStore.unbox(ts);
/* 28 */       return Optics.pStore(func.compose(input::peek)::apply, input::pos);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\PStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */