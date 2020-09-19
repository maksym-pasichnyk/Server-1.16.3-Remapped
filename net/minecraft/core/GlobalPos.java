/*    */ package net.minecraft.core;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class GlobalPos {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalPos::dimension), (App)BlockPos.CODEC.fieldOf("pos").forGetter(GlobalPos::pos)).apply((Applicative)debug0, GlobalPos::of));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<GlobalPos> CODEC;
/*    */   private final ResourceKey<Level> dimension;
/*    */   private final BlockPos pos;
/*    */   
/*    */   private GlobalPos(ResourceKey<Level> debug1, BlockPos debug2) {
/* 20 */     this.dimension = debug1;
/* 21 */     this.pos = debug2;
/*    */   }
/*    */   
/*    */   public static GlobalPos of(ResourceKey<Level> debug0, BlockPos debug1) {
/* 25 */     return new GlobalPos(debug0, debug1);
/*    */   }
/*    */   
/*    */   public ResourceKey<Level> dimension() {
/* 29 */     return this.dimension;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 33 */     return this.pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 38 */     if (this == debug1) {
/* 39 */       return true;
/*    */     }
/* 41 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 42 */       return false;
/*    */     }
/* 44 */     GlobalPos debug2 = (GlobalPos)debug1;
/* 45 */     return (Objects.equals(this.dimension, debug2.dimension) && Objects.equals(this.pos, debug2.pos));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     return Objects.hash(new Object[] { this.dimension, this.pos });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return this.dimension.toString() + " " + this.pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\GlobalPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */