package com.copperleaf.kodiak.groovy.formatter

import com.copperleaf.kodiak.common.RichTextComponent
import com.copperleaf.kodiak.common.RichTextComponent.Companion.TEXT
import com.copperleaf.kodiak.common.RichTextComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.common.DocComment
import com.copperleaf.kodiak.groovy.models.GroovyPackage
import org.codehaus.groovy.groovydoc.GroovyPackageDoc

fun GroovyPackageDoc.toPackageDoc(deep: Boolean): GroovyPackage {
    return GroovyPackage(
        this,
        this.nameWithDots(),
        this.nameWithDots(),
        "",
        emptyList(),
        DocComment(
            this.description().parseCommentToValues().commentText,
            emptyMap()
        ),
        this.packageSignature(),
        if (deep) this.allClasses().filter { !it.isSuppressed() }.map { it.toClassDoc(false) } else emptyList(),
        emptyList()
    )
}

fun GroovyPackageDoc.packageSignature(): List<RichTextComponent> {
    return listOf(
        RichTextComponent(TEXT, "package "),
        RichTextComponent(TYPE_NAME, this.nameWithDots(), this.nameWithDots())
    )
}
