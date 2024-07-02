package dev.crmodders.puzzle.access_manipulator.transformers;

import dev.crmodders.puzzle.access_manipulator.pairs.FieldModifierPair;
import dev.crmodders.puzzle.access_manipulator.pairs.MethodModifierPair;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClassTransformerVisitor extends ClassVisitor {

    String className;

    protected ClassTransformerVisitor(String className, int api) {
        super(api);

        this.className = className;
    }

    @Override
    public FieldVisitor visitField(
            int access, String name, String desc, String signature, Object value) {

        Map<String, FieldModifierPair> pairs = AccessManipulatorTransformer.fieldsToModify.get(className);
        FieldModifierPair pair = pairs.get(name);
        if (pairs.containsKey(name)) {
            int newAccess = pair.modifier.updateFlags(access);
            return cv.visitField(newAccess, name, desc, signature, value);
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {

        List<MethodModifierPair> pairs = AccessManipulatorTransformer.methodsToModify.get(className);
        for (MethodModifierPair pair : pairs) {
            if (Objects.equals(pair.methodName, name) && Objects.equals(pair.methodDesc, descriptor)) {
                int newAccess = pair.modifier.updateFlags(access);
                return cv.visitMethod(newAccess, name, descriptor, signature, exceptions);
            }
        }
        return cv.visitMethod(access, name, descriptor, signature, exceptions);
    }
}