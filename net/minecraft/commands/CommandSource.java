/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface CommandSource
/*    */ {
/*  8 */   public static final CommandSource NULL = new CommandSource()
/*    */     {
/*    */       public void sendMessage(Component debug1, UUID debug2) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean acceptsSuccess() {
/* 15 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean acceptsFailure() {
/* 20 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean shouldInformAdmins() {
/* 25 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   void sendMessage(Component paramComponent, UUID paramUUID);
/*    */   
/*    */   boolean acceptsSuccess();
/*    */   
/*    */   boolean acceptsFailure();
/*    */   
/*    */   boolean shouldInformAdmins();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\CommandSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */