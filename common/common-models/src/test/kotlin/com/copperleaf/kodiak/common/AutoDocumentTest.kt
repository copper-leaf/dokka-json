package com.copperleaf.kodiak.common

import com.copperleaf.kodiak.common.RichTextComponent.Companion.TEXT
import clog.Clog
import kotlin.test.BeforeTest
import kotlin.test.Test

class AutoDocumentTest {

    internal var receiver: TestTypeElement? = null
    internal lateinit var parameters: List<DocElement>
    internal lateinit var typeParameters: List<DocElement>
    internal lateinit var returnValue: TestTypeElement

    @BeforeTest
    internal fun setUp() {
        receiver = null
        parameters = listOf(
            doc("parameter", "p1", "kotlin.String"),
            doc("parameter", "p2", "kotlin.String"),
            doc("parameter", "p2", "kotlin.String")
        )
        typeParameters = emptyList()
        returnValue = type("returnValue", "returnValue", "kotlin.Int")
    }

    @Test
    fun testAutoDocument() {
        val underTest = TestMethodDoc(receiver, parameters, typeParameters, returnValue)
        underTest.nodes.forEach { Clog.d("node name=${it.name} with ${it.elements.size} elements") }
    }

// Helpers
// ----------------------------------------------------------------------------------------------------------------------

    internal fun doc(
        kind: String,
        name: String,
        id: String = name,
        commentText: String = "",
        commentTags: Map<String, String> = emptyMap()
    ): DocElement {
        return object : DocElement {
            override val kind = kind
            override val name = name
            override val id = id
            override val modifiers = emptyList<String>()
            override val comment: DocComment
                get() = DocComment(
                    components = listOf(RichTextComponent(TEXT, commentText)),
                    tags =
                    commentTags.mapValues {
                        CommentTag(
                            value = listOf(
                                RichTextComponent(
                                    TEXT,
                                    it.value
                                )
                            )
                        )
                    }
                )
            override val signature: List<RichTextComponent> = emptyList()
        }
    }

    internal fun type(
        kind: String,
        name: String,
        id: String = name,
        modifiers: List<String> = emptyList(),
        commentText: String = "",
        commentTags: Map<String, String> = emptyMap()
    ): TestTypeElement {
        return TestTypeElement(
            kind,
            name,
            id,
            modifiers,
            DocComment(
                listOf(RichTextComponent(TEXT, commentText)),
                commentTags.mapValues { CommentTag(value = listOf(RichTextComponent(TEXT, it.value))) }
            )
        )
    }
}

// Setup as data class with overridden method supplying nodes
// ----------------------------------------------------------------------------------------------------------------------

interface AutoDocument {
    val nodes: List<AutoDocumentNode>
}

internal data class TestMethodDoc(
    val receiver: TestTypeElement?,
    val parameters: List<DocElement>,
    val typeParameters: List<DocElement>,
    val returnValue: TestTypeElement
) : AutoDocument {
    override val nodes = listOf(
        fromDoc(::receiver),
        fromDocList(::parameters),
        fromDocList(::typeParameters),
        fromDoc(::returnValue)
    )
}

internal data class TestTypeElement(
    override val kind: String,
    override val name: String,
    override val id: String,
    override val modifiers: List<String>,
    override val comment: DocComment,

    override val typeName: String = name,
    override val typeId: String = id,
    override val signature: List<RichTextComponent> = emptyList()
) : ElementType
