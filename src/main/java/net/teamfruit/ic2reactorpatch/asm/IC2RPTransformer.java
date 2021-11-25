package net.teamfruit.ic2reactorpatch.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.IClassTransformer;
import net.teamfruit.ic2reactorpatch.asm.lib.VisitorHelper;
import net.teamfruit.ic2reactorpatch.asm.lib.VisitorHelper.TransformProvider;

public class IC2RPTransformer implements IClassTransformer {

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		if (bytes==null||name==null||transformedName==null)
			return bytes;

		if (transformedName.equals("ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric"))
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {

				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					System.out.println("IC2ReactorPatch is trying to patch");
					return new TileEntityNuclearReactorElectricVisitor(name, cv);
				}

			});

		return bytes;
	}

}
