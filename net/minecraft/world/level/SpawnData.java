/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.WeighedRandom;
/*    */ 
/*    */ 
/*    */ public class SpawnData
/*    */   extends WeighedRandom.WeighedRandomItem
/*    */ {
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public SpawnData() {
/* 15 */     super(1);
/*    */     
/* 17 */     this.tag = new CompoundTag();
/* 18 */     this.tag.putString("id", "minecraft:pig");
/*    */   }
/*    */   
/*    */   public SpawnData(CompoundTag debug1) {
/* 22 */     this(debug1.contains("Weight", 99) ? debug1.getInt("Weight") : 1, debug1.getCompound("Entity"));
/*    */   }
/*    */   
/*    */   public SpawnData(int debug1, CompoundTag debug2) {
/* 26 */     super(debug1);
/*    */     
/* 28 */     this.tag = debug2;
/*    */ 
/*    */     
/* 31 */     ResourceLocation debug3 = ResourceLocation.tryParse(debug2.getString("id"));
/* 32 */     if (debug3 != null) {
/* 33 */       debug2.putString("id", debug3.toString());
/*    */     } else {
/* 35 */       debug2.putString("id", "minecraft:pig");
/*    */     } 
/*    */   }
/*    */   
/*    */   public CompoundTag save() {
/* 40 */     CompoundTag debug1 = new CompoundTag();
/*    */     
/* 42 */     debug1.put("Entity", (Tag)this.tag);
/* 43 */     debug1.putInt("Weight", this.weight);
/*    */     
/* 45 */     return debug1;
/*    */   }
/*    */   
/*    */   public CompoundTag getTag() {
/* 49 */     return this.tag;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\SpawnData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */