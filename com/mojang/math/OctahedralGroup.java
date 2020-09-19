/*     */ package com.mojang.math;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum OctahedralGroup
/*     */   implements StringRepresentable
/*     */ {
/*  23 */   IDENTITY("identity", SymmetricGroup3.P123, false, false, false),
/*     */ 
/*     */   
/*  26 */   ROT_180_FACE_XY("rot_180_face_xy", SymmetricGroup3.P123, true, true, false),
/*  27 */   ROT_180_FACE_XZ("rot_180_face_xz", SymmetricGroup3.P123, true, false, true),
/*  28 */   ROT_180_FACE_YZ("rot_180_face_yz", SymmetricGroup3.P123, false, true, true),
/*     */ 
/*     */   
/*  31 */   ROT_120_NNN("rot_120_nnn", SymmetricGroup3.P231, false, false, false),
/*  32 */   ROT_120_NNP("rot_120_nnp", SymmetricGroup3.P312, true, false, true),
/*  33 */   ROT_120_NPN("rot_120_npn", SymmetricGroup3.P312, false, true, true),
/*  34 */   ROT_120_NPP("rot_120_npp", SymmetricGroup3.P231, true, false, true),
/*  35 */   ROT_120_PNN("rot_120_pnn", SymmetricGroup3.P312, true, true, false),
/*  36 */   ROT_120_PNP("rot_120_pnp", SymmetricGroup3.P231, true, true, false),
/*  37 */   ROT_120_PPN("rot_120_ppn", SymmetricGroup3.P231, false, true, true),
/*  38 */   ROT_120_PPP("rot_120_ppp", SymmetricGroup3.P312, false, false, false),
/*     */ 
/*     */   
/*  41 */   ROT_180_EDGE_XY_NEG("rot_180_edge_xy_neg", SymmetricGroup3.P213, true, true, true),
/*  42 */   ROT_180_EDGE_XY_POS("rot_180_edge_xy_pos", SymmetricGroup3.P213, false, false, true),
/*  43 */   ROT_180_EDGE_XZ_NEG("rot_180_edge_xz_neg", SymmetricGroup3.P321, true, true, true),
/*  44 */   ROT_180_EDGE_XZ_POS("rot_180_edge_xz_pos", SymmetricGroup3.P321, false, true, false),
/*  45 */   ROT_180_EDGE_YZ_NEG("rot_180_edge_yz_neg", SymmetricGroup3.P132, true, true, true),
/*  46 */   ROT_180_EDGE_YZ_POS("rot_180_edge_yz_pos", SymmetricGroup3.P132, true, false, false),
/*     */ 
/*     */   
/*  49 */   ROT_90_X_NEG("rot_90_x_neg", SymmetricGroup3.P132, false, false, true),
/*  50 */   ROT_90_X_POS("rot_90_x_pos", SymmetricGroup3.P132, false, true, false),
/*  51 */   ROT_90_Y_NEG("rot_90_y_neg", SymmetricGroup3.P321, true, false, false),
/*  52 */   ROT_90_Y_POS("rot_90_y_pos", SymmetricGroup3.P321, false, false, true),
/*  53 */   ROT_90_Z_NEG("rot_90_z_neg", SymmetricGroup3.P213, false, true, false),
/*  54 */   ROT_90_Z_POS("rot_90_z_pos", SymmetricGroup3.P213, true, false, false),
/*     */ 
/*     */   
/*  57 */   INVERSION("inversion", SymmetricGroup3.P123, true, true, true),
/*     */ 
/*     */   
/*  60 */   INVERT_X("invert_x", SymmetricGroup3.P123, true, false, false),
/*  61 */   INVERT_Y("invert_y", SymmetricGroup3.P123, false, true, false),
/*  62 */   INVERT_Z("invert_z", SymmetricGroup3.P123, false, false, true),
/*     */ 
/*     */   
/*  65 */   ROT_60_REF_NNN("rot_60_ref_nnn", SymmetricGroup3.P312, true, true, true),
/*  66 */   ROT_60_REF_NNP("rot_60_ref_nnp", SymmetricGroup3.P231, true, false, false),
/*  67 */   ROT_60_REF_NPN("rot_60_ref_npn", SymmetricGroup3.P231, false, false, true),
/*  68 */   ROT_60_REF_NPP("rot_60_ref_npp", SymmetricGroup3.P312, false, false, true),
/*  69 */   ROT_60_REF_PNN("rot_60_ref_pnn", SymmetricGroup3.P231, false, true, false),
/*  70 */   ROT_60_REF_PNP("rot_60_ref_pnp", SymmetricGroup3.P312, true, false, false),
/*  71 */   ROT_60_REF_PPN("rot_60_ref_ppn", SymmetricGroup3.P312, false, true, false),
/*  72 */   ROT_60_REF_PPP("rot_60_ref_ppp", SymmetricGroup3.P231, true, true, true),
/*     */ 
/*     */   
/*  75 */   SWAP_XY("swap_xy", SymmetricGroup3.P213, false, false, false),
/*  76 */   SWAP_YZ("swap_yz", SymmetricGroup3.P132, false, false, false),
/*  77 */   SWAP_XZ("swap_xz", SymmetricGroup3.P321, false, false, false),
/*     */ 
/*     */   
/*  80 */   SWAP_NEG_XY("swap_neg_xy", SymmetricGroup3.P213, true, true, false),
/*  81 */   SWAP_NEG_YZ("swap_neg_yz", SymmetricGroup3.P132, false, true, true),
/*  82 */   SWAP_NEG_XZ("swap_neg_xz", SymmetricGroup3.P321, true, false, true),
/*     */ 
/*     */   
/*  85 */   ROT_90_REF_X_NEG("rot_90_ref_x_neg", SymmetricGroup3.P132, true, false, true),
/*  86 */   ROT_90_REF_X_POS("rot_90_ref_x_pos", SymmetricGroup3.P132, true, true, false),
/*  87 */   ROT_90_REF_Y_NEG("rot_90_ref_y_neg", SymmetricGroup3.P321, true, true, false),
/*  88 */   ROT_90_REF_Y_POS("rot_90_ref_y_pos", SymmetricGroup3.P321, false, true, true),
/*  89 */   ROT_90_REF_Z_NEG("rot_90_ref_z_neg", SymmetricGroup3.P213, false, true, true),
/*  90 */   ROT_90_REF_Z_POS("rot_90_ref_z_pos", SymmetricGroup3.P213, true, false, true);
/*     */   
/*     */   private final Matrix3f transformation;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   @Nullable
/*     */   private Map<Direction, Direction> rotatedDirections;
/*     */   private final boolean invertX;
/*     */   private final boolean invertY;
/*     */   private final boolean invertZ;
/*     */   private final SymmetricGroup3 permutation;
/*     */   private static final OctahedralGroup[][] cayleyTable;
/*     */   private static final OctahedralGroup[] inverseTable;
/*     */   
/*     */   OctahedralGroup(String debug3, SymmetricGroup3 debug4, boolean debug5, boolean debug6, boolean debug7) {
/* 106 */     this.name = debug3;
/* 107 */     this.invertX = debug5;
/* 108 */     this.invertY = debug6;
/* 109 */     this.invertZ = debug7;
/* 110 */     this.permutation = debug4;
/*     */     
/* 112 */     this.transformation = new Matrix3f();
/* 113 */     this.transformation.m00 = debug5 ? -1.0F : 1.0F;
/* 114 */     this.transformation.m11 = debug6 ? -1.0F : 1.0F;
/* 115 */     this.transformation.m22 = debug7 ? -1.0F : 1.0F;
/*     */     
/* 117 */     this.transformation.mul(debug4.transformation());
/*     */   }
/*     */   
/*     */   private BooleanList packInversions() {
/* 121 */     return (BooleanList)new BooleanArrayList(new boolean[] { this.invertX, this.invertY, this.invertZ });
/*     */   }
/*     */   static {
/* 124 */     cayleyTable = (OctahedralGroup[][])Util.make(new OctahedralGroup[(values()).length][(values()).length], debug0 -> {
/*     */           Map<Pair<SymmetricGroup3, BooleanList>, OctahedralGroup> debug1 = (Map<Pair<SymmetricGroup3, BooleanList>, OctahedralGroup>)Arrays.<OctahedralGroup>stream(values()).collect(Collectors.toMap((), ()));
/*     */           
/*     */           for (OctahedralGroup debug5 : values()) {
/*     */             for (OctahedralGroup debug9 : values()) {
/*     */               BooleanList debug10 = debug5.packInversions();
/*     */               
/*     */               BooleanList debug11 = debug9.packInversions();
/*     */               
/*     */               SymmetricGroup3 debug12 = debug9.permutation.compose(debug5.permutation);
/*     */               
/*     */               BooleanArrayList debug13 = new BooleanArrayList(3);
/*     */               
/*     */               for (int debug14 = 0; debug14 < 3; debug14++) {
/*     */                 debug13.add(debug10.getBoolean(debug14) ^ debug11.getBoolean(debug5.permutation.permutation(debug14)));
/*     */               }
/*     */               
/*     */               debug0[debug5.ordinal()][debug9.ordinal()] = debug1.get(Pair.of(debug12, debug13));
/*     */             } 
/*     */           } 
/*     */         });
/* 145 */     inverseTable = (OctahedralGroup[])Arrays.<OctahedralGroup>stream(values()).map(debug0 -> (OctahedralGroup)Arrays.<OctahedralGroup>stream(values()).filter(()).findAny().get()).toArray(debug0 -> new OctahedralGroup[debug0]);
/*     */   }
/*     */   public OctahedralGroup compose(OctahedralGroup debug1) {
/* 148 */     return cayleyTable[ordinal()][debug1.ordinal()];
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
/*     */   public String toString() {
/* 161 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/* 166 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction rotate(Direction debug1) {
/* 171 */     if (this.rotatedDirections == null) {
/* 172 */       this.rotatedDirections = Maps.newEnumMap(Direction.class);
/*     */       
/* 174 */       for (Direction debug5 : Direction.values()) {
/* 175 */         Direction.Axis debug6 = debug5.getAxis();
/* 176 */         Direction.AxisDirection debug7 = debug5.getAxisDirection();
/*     */         
/* 178 */         Direction.Axis debug8 = Direction.Axis.values()[this.permutation.permutation(debug6.ordinal())];
/*     */         
/* 180 */         Direction.AxisDirection debug9 = inverts(debug8) ? debug7.opposite() : debug7;
/*     */         
/* 182 */         Direction debug10 = Direction.fromAxisAndDirection(debug8, debug9);
/*     */         
/* 184 */         this.rotatedDirections.put(debug5, debug10);
/*     */       } 
/*     */     } 
/* 187 */     return this.rotatedDirections.get(debug1);
/*     */   }
/*     */   
/*     */   public boolean inverts(Direction.Axis debug1) {
/* 191 */     switch (debug1) {
/*     */       case X:
/* 193 */         return this.invertX;
/*     */       case Y:
/* 195 */         return this.invertY;
/*     */     } 
/*     */     
/* 198 */     return this.invertZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public FrontAndTop rotate(FrontAndTop debug1) {
/* 203 */     return FrontAndTop.fromFrontAndTop(rotate(debug1.front()), rotate(debug1.top()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\math\OctahedralGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */