/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TranslatableComponent
/*     */   extends BaseComponent implements ContextAwareComponent {
/*  17 */   private static final Object[] NO_ARGS = new Object[0];
/*     */   
/*  19 */   private static final FormattedText TEXT_PERCENT = FormattedText.of("%");
/*  20 */   private static final FormattedText TEXT_NULL = FormattedText.of("null");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final Object[] args;
/*     */   @Nullable
/*     */   private Language decomposedWith;
/*  27 */   private final List<FormattedText> decomposedParts = Lists.newArrayList();
/*     */   
/*  29 */   private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
/*     */   
/*     */   public TranslatableComponent(String debug1) {
/*  32 */     this.key = debug1;
/*  33 */     this.args = NO_ARGS;
/*     */   }
/*     */   
/*     */   public TranslatableComponent(String debug1, Object... debug2) {
/*  37 */     this.key = debug1;
/*  38 */     this.args = debug2;
/*     */   }
/*     */   
/*     */   private void decompose() {
/*  42 */     Language debug1 = Language.getInstance();
/*  43 */     if (debug1 == this.decomposedWith) {
/*     */       return;
/*     */     }
/*  46 */     this.decomposedWith = debug1;
/*  47 */     this.decomposedParts.clear();
/*     */     
/*  49 */     String debug2 = debug1.getOrDefault(this.key);
/*     */     try {
/*  51 */       decomposeTemplate(debug2);
/*  52 */     } catch (TranslatableFormatException debug3) {
/*  53 */       this.decomposedParts.clear();
/*  54 */       this.decomposedParts.add(FormattedText.of(debug2));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decomposeTemplate(String debug1) {
/*  59 */     Matcher debug2 = FORMAT_PATTERN.matcher(debug1);
/*     */     
/*     */     try {
/*  62 */       int debug3 = 0;
/*  63 */       int debug4 = 0;
/*     */       
/*  65 */       while (debug2.find(debug4)) {
/*  66 */         int debug5 = debug2.start();
/*  67 */         int debug6 = debug2.end();
/*     */         
/*  69 */         if (debug5 > debug4) {
/*  70 */           String str = debug1.substring(debug4, debug5);
/*  71 */           if (str.indexOf('%') != -1) {
/*  72 */             throw new IllegalArgumentException();
/*     */           }
/*  74 */           this.decomposedParts.add(FormattedText.of(str));
/*     */         } 
/*     */         
/*  77 */         String debug7 = debug2.group(2);
/*  78 */         String debug8 = debug1.substring(debug5, debug6);
/*     */ 
/*     */         
/*  81 */         if ("%".equals(debug7) && "%%".equals(debug8)) {
/*  82 */           this.decomposedParts.add(TEXT_PERCENT);
/*  83 */         } else if ("s".equals(debug7)) {
/*  84 */           String debug9 = debug2.group(1);
/*  85 */           int debug10 = (debug9 != null) ? (Integer.parseInt(debug9) - 1) : debug3++;
/*  86 */           if (debug10 < this.args.length) {
/*  87 */             this.decomposedParts.add(getArgument(debug10));
/*     */           }
/*     */         } else {
/*  90 */           throw new TranslatableFormatException(this, "Unsupported format: '" + debug8 + "'");
/*     */         } 
/*     */         
/*  93 */         debug4 = debug6;
/*     */       } 
/*     */       
/*  96 */       if (debug4 < debug1.length()) {
/*  97 */         String debug5 = debug1.substring(debug4);
/*  98 */         if (debug5.indexOf('%') != -1) {
/*  99 */           throw new IllegalArgumentException();
/*     */         }
/* 101 */         this.decomposedParts.add(FormattedText.of(debug5));
/*     */       } 
/* 103 */     } catch (IllegalArgumentException debug3) {
/* 104 */       throw new TranslatableFormatException(this, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FormattedText getArgument(int debug1) {
/* 109 */     if (debug1 >= this.args.length) {
/* 110 */       throw new TranslatableFormatException(this, debug1);
/*     */     }
/*     */     
/* 113 */     Object debug2 = this.args[debug1];
/*     */     
/* 115 */     if (debug2 instanceof Component) {
/* 116 */       return (Component)debug2;
/*     */     }
/* 118 */     return (debug2 == null) ? TEXT_NULL : FormattedText.of(debug2.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TranslatableComponent plainCopy() {
/* 124 */     return new TranslatableComponent(this.key, this.args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Optional<T> visitSelf(FormattedText.ContentConsumer<T> debug1) {
/* 143 */     decompose();
/*     */     
/* 145 */     for (FormattedText debug3 : this.decomposedParts) {
/* 146 */       Optional<T> debug4 = debug3.visit(debug1);
/* 147 */       if (debug4.isPresent()) {
/* 148 */         return debug4;
/*     */       }
/*     */     } 
/*     */     
/* 152 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(@Nullable CommandSourceStack debug1, @Nullable Entity debug2, int debug3) throws CommandSyntaxException {
/* 157 */     Object[] debug4 = new Object[this.args.length];
/*     */     
/* 159 */     for (int debug5 = 0; debug5 < debug4.length; debug5++) {
/* 160 */       Object debug6 = this.args[debug5];
/* 161 */       if (debug6 instanceof Component) {
/* 162 */         debug4[debug5] = ComponentUtils.updateForEntity(debug1, (Component)debug6, debug2, debug3);
/*     */       } else {
/* 164 */         debug4[debug5] = debug6;
/*     */       } 
/*     */     } 
/* 167 */     return new TranslatableComponent(this.key, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 172 */     if (this == debug1) {
/* 173 */       return true;
/*     */     }
/*     */     
/* 176 */     if (debug1 instanceof TranslatableComponent) {
/* 177 */       TranslatableComponent debug2 = (TranslatableComponent)debug1;
/* 178 */       return (Arrays.equals(this.args, debug2.args) && this.key.equals(debug2.key) && super.equals(debug1));
/*     */     } 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 186 */     int debug1 = super.hashCode();
/* 187 */     debug1 = 31 * debug1 + this.key.hashCode();
/* 188 */     debug1 = 31 * debug1 + Arrays.hashCode(this.args);
/* 189 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 194 */     return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + 
/*     */       
/* 196 */       Arrays.toString(this.args) + ", siblings=" + this.siblings + ", style=" + 
/*     */       
/* 198 */       getStyle() + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 203 */     return this.key;
/*     */   }
/*     */   
/*     */   public Object[] getArgs() {
/* 207 */     return this.args;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\TranslatableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */