/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class GameTestTicker
/*    */ {
/*  8 */   public static final GameTestTicker singleton = new GameTestTicker();
/*  9 */   private final Collection<GameTestInfo> testInfos = Lists.newCopyOnWriteArrayList();
/*    */   
/*    */   public void add(GameTestInfo debug1) {
/* 12 */     this.testInfos.add(debug1);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 16 */     this.testInfos.clear();
/*    */   }
/*    */   
/*    */   public void tick() {
/* 20 */     this.testInfos.forEach(GameTestInfo::tick);
/* 21 */     this.testInfos.removeIf(GameTestInfo::isDone);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestTicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */