/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public enum SoundSource
/*    */ {
/* 10 */   MASTER("master"),
/* 11 */   MUSIC("music"),
/* 12 */   RECORDS("record"),
/* 13 */   WEATHER("weather"),
/* 14 */   BLOCKS("block"),
/* 15 */   HOSTILE("hostile"),
/* 16 */   NEUTRAL("neutral"),
/* 17 */   PLAYERS("player"),
/* 18 */   AMBIENT("ambient"),
/* 19 */   VOICE("voice"); private static final Map<String, SoundSource> BY_NAME;
/*    */   
/*    */   static {
/* 22 */     BY_NAME = (Map<String, SoundSource>)Arrays.<SoundSource>stream(values()).collect(Collectors.toMap(SoundSource::getName, Function.identity()));
/*    */   }
/*    */   private final String name;
/*    */   
/*    */   SoundSource(String debug3) {
/* 27 */     this.name = debug3;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\sounds\SoundSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */