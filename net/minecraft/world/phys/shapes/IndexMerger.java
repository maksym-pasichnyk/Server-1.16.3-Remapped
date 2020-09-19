package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

interface IndexMerger {
  DoubleList getList();
  
  boolean forMergedIndexes(IndexConsumer paramIndexConsumer);
  
  public static interface IndexConsumer {
    boolean merge(int param1Int1, int param1Int2, int param1Int3);
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\IndexMerger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */