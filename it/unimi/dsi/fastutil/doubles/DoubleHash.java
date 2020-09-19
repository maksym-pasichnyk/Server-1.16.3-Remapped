package it.unimi.dsi.fastutil.doubles;

public interface DoubleHash {
  public static interface Strategy {
    int hashCode(double param1Double);
    
    boolean equals(double param1Double1, double param1Double2);
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleHash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */