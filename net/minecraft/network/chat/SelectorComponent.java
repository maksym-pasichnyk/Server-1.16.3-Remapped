/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class SelectorComponent
/*    */   extends BaseComponent implements ContextAwareComponent {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final String pattern;
/*    */   @Nullable
/*    */   private final EntitySelector selector;
/*    */   
/*    */   public SelectorComponent(String debug1) {
/* 22 */     this.pattern = debug1;
/*    */     
/* 24 */     EntitySelector debug2 = null;
/*    */     try {
/* 26 */       EntitySelectorParser debug3 = new EntitySelectorParser(new StringReader(debug1));
/* 27 */       debug2 = debug3.parse();
/* 28 */     } catch (CommandSyntaxException debug3) {
/* 29 */       LOGGER.warn("Invalid selector component: {}", debug1, debug3.getMessage());
/*    */     } 
/* 31 */     this.selector = debug2;
/*    */   }
/*    */   
/*    */   public String getPattern() {
/* 35 */     return this.pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableComponent resolve(@Nullable CommandSourceStack debug1, @Nullable Entity debug2, int debug3) throws CommandSyntaxException {
/* 45 */     if (debug1 == null || this.selector == null) {
/* 46 */       return new TextComponent("");
/*    */     }
/* 48 */     return EntitySelector.joinNames(this.selector.findEntities(debug1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getContents() {
/* 54 */     return this.pattern;
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectorComponent plainCopy() {
/* 59 */     return new SelectorComponent(this.pattern);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 64 */     if (this == debug1) {
/* 65 */       return true;
/*    */     }
/*    */     
/* 68 */     if (debug1 instanceof SelectorComponent) {
/* 69 */       SelectorComponent debug2 = (SelectorComponent)debug1;
/* 70 */       return (this.pattern.equals(debug2.pattern) && super.equals(debug1));
/*    */     } 
/*    */     
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return "SelectorComponent{pattern='" + this.pattern + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*    */ 
/*    */       
/* 81 */       getStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\SelectorComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */