package com.jjx.cloudnacosprovider.utils;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jiangjx
 */
public class AsmTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] bytes = generate();
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> clazz = classLoader.defineClass("com.jjx.cloudnacosprovider.utils.HelloWorld", bytes);
        Method main = clazz.getMethod("main", String[].class);
        main.invoke(clazz, new Object[]{new String[]{}});
    }

    /**
     * 这里动态生成一个类
     * @return 类的字节数组
     */
    private static byte[] generate() {
        return generateHello();
    }

    private static byte[] generateHello() {
        ClassWriter cw = new ClassWriter(0);
        // 定义对象头：版本号、修饰符、全类名、签名、父类、实现的接口
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "com/jjx/cloudnacosprovider/utils/HelloWorld", "", "java/lang/Object", null);
        // 添加方法：修饰符、方法名、描述符、签名、抛出的异常
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        // 执行指令：获取静态属性
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        // 加载常量
        mv.visitLdcInsn("HelloWorld!");
        // 调用方法
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        // 返回
        mv.visitInsn(Opcodes.RETURN);
        // 设置栈大小和局部变量表大小
        mv.visitMaxs(2, 1);
        // 方法结束
        mv.visitEnd();
        // 类完成
        cw.visitEnd();
        // 生成字节数组
        return cw.toByteArray();
    }

    static class MyClassLoader extends ClassLoader {
        //重写defineClass用于暴露该函数
        Class<?> defineClass(String name, byte[] b) {
            return super.defineClass(name, b, 0, b.length);
        }
    }

}
