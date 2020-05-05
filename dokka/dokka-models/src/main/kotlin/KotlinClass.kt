package com.copperleaf.kodiak.kotlin.models

import com.copperleaf.kodiak.common.AutoDocument
import com.copperleaf.kodiak.common.RichTextComponent
import com.copperleaf.kodiak.common.RichTextComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.common.DocComment
import com.copperleaf.kodiak.common.DocElement
import com.copperleaf.kodiak.common.TopLevel
import com.copperleaf.kodiak.common.fromDoc
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
data class KotlinClass(
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

    val constructors: List<KotlinConstructor>,
    val methods: List<KotlinMethod>,
    val fields: List<KotlinField>,
    val extensions: List<KotlinMethod>,
    override val signature: List<RichTextComponent>,

    val companionObject: KotlinClass?,
    val enumItems: List<KotlinEnumConstant>
) : DocElement, AutoDocument, TopLevel {

    override val parents = listOfNotNull(superclass, *interfaces.toTypedArray())
    override val contexts = listOf(RichTextComponent(TYPE_NAME, `package`, `package`))

    @Transient
    override val nodes = listOf(
        fromDocList(::enumItems),
        fromDocList(::fields),
        fromDocList(::constructors),
        fromDocList(::methods),
        fromDocList(::extensions),
        fromDoc(::companionObject)
    )

    @UseExperimental(UnstableDefault::class)
    companion object {
        fun fromJson(json: String): KotlinClass {
            return Json.parse(KotlinClass.serializer(), json)
        }
    }

    @UseExperimental(UnstableDefault::class)
    fun toJson(): String {
        return Json.indented.stringify(KotlinClass.serializer(), this)
    }
}
