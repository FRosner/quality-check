package net.sf.qualitycheck.immutableobject.generator;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import net.sf.qualitycheck.Check;
import net.sf.qualitycheck.immutableobject.domain.Annotation;
import net.sf.qualitycheck.immutableobject.domain.ImmutableInterfaceAnalysis;
import net.sf.qualitycheck.immutableobject.domain.Imports;
import net.sf.qualitycheck.immutableobject.domain.Interface;
import net.sf.qualitycheck.immutableobject.domain.InterfaceAnalysis;
import net.sf.qualitycheck.immutableobject.domain.Method;
import net.sf.qualitycheck.immutableobject.domain.Package;

@ThreadSafe
final class InterfaceAnalyzer {

	/**
	 * Analyzes the source code of an interface. The specified interface must not contain methods, that changes the
	 * state of the corresponding object itself.
	 * 
	 * @param code
	 *            source code of an interface which describes how to generate the <i>immutable</i>
	 * @return analysis result
	 */
	@Nonnull
	public static InterfaceAnalysis analyze(@Nonnull final String code) {
		Check.notNull(code, "code");

		final CompilationUnit unit = Check.notNull(SourceCodeReader.parse(code), "compilationUnit");
		final List<TypeDeclaration> types = Check.notEmpty(unit.getTypes(), "typeDeclarations");
		Check.stateIsTrue(types.size() == 1, "more than one interface declaration per compilation unit is not supported");

		final ClassOrInterfaceDeclaration type = (ClassOrInterfaceDeclaration) types.get(0);
		final Imports imports = SourceCodeReader.findImports(unit.getImports());
		final Package pkg = new Package(unit.getPackage().getName().toString());
		final List<Annotation> annotations = SourceCodeReader.findAnnotations(type.getAnnotations(), imports);
		final List<Method> methods = SourceCodeReader.findMethods(type.getMembers(), imports);
		final List<Interface> extendsInterfaces = SourceCodeReader.findExtends(type);
		final String interfaceName = type.getName();
		return new ImmutableInterfaceAnalysis(annotations, extendsInterfaces, imports.asList(), interfaceName, methods, pkg);
	}

	/**
	 * <strong>Attention:</strong> This class is not intended to create objects from it.
	 */
	private InterfaceAnalyzer() {
		// This class is not intended to create objects from it.
	}

}
