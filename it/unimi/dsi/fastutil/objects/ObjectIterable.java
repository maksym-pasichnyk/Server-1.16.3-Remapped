package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public interface ObjectIterable<K> extends Iterable<K> {
  ObjectIterator<K> iterator();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */