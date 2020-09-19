package io.netty.util.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface SuppressJava6Requirement {
  String reason();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\SuppressJava6Requirement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */