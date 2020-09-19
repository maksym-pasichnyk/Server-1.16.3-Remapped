/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class ClickEvent {
/*    */   private final Action action;
/*    */   private final String value;
/*    */   
/*    */   public ClickEvent(Action debug1, String debug2) {
/* 12 */     this.action = debug1;
/* 13 */     this.value = debug2;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 17 */     return this.action;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 21 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 26 */     if (this == debug1) {
/* 27 */       return true;
/*    */     }
/* 29 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     ClickEvent debug2 = (ClickEvent)debug1;
/*    */     
/* 35 */     if (this.action != debug2.action) {
/* 36 */       return false;
/*    */     }
/* 38 */     if ((this.value != null) ? !this.value.equals(debug2.value) : (debug2.value != null)) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 55 */     int debug1 = this.action.hashCode();
/* 56 */     debug1 = 31 * debug1 + ((this.value != null) ? this.value.hashCode() : 0);
/* 57 */     return debug1;
/*    */   }
/*    */   
/*    */   public enum Action { private static final Map<String, Action> LOOKUP;
/* 61 */     OPEN_URL("open_url", true),
/* 62 */     OPEN_FILE("open_file", false),
/* 63 */     RUN_COMMAND("run_command", true),
/* 64 */     SUGGEST_COMMAND("suggest_command", true),
/* 65 */     CHANGE_PAGE("change_page", true),
/* 66 */     COPY_TO_CLIPBOARD("copy_to_clipboard", true);
/*    */     
/*    */     static {
/* 69 */       LOOKUP = (Map<String, Action>)Arrays.<Action>stream(values()).collect(Collectors.toMap(Action::getName, debug0 -> debug0));
/*    */     }
/*    */     private final boolean allowFromServer;
/*    */     private final String name;
/*    */     
/*    */     Action(String debug3, boolean debug4) {
/* 75 */       this.name = debug3;
/* 76 */       this.allowFromServer = debug4;
/*    */     }
/*    */     
/*    */     public boolean isAllowedFromServer() {
/* 80 */       return this.allowFromServer;
/*    */     }
/*    */     
/*    */     public String getName() {
/* 84 */       return this.name;
/*    */     }
/*    */     
/*    */     public static Action getByName(String debug0) {
/* 88 */       return LOOKUP.get(debug0);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\ClickEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */