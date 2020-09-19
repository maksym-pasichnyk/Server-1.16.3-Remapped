/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MobCategory
/*    */   implements StringRepresentable {
/* 11 */   MONSTER("monster", 70, false, false, 128),
/* 12 */   CREATURE("creature", 10, true, true, 128),
/* 13 */   AMBIENT("ambient", 15, true, false, 128),
/* 14 */   WATER_CREATURE("water_creature", 5, true, false, 128),
/* 15 */   WATER_AMBIENT("water_ambient", 20, true, false, 64),
/* 16 */   MISC("misc", -1, true, true, 128);
/*    */   
/*    */   static {
/* 19 */     CODEC = StringRepresentable.fromEnum(MobCategory::values, MobCategory::byName);
/*    */     
/* 21 */     BY_NAME = (Map<String, MobCategory>)Arrays.<MobCategory>stream(values()).collect(Collectors.toMap(MobCategory::getName, debug0 -> debug0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   private final int noDespawnDistance = 32; public static final Codec<MobCategory> CODEC; private static final Map<String, MobCategory> BY_NAME;
/*    */   private final int max;
/*    */   
/*    */   MobCategory(String debug3, int debug4, boolean debug5, boolean debug6, int debug7) {
/* 30 */     this.name = debug3;
/* 31 */     this.max = debug4;
/* 32 */     this.isFriendly = debug5;
/* 33 */     this.isPersistent = debug6;
/* 34 */     this.despawnDistance = debug7;
/*    */   }
/*    */   private final boolean isFriendly; private final boolean isPersistent; private final String name; private final int despawnDistance;
/*    */   public String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   public static MobCategory byName(String debug0) {
/* 47 */     return BY_NAME.get(debug0);
/*    */   }
/*    */   
/*    */   public int getMaxInstancesPerChunk() {
/* 51 */     return this.max;
/*    */   }
/*    */   
/*    */   public boolean isFriendly() {
/* 55 */     return this.isFriendly;
/*    */   }
/*    */   
/*    */   public boolean isPersistent() {
/* 59 */     return this.isPersistent;
/*    */   }
/*    */   
/*    */   public int getDespawnDistance() {
/* 63 */     return this.despawnDistance;
/*    */   }
/*    */   
/*    */   public int getNoDespawnDistance() {
/* 67 */     return 32;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\MobCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */