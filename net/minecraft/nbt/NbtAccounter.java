/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public class NbtAccounter {
/*  4 */   public static final NbtAccounter UNLIMITED = new NbtAccounter(0L)
/*    */     {
/*    */       public void accountBits(long debug1) {}
/*    */     };
/*    */ 
/*    */   
/*    */   private final long quota;
/*    */   
/*    */   private long usage;
/*    */   
/*    */   public NbtAccounter(long debug1) {
/* 15 */     this.quota = debug1;
/*    */   }
/*    */   
/*    */   public void accountBits(long debug1) {
/* 19 */     this.usage += debug1 / 8L;
/* 20 */     if (this.usage > this.quota)
/* 21 */       throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.usage + "bytes where max allowed: " + this.quota); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\NbtAccounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */