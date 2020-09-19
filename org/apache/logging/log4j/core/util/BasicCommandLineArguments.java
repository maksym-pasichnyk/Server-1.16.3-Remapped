/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import com.beust.jcommander.JCommander;
/*    */ import com.beust.jcommander.Parameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicCommandLineArguments
/*    */ {
/*    */   @Parameter(names = {"--help", "-?", "-h"}, help = true, description = "Prints this help.")
/*    */   private boolean help;
/*    */   
/*    */   public static <T extends BasicCommandLineArguments> T parseCommandLine(String[] mainArgs, Class<?> clazz, T args) {
/* 26 */     JCommander jCommander = new JCommander(args);
/* 27 */     jCommander.setProgramName(clazz.getName());
/* 28 */     jCommander.setCaseSensitiveOptions(false);
/* 29 */     jCommander.parse(mainArgs);
/* 30 */     if (args.isHelp()) {
/* 31 */       jCommander.usage();
/*    */     }
/* 33 */     return args;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isHelp() {
/* 40 */     return this.help;
/*    */   }
/*    */   
/*    */   public void setHelp(boolean help) {
/* 44 */     this.help = help;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\BasicCommandLineArguments.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */