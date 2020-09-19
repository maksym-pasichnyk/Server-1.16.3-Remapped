/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ 
/*     */ 
/*     */ public class MessageArgument
/*     */   implements ArgumentType<MessageArgument.Message>
/*     */ {
/*  22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Hello world!", "foo", "@e", "Hello @p :)" });
/*     */   
/*     */   public static MessageArgument message() {
/*  25 */     return new MessageArgument();
/*     */   }
/*     */   
/*     */   public static Component getMessage(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  29 */     return ((Message)debug0.getArgument(debug1, Message.class)).toComponent((CommandSourceStack)debug0.getSource(), ((CommandSourceStack)debug0.getSource()).hasPermission(2));
/*     */   }
/*     */ 
/*     */   
/*     */   public Message parse(StringReader debug1) throws CommandSyntaxException {
/*  34 */     return Message.parseText(debug1, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/*  39 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Message {
/*     */     private final String text;
/*     */     private final MessageArgument.Part[] parts;
/*     */     
/*     */     public Message(String debug1, MessageArgument.Part[] debug2) {
/*  47 */       this.text = debug1;
/*  48 */       this.parts = debug2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Component toComponent(CommandSourceStack debug1, boolean debug2) throws CommandSyntaxException {
/*  60 */       if (this.parts.length == 0 || !debug2) {
/*  61 */         return (Component)new TextComponent(this.text);
/*     */       }
/*     */       
/*  64 */       TextComponent textComponent = new TextComponent(this.text.substring(0, this.parts[0].getStart()));
/*  65 */       int debug4 = this.parts[0].getStart();
/*     */       
/*  67 */       for (MessageArgument.Part debug8 : this.parts) {
/*  68 */         Component debug9 = debug8.toComponent(debug1);
/*  69 */         if (debug4 < debug8.getStart()) {
/*  70 */           textComponent.append(this.text.substring(debug4, debug8.getStart()));
/*     */         }
/*  72 */         if (debug9 != null) {
/*  73 */           textComponent.append(debug9);
/*     */         }
/*  75 */         debug4 = debug8.getEnd();
/*     */       } 
/*     */       
/*  78 */       if (debug4 < this.text.length()) {
/*  79 */         textComponent.append(this.text.substring(debug4, this.text.length()));
/*     */       }
/*     */       
/*  82 */       return (Component)textComponent;
/*     */     }
/*     */     
/*     */     public static Message parseText(StringReader debug0, boolean debug1) throws CommandSyntaxException {
/*  86 */       String debug2 = debug0.getString().substring(debug0.getCursor(), debug0.getTotalLength());
/*     */       
/*  88 */       if (!debug1) {
/*  89 */         debug0.setCursor(debug0.getTotalLength());
/*  90 */         return new Message(debug2, new MessageArgument.Part[0]);
/*     */       } 
/*     */       
/*  93 */       List<MessageArgument.Part> debug3 = Lists.newArrayList();
/*  94 */       int debug4 = debug0.getCursor();
/*     */       
/*  96 */       while (debug0.canRead()) {
/*  97 */         if (debug0.peek() == '@') {
/*  98 */           EntitySelector debug6; int debug5 = debug0.getCursor();
/*     */           
/*     */           try {
/* 101 */             EntitySelectorParser debug7 = new EntitySelectorParser(debug0);
/* 102 */             debug6 = debug7.parse();
/* 103 */           } catch (CommandSyntaxException debug7) {
/* 104 */             if (debug7.getType() == EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE || debug7.getType() == EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE) {
/* 105 */               debug0.setCursor(debug5 + 1);
/*     */               continue;
/*     */             } 
/* 108 */             throw debug7;
/*     */           } 
/* 110 */           debug3.add(new MessageArgument.Part(debug5 - debug4, debug0.getCursor() - debug4, debug6)); continue;
/*     */         } 
/* 112 */         debug0.skip();
/*     */       } 
/*     */ 
/*     */       
/* 116 */       return new Message(debug2, debug3.<MessageArgument.Part>toArray(new MessageArgument.Part[debug3.size()]));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Part {
/*     */     private final int start;
/*     */     private final int end;
/*     */     private final EntitySelector selector;
/*     */     
/*     */     public Part(int debug1, int debug2, EntitySelector debug3) {
/* 126 */       this.start = debug1;
/* 127 */       this.end = debug2;
/* 128 */       this.selector = debug3;
/*     */     }
/*     */     
/*     */     public int getStart() {
/* 132 */       return this.start;
/*     */     }
/*     */     
/*     */     public int getEnd() {
/* 136 */       return this.end;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Component toComponent(CommandSourceStack debug1) throws CommandSyntaxException {
/* 145 */       return (Component)EntitySelector.joinNames(this.selector.findEntities(debug1));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\MessageArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */