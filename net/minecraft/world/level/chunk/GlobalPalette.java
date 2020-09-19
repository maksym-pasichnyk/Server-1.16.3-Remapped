/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.IdMapper;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class GlobalPalette<T>
/*    */   implements Palette<T> {
/*    */   private final IdMapper<T> registry;
/*    */   private final T defaultValue;
/*    */   
/*    */   public GlobalPalette(IdMapper<T> debug1, T debug2) {
/* 14 */     this.registry = debug1;
/* 15 */     this.defaultValue = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int idFor(T debug1) {
/* 20 */     int debug2 = this.registry.getId(debug1);
/* 21 */     return (debug2 == -1) ? 0 : debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean maybeHas(Predicate<T> debug1) {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T valueFor(int debug1) {
/* 31 */     T debug2 = (T)this.registry.byId(debug1);
/* 32 */     return (debug2 == null) ? this.defaultValue : debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSerializedSize() {
/* 45 */     return FriendlyByteBuf.getVarIntSize(0);
/*    */   }
/*    */   
/*    */   public void read(ListTag debug1) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\GlobalPalette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */