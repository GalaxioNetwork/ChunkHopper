package me.bugsum.chunkHopper.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String command();
    String permission();
    int length() default 1;
    String syntax() default "";
    boolean inGame() default false;
}