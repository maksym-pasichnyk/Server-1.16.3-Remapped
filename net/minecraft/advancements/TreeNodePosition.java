/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class TreeNodePosition
/*     */ {
/*     */   private final Advancement advancement;
/*     */   private final TreeNodePosition parent;
/*     */   private final TreeNodePosition previousSibling;
/*     */   private final int childIndex;
/*  13 */   private final List<TreeNodePosition> children = Lists.newArrayList();
/*     */   private TreeNodePosition ancestor;
/*     */   private TreeNodePosition thread;
/*     */   private int x;
/*     */   private float y;
/*     */   private float mod;
/*     */   private float change;
/*     */   private float shift;
/*     */   
/*     */   public TreeNodePosition(Advancement debug1, @Nullable TreeNodePosition debug2, @Nullable TreeNodePosition debug3, int debug4, int debug5) {
/*  23 */     if (debug1.getDisplay() == null) {
/*  24 */       throw new IllegalArgumentException("Can't position an invisible advancement!");
/*     */     }
/*  26 */     this.advancement = debug1;
/*  27 */     this.parent = debug2;
/*  28 */     this.previousSibling = debug3;
/*  29 */     this.childIndex = debug4;
/*  30 */     this.ancestor = this;
/*  31 */     this.x = debug5;
/*  32 */     this.y = -1.0F;
/*     */     
/*  34 */     TreeNodePosition debug6 = null;
/*  35 */     for (Advancement debug8 : debug1.getChildren()) {
/*  36 */       debug6 = addChild(debug8, debug6);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private TreeNodePosition addChild(Advancement debug1, @Nullable TreeNodePosition debug2) {
/*  42 */     if (debug1.getDisplay() != null) {
/*  43 */       debug2 = new TreeNodePosition(debug1, this, debug2, this.children.size() + 1, this.x + 1);
/*  44 */       this.children.add(debug2);
/*     */     } else {
/*  46 */       for (Advancement debug4 : debug1.getChildren()) {
/*  47 */         debug2 = addChild(debug4, debug2);
/*     */       }
/*     */     } 
/*  50 */     return debug2;
/*     */   }
/*     */   
/*     */   private void firstWalk() {
/*  54 */     if (this.children.isEmpty()) {
/*  55 */       if (this.previousSibling != null) {
/*  56 */         this.previousSibling.y++;
/*     */       } else {
/*  58 */         this.y = 0.0F;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     TreeNodePosition debug1 = null;
/*  64 */     for (TreeNodePosition debug3 : this.children) {
/*  65 */       debug3.firstWalk();
/*  66 */       debug1 = debug3.apportion((debug1 == null) ? debug3 : debug1);
/*     */     } 
/*  68 */     executeShifts();
/*     */     
/*  70 */     float debug2 = (((TreeNodePosition)this.children.get(0)).y + ((TreeNodePosition)this.children.get(this.children.size() - 1)).y) / 2.0F;
/*  71 */     if (this.previousSibling != null) {
/*  72 */       this.previousSibling.y++;
/*  73 */       this.mod = this.y - debug2;
/*     */     } else {
/*  75 */       this.y = debug2;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float secondWalk(float debug1, int debug2, float debug3) {
/*  80 */     this.y += debug1;
/*  81 */     this.x = debug2;
/*     */     
/*  83 */     if (this.y < debug3) {
/*  84 */       debug3 = this.y;
/*     */     }
/*     */     
/*  87 */     for (TreeNodePosition debug5 : this.children) {
/*  88 */       debug3 = debug5.secondWalk(debug1 + this.mod, debug2 + 1, debug3);
/*     */     }
/*     */     
/*  91 */     return debug3;
/*     */   }
/*     */   
/*     */   private void thirdWalk(float debug1) {
/*  95 */     this.y += debug1;
/*  96 */     for (TreeNodePosition debug3 : this.children) {
/*  97 */       debug3.thirdWalk(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void executeShifts() {
/* 102 */     float debug1 = 0.0F;
/* 103 */     float debug2 = 0.0F;
/* 104 */     for (int debug3 = this.children.size() - 1; debug3 >= 0; debug3--) {
/* 105 */       TreeNodePosition debug4 = this.children.get(debug3);
/* 106 */       debug4.y += debug1;
/* 107 */       debug4.mod += debug1;
/* 108 */       debug2 += debug4.change;
/* 109 */       debug1 += debug4.shift + debug2;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private TreeNodePosition previousOrThread() {
/* 115 */     if (this.thread != null) {
/* 116 */       return this.thread;
/*     */     }
/* 118 */     if (!this.children.isEmpty()) {
/* 119 */       return this.children.get(0);
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private TreeNodePosition nextOrThread() {
/* 126 */     if (this.thread != null) {
/* 127 */       return this.thread;
/*     */     }
/* 129 */     if (!this.children.isEmpty()) {
/* 130 */       return this.children.get(this.children.size() - 1);
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   private TreeNodePosition apportion(TreeNodePosition debug1) {
/* 136 */     if (this.previousSibling == null) {
/* 137 */       return debug1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 142 */     TreeNodePosition debug2 = this;
/* 143 */     TreeNodePosition debug3 = this;
/* 144 */     TreeNodePosition debug4 = this.previousSibling;
/* 145 */     TreeNodePosition debug5 = this.parent.children.get(0);
/* 146 */     float debug6 = this.mod;
/* 147 */     float debug7 = this.mod;
/* 148 */     float debug8 = debug4.mod;
/* 149 */     float debug9 = debug5.mod;
/*     */     
/* 151 */     while (debug4.nextOrThread() != null && debug2.previousOrThread() != null) {
/* 152 */       debug4 = debug4.nextOrThread();
/* 153 */       debug2 = debug2.previousOrThread();
/* 154 */       debug5 = debug5.previousOrThread();
/* 155 */       debug3 = debug3.nextOrThread();
/* 156 */       debug3.ancestor = this;
/* 157 */       float debug10 = debug4.y + debug8 - debug2.y + debug6 + 1.0F;
/* 158 */       if (debug10 > 0.0F) {
/* 159 */         debug4.getAncestor(this, debug1).moveSubtree(this, debug10);
/* 160 */         debug6 += debug10;
/* 161 */         debug7 += debug10;
/*     */       } 
/* 163 */       debug8 += debug4.mod;
/* 164 */       debug6 += debug2.mod;
/* 165 */       debug9 += debug5.mod;
/* 166 */       debug7 += debug3.mod;
/*     */     } 
/* 168 */     if (debug4.nextOrThread() != null && debug3.nextOrThread() == null) {
/* 169 */       debug3.thread = debug4.nextOrThread();
/* 170 */       debug3.mod += debug8 - debug7;
/*     */     } else {
/* 172 */       if (debug2.previousOrThread() != null && debug5.previousOrThread() == null) {
/* 173 */         debug5.thread = debug2.previousOrThread();
/* 174 */         debug5.mod += debug6 - debug9;
/*     */       } 
/* 176 */       debug1 = this;
/*     */     } 
/*     */     
/* 179 */     return debug1;
/*     */   }
/*     */   
/*     */   private void moveSubtree(TreeNodePosition debug1, float debug2) {
/* 183 */     float debug3 = (debug1.childIndex - this.childIndex);
/* 184 */     if (debug3 != 0.0F) {
/* 185 */       debug1.change -= debug2 / debug3;
/* 186 */       this.change += debug2 / debug3;
/*     */     } 
/* 188 */     debug1.shift += debug2;
/* 189 */     debug1.y += debug2;
/* 190 */     debug1.mod += debug2;
/*     */   }
/*     */   
/*     */   private TreeNodePosition getAncestor(TreeNodePosition debug1, TreeNodePosition debug2) {
/* 194 */     if (this.ancestor != null && debug1.parent.children.contains(this.ancestor)) {
/* 195 */       return this.ancestor;
/*     */     }
/* 197 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   private void finalizePosition() {
/* 202 */     if (this.advancement.getDisplay() != null) {
/* 203 */       this.advancement.getDisplay().setLocation(this.x, this.y);
/*     */     }
/*     */     
/* 206 */     if (!this.children.isEmpty()) {
/* 207 */       for (TreeNodePosition debug2 : this.children) {
/* 208 */         debug2.finalizePosition();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void run(Advancement debug0) {
/* 214 */     if (debug0.getDisplay() == null) {
/* 215 */       throw new IllegalArgumentException("Can't position children of an invisible root!");
/*     */     }
/* 217 */     TreeNodePosition debug1 = new TreeNodePosition(debug0, null, null, 1, 0);
/* 218 */     debug1.firstWalk();
/* 219 */     float debug2 = debug1.secondWalk(0.0F, 0, debug1.y);
/* 220 */     if (debug2 < 0.0F) {
/* 221 */       debug1.thirdWalk(-debug2);
/*     */     }
/* 223 */     debug1.finalizePosition();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\TreeNodePosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */