package it.unimi.dsi.fastutil.shorts;

public interface ShortHash {
  public static interface Strategy {
    int hashCode(short param1Short);
    
    boolean equals(short param1Short1, short param1Short2);
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\ShortHash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */