/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ public enum ChatVisiblity
/*    */ {
/*  9 */   FULL(0, "options.chat.visibility.full"),
/* 10 */   SYSTEM(1, "options.chat.visibility.system"),
/* 11 */   HIDDEN(2, "options.chat.visibility.hidden"); private static final ChatVisiblity[] BY_ID;
/*    */   static {
/* 13 */     BY_ID = (ChatVisiblity[])Arrays.<ChatVisiblity>stream(values()).sorted(Comparator.comparingInt(ChatVisiblity::getId)).toArray(debug0 -> new ChatVisiblity[debug0]);
/*    */   }
/*    */   private final int id; private final String key;
/*    */   
/*    */   ChatVisiblity(int debug3, String debug4) {
/* 18 */     this.id = debug3;
/* 19 */     this.key = debug4;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 23 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\player\ChatVisiblity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */