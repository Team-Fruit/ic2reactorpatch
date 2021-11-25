package net.teamfruit.ic2reactorpatch.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.teamfruit.ic2reactorpatch.asm.lib.DescHelper;
import net.teamfruit.ic2reactorpatch.asm.lib.MethodMatcher;
import net.teamfruit.ic2reactorpatch.asm.lib.RefName;

public class TileEntityNuclearReactorElectricVisitor extends ClassVisitor {

	private final MethodMatcher reactorSizeMatcher;
	private final MethodMatcher produceEnergyMatcher;

	public TileEntityNuclearReactorElectricVisitor(final String obfClassName, final ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.reactorSizeMatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, new Object[0]), RefName.name("getReactorSize"));
		this.produceEnergyMatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, new Object[0]), RefName.name("produceEnergy"));
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		if (name==null||desc==null)
			return parent;
		if (this.reactorSizeMatcher.match(name, desc))
			return new ReactorSizeHookMethodVisitor(parent);
		if (this.produceEnergyMatcher.match(name, desc))
			return new ProduceEnergyHookMethodVisitor(parent);
		return parent;
	}

	private class ReactorSizeHookMethodVisitor extends MethodVisitor {

		public ReactorSizeHookMethodVisitor(final MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitCode() {
			super.visitIntInsn(Opcodes.SIPUSH, 9);
			super.visitInsn(Opcodes.IRETURN);
		}

	}

	private class ProduceEnergyHookMethodVisitor extends MethodVisitor {
		public ProduceEnergyHookMethodVisitor(final MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitCode() {
			super.visitInsn(Opcodes.ICONST_1);
			super.visitInsn(Opcodes.IRETURN);
		}
	}

}
