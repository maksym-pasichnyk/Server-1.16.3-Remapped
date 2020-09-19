/*    */ package net.minecraft.world.entity.npc;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class VillagerData {
/* 10 */   private static final int[] NEXT_LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 70, 150, 250 };
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Registry.VILLAGER_TYPE.fieldOf("type").orElseGet(()).forGetter(()), (App)Registry.VILLAGER_PROFESSION.fieldOf("profession").orElseGet(()).forGetter(()), (App)Codec.INT.fieldOf("level").orElse(Integer.valueOf(1)).forGetter(())).apply((Applicative)debug0, VillagerData::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<VillagerData> CODEC;
/*    */   
/*    */   private final VillagerType type;
/*    */   private final VillagerProfession profession;
/*    */   private final int level;
/*    */   
/*    */   public VillagerData(VillagerType debug1, VillagerProfession debug2, int debug3) {
/* 23 */     this.type = debug1;
/* 24 */     this.profession = debug2;
/* 25 */     this.level = Math.max(1, debug3);
/*    */   }
/*    */   
/*    */   public VillagerType getType() {
/* 29 */     return this.type;
/*    */   }
/*    */   
/*    */   public VillagerProfession getProfession() {
/* 33 */     return this.profession;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 37 */     return this.level;
/*    */   }
/*    */   
/*    */   public VillagerData setType(VillagerType debug1) {
/* 41 */     return new VillagerData(debug1, this.profession, this.level);
/*    */   }
/*    */   
/*    */   public VillagerData setProfession(VillagerProfession debug1) {
/* 45 */     return new VillagerData(this.type, debug1, this.level);
/*    */   }
/*    */   
/*    */   public VillagerData setLevel(int debug1) {
/* 49 */     return new VillagerData(this.type, this.profession, debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getMaxXpPerLevel(int debug0) {
/* 57 */     return canLevelUp(debug0) ? NEXT_LEVEL_XP_THRESHOLDS[debug0] : 0;
/*    */   }
/*    */   
/*    */   public static boolean canLevelUp(int debug0) {
/* 61 */     return (debug0 >= 1 && debug0 < 5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\VillagerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */