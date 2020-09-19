/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public enum NoteBlockInstrument implements StringRepresentable {
/* 12 */   HARP("harp", SoundEvents.NOTE_BLOCK_HARP),
/* 13 */   BASEDRUM("basedrum", SoundEvents.NOTE_BLOCK_BASEDRUM),
/* 14 */   SNARE("snare", SoundEvents.NOTE_BLOCK_SNARE),
/* 15 */   HAT("hat", SoundEvents.NOTE_BLOCK_HAT),
/* 16 */   BASS("bass", SoundEvents.NOTE_BLOCK_BASS),
/* 17 */   FLUTE("flute", SoundEvents.NOTE_BLOCK_FLUTE),
/* 18 */   BELL("bell", SoundEvents.NOTE_BLOCK_BELL),
/* 19 */   GUITAR("guitar", SoundEvents.NOTE_BLOCK_GUITAR),
/* 20 */   CHIME("chime", SoundEvents.NOTE_BLOCK_CHIME),
/* 21 */   XYLOPHONE("xylophone", SoundEvents.NOTE_BLOCK_XYLOPHONE),
/* 22 */   IRON_XYLOPHONE("iron_xylophone", SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE),
/* 23 */   COW_BELL("cow_bell", SoundEvents.NOTE_BLOCK_COW_BELL),
/* 24 */   DIDGERIDOO("didgeridoo", SoundEvents.NOTE_BLOCK_DIDGERIDOO),
/* 25 */   BIT("bit", SoundEvents.NOTE_BLOCK_BIT),
/* 26 */   BANJO("banjo", SoundEvents.NOTE_BLOCK_BANJO),
/* 27 */   PLING("pling", SoundEvents.NOTE_BLOCK_PLING);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final SoundEvent soundEvent;
/*    */   
/*    */   NoteBlockInstrument(String debug3, SoundEvent debug4) {
/* 34 */     this.name = debug3;
/* 35 */     this.soundEvent = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 40 */     return this.name;
/*    */   }
/*    */   
/*    */   public SoundEvent getSoundEvent() {
/* 44 */     return this.soundEvent;
/*    */   }
/*    */   
/*    */   public static NoteBlockInstrument byState(BlockState debug0) {
/* 48 */     if (debug0.is(Blocks.CLAY)) {
/* 49 */       return FLUTE;
/*    */     }
/* 51 */     if (debug0.is(Blocks.GOLD_BLOCK)) {
/* 52 */       return BELL;
/*    */     }
/* 54 */     if (debug0.is((Tag)BlockTags.WOOL)) {
/* 55 */       return GUITAR;
/*    */     }
/* 57 */     if (debug0.is(Blocks.PACKED_ICE)) {
/* 58 */       return CHIME;
/*    */     }
/* 60 */     if (debug0.is(Blocks.BONE_BLOCK)) {
/* 61 */       return XYLOPHONE;
/*    */     }
/* 63 */     if (debug0.is(Blocks.IRON_BLOCK)) {
/* 64 */       return IRON_XYLOPHONE;
/*    */     }
/* 66 */     if (debug0.is(Blocks.SOUL_SAND)) {
/* 67 */       return COW_BELL;
/*    */     }
/* 69 */     if (debug0.is(Blocks.PUMPKIN)) {
/* 70 */       return DIDGERIDOO;
/*    */     }
/* 72 */     if (debug0.is(Blocks.EMERALD_BLOCK)) {
/* 73 */       return BIT;
/*    */     }
/* 75 */     if (debug0.is(Blocks.HAY_BLOCK)) {
/* 76 */       return BANJO;
/*    */     }
/* 78 */     if (debug0.is(Blocks.GLOWSTONE)) {
/* 79 */       return PLING;
/*    */     }
/*    */     
/* 82 */     Material debug1 = debug0.getMaterial();
/* 83 */     if (debug1 == Material.STONE) {
/* 84 */       return BASEDRUM;
/*    */     }
/* 86 */     if (debug1 == Material.SAND) {
/* 87 */       return SNARE;
/*    */     }
/* 89 */     if (debug1 == Material.GLASS) {
/* 90 */       return HAT;
/*    */     }
/* 92 */     if (debug1 == Material.WOOD || debug1 == Material.NETHER_WOOD) {
/* 93 */       return BASS;
/*    */     }
/*    */     
/* 96 */     return HARP;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\NoteBlockInstrument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */