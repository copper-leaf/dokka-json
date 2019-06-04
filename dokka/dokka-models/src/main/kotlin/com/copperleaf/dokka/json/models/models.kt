package com.copperleaf.dokka.json.models

import com.copperleaf.json.common.CommentComponent
import com.copperleaf.json.common.CommentTag
import com.copperleaf.json.common.DocElement
import com.copperleaf.json.common.ElementType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JSON

interface KotlinClasslike : DocElement

interface KotlinMemberlike : DocElement {
    val modifiers: List<String>
}

/**
 * The result of executing Dokka and transforming the results to JSON.
 */
class KotlinRootdoc(
        val packages: List<KotlinPackageDoc>,
        val classes: List<KotlinClassDoc>
)

/**
 * The docs for a single class. Includes a list of the constructors, methods, and fields in the class, as well as the
 * KDoc comment on the class.
 */
@Serializable
data class KotlinClassDoc(
    @Transient
        val node: Any? = null,

    val `package`: String,
    override val kind: String,
    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,
    val modifiers: List<String>,
    val constructors: List<KotlinConstructor>,
    val methods: List<KotlinMethod>,
    val fields: List<KotlinField>,
    val extensions: List<KotlinMethod>,
    val signature: List<CommentComponent>
) : KotlinClasslike {
    companion object {
        fun fromJson(json: String): KotlinClassDoc {
            return JSON.parse(KotlinClassDoc.serializer(), json)
        }
    }

    fun toJson(): String {
        return JSON.indented.stringify(KotlinClassDoc.serializer(), this)
    }
}

/**
 * The docs for a single package. Includes a list of the classes in the package, as well as the KDoc comment on the
 * package. Class definitions only include metadata, but do not include information about their members.
 */
@Serializable
data class KotlinPackageDoc(
    @Transient
        val node: Any? = null,

    val classes: List<KotlinClassDoc>,
    val methods: List<KotlinMethod>,
    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>
) : DocElement {
    override val kind = "Package"

    companion object {
        fun fromJson(json: String): KotlinPackageDoc {
            return JSON.parse(KotlinPackageDoc.serializer(), json)
        }
    }

    fun toJson(): String {
        return JSON.indented.stringify(KotlinPackageDoc.serializer(), this)
    }
}

/**
 * The docs for a constructor of a class.
 */
@Serializable
data class KotlinConstructor(
    @Transient
        val node: Any? = null,

    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,
    override val modifiers: List<String>,
    val parameters: List<KotlinParameter>,
    val signature: List<CommentComponent>
) : KotlinMemberlike {
    override val kind = "Constructor"
}

/**
 * The docs for a method or function in a class.
 */
@Serializable
data class KotlinMethod(
    @Transient
        val node: Any? = null,

    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,
    override val modifiers: List<String>,

    val parameters: List<KotlinParameter>,
    val receiver: KotlinReceiverType? = null,
    val returnValue: KotlinReturnType,
    val signature: List<CommentComponent>
) : KotlinMemberlike {
    override val kind = "Method"
}

/**
 * The docs for a field or property in a class.
 */
@Serializable
data class KotlinField(
    @Transient
        val node: Any? = null,

    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,
    override val modifiers: List<String>,

    override val typeName: String,
    override val typeId: String,
    override val signature: List<CommentComponent>
) : KotlinMemberlike, ElementType {
    override val kind = "Field"
}

/**
 * The docs for a parameter of a constructor or method
 */
@Serializable
data class KotlinParameter(
    @Transient
        val node: Any? = null,

    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,

    override val typeName: String,
    override val typeId: String,
    val defaultValue: String?,
    override val signature: List<CommentComponent>

) : DocElement, ElementType {
    override val kind = "Parameter"
}

/**
 * The docs for a method return typeName
 */
@Serializable
data class KotlinReturnType(
    @Transient
        val node: Any? = null,

    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,

    override val typeName: String,
    override val typeId: String,
    override val signature: List<CommentComponent>
) : DocElement, ElementType {
    override val kind = "ReturnType"
}

/**
 * The docs for a method receiver typeName
 */
@Serializable
data class KotlinReceiverType(
    @Transient
        val node: Any? = null,

    override val name: String,
    override val id: String,
    override val commentComponents: List<CommentComponent>,
    override val commentTags: Map<String, CommentTag>,

    override val typeName: String,
    override val typeId: String,
    override val signature: List<CommentComponent>
) : DocElement, ElementType {
    override val kind = "ReceiverType"
}
