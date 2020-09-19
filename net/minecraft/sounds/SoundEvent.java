/*    */ package net.minecraft.sounds;
/*    */ 
/*    */ public class SoundEvent {
/*    */   public static final Codec<SoundEvent> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = ResourceLocation.CODEC.xmap(SoundEvent::new, debug0 -> debug0.location);
/*    */   }
/*    */   private final ResourceLocation location;
/*    */   
/*    */   public SoundEvent(ResourceLocation debug1) {
/* 12 */     this.location = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\sounds\SoundEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */