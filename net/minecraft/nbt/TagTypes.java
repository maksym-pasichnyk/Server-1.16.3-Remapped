/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public class TagTypes {
/*  4 */   private static final TagType<?>[] TYPES = new TagType[] { EndTag.TYPE, ByteTag.TYPE, ShortTag.TYPE, IntTag.TYPE, LongTag.TYPE, FloatTag.TYPE, DoubleTag.TYPE, ByteArrayTag.TYPE, StringTag.TYPE, ListTag.TYPE, CompoundTag.TYPE, IntArrayTag.TYPE, LongArrayTag.TYPE };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TagType<?> getType(int debug0) {
/* 21 */     if (debug0 < 0 || debug0 >= TYPES.length) {
/* 22 */       return TagType.createInvalid(debug0);
/*    */     }
/*    */     
/* 25 */     return TYPES[debug0];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\TagTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */