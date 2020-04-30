package com.copperleaf.kodiak.groovy.formatter

import com.copperleaf.kodiak.common.CommentComponent
import com.copperleaf.kodiak.common.CommentComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.groovy.models.GroovyClass
import org.codehaus.groovy.groovydoc.GroovyClassDoc
import org.codehaus.groovy.tools.groovydoc.SimpleGroovyDoc

fun GroovyClassDoc.toClassDoc(deep: Boolean): GroovyClass {
    val modifiers = listOf(this.modifiers()).filterNotNull()

    return GroovyClass(
        this,
        this.containingPackage().nameWithDots(),
        this.superclass()?.qualifiedTypeName(),
        this.interfaces()?.mapNotNull { it?.qualifiedTypeName() } ?: emptyList(),
        this.classKind,
        this.simpleTypeName(),
        this.qualifiedTypeName(),
        modifiers,
        this.getComment(),
        if (deep) this.constructors().filter { !it.isSuppressed() }.map { it.toConstructor(this) } else emptyList(),
        if (deep) this.methods().filter { !it.isSuppressed() }.map { it.toMethod() } else emptyList(),
        if (deep) this.fields().filter { !it.isSuppressed() }.map { it.toField() } else emptyList(),
        this.classSignature(
            modifiers
        ),
        if (deep && this.isEnum) this.enumConstants().map { it.toEnumConstant() } else emptyList()
    )
}

val GroovyClassDoc.classKind: String
    get() {
        return when {
            isInterface                        -> "interface"
            isAnnotationType                   -> "@interface"
            isEnum                             -> "enum"
            isExceptionClass()                 -> "class"
            this is SimpleGroovyDoc && isTrait -> "trait"
            else                               -> "class"
        }
    }

fun GroovyClassDoc.classSignature(
    modifiers: List<String>
): List<CommentComponent> {
    val list = mutableListOf<CommentComponent>()

    list.addAll(modifiers.toModifierListSignature())
    list.add(CommentComponent("name", "${this.classKind} "))
    list.add(CommentComponent(TYPE_NAME, "" + this.simpleTypeName(), "" + this.qualifiedTypeName()))
//    list.addAll(this.typeParameters().toWildcardSignature())

    if (this.isInterface) {
        val interfaces = this.interfaces()
        if (interfaces.isNotEmpty()) {
            list.add(CommentComponent("name", " extends "))
            interfaces.forEachIndexed { boundsIndex, type ->
                list.addAll(type.toTypeSignature())
                if (boundsIndex < interfaces.size - 1) {
                    list.add(CommentComponent("punctuation", ", "))
                }
            }
        }
    } else {
        val superclass = this.superclass()
        if (superclass != null) {
            list.add(CommentComponent("name", " extends "))
            list.addAll(superclass.toTypeSignature())
        }

        val interfaces = this.interfaces()
        if (interfaces.isNotEmpty()) {
            list.add(CommentComponent("name", " implements "))
            interfaces.forEachIndexed { boundsIndex, type ->
                list.addAll(type.toTypeSignature())
                if (boundsIndex < interfaces.size - 1) {
                    list.add(CommentComponent("punctuation", ", "))
                }
            }
        }
    }

    return list
}
