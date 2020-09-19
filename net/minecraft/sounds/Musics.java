/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Musics
/*    */ {
/* 12 */   public static final Music MENU = new Music(SoundEvents.MUSIC_MENU, 20, 600, true);
/* 13 */   public static final Music CREATIVE = new Music(SoundEvents.MUSIC_CREATIVE, 12000, 24000, false);
/* 14 */   public static final Music CREDITS = new Music(SoundEvents.MUSIC_CREDITS, 0, 0, true);
/* 15 */   public static final Music END_BOSS = new Music(SoundEvents.MUSIC_DRAGON, 0, 0, true);
/* 16 */   public static final Music END = new Music(SoundEvents.MUSIC_END, 6000, 24000, true);
/*    */   
/* 18 */   public static final Music UNDER_WATER = createGameMusic(SoundEvents.MUSIC_UNDER_WATER);
/* 19 */   public static final Music GAME = createGameMusic(SoundEvents.MUSIC_GAME);
/*    */   
/*    */   public static Music createGameMusic(SoundEvent debug0) {
/* 22 */     return new Music(debug0, 12000, 24000, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\sounds\Musics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */