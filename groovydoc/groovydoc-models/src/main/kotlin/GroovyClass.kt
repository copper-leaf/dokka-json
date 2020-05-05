package com.copperleaf.kodiak.groovy.models

import com.copperleaf.kodiak.common.AutoDocument
import com.copperleaf.kodiak.common.RichTextComponent
import com.copperleaf.kodiak.common.RichTextComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.common.DocComment
import com.copperleaf.kodiak.common.DocElement
import com.copperleaf.kodiak.common.TopLevel
import com.copperleaf.kodiak.common.fromDocList
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

/**
 * The docs for a single class. Includes a list of the constructors, methods, and fields in the class, as well as the
 * KDoc comment on the class.
 */
@Serializable
data class GroovyClass(
    @Transient
    val node: Any? = null,

    val `package`: String,
    val superclass: RichTextComponent?,
    val interfaces: List<RichTextComponent>,

    override val kind: String,
    override val name: String,
    override val id: String,
    override val modifiers: List<String>,
    override val comment: DocComment,

    val constructors: List<GroovyConstructor>,
    val methods: List<GroovyMethod>,
    val fields: List<GroovyField>,
    override val signature: List<RichTextComponent>,

    val enumItems: List<GroovyEnumConstant>
) : DocElement, AutoDocument, TopLevel {

    override val parents = listOfNotNull(superclass, *interfaces.toTypedArray())
    override val contexts = listOf(RichTextComponent(TYPE_NAME, `package`, `package`))

    @Transient
    override val nodes = listOf(
        fromDocList(::enumItems),
        fromDocList(::fields),
        fromDocList(::constructors),
        fromDocList(::methods)
    )

    @UseExperimental(UnstableDefault::class)
    fun toJson(): String {
        return Json.indented.stringify(GroovyClass.serializer(), this)
    }
}
