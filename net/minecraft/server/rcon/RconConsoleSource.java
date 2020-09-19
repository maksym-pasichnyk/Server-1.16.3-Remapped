/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.commands.CommandSource;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class RconConsoleSource
/*    */   implements CommandSource
/*    */ {
/* 18 */   private static final TextComponent RCON_COMPONENT = new TextComponent("Rcon");
/* 19 */   private final StringBuffer buffer = new StringBuffer();
/*    */   private final MinecraftServer server;
/*    */   
/*    */   public RconConsoleSource(MinecraftServer debug1) {
/* 23 */     this.server = debug1;
/*    */   }
/*    */   
/*    */   public void prepareForCommand() {
/* 27 */     this.buffer.setLength(0);
/*    */   }
/*    */   
/*    */   public String getCommandResponse() {
/* 31 */     return this.buffer.toString();
/*    */   }
/*    */   
/*    */   public CommandSourceStack createCommandSourceStack() {
/* 35 */     ServerLevel debug1 = this.server.overworld();
/* 36 */     return new CommandSourceStack(this, Vec3.atLowerCornerOf((Vec3i)debug1.getSharedSpawnPos()), Vec2.ZERO, debug1, 4, "Rcon", (Component)RCON_COMPONENT, this.server, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendMessage(Component debug1, UUID debug2) {
/* 41 */     this.buffer.append(debug1.getString());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptsSuccess() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptsFailure() {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldInformAdmins() {
/* 56 */     return this.server.shouldRconBroadcast();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\RconConsoleSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */