package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@GwtIncompatible
@interface AndroidIncompatible {}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\AndroidIncompatible.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */