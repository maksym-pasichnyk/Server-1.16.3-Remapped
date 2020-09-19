package io.netty.util;

public interface Constant<T extends Constant<T>> extends Comparable<T> {
  int id();
  
  String name();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\Constant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */