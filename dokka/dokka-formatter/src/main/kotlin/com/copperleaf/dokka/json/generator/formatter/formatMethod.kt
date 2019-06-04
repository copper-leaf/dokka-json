package com.copperleaf.dokka.json.generator.formatter

import com.copperleaf.dokka.json.models.KotlinMethod
import com.copperleaf.dokka.json.models.KotlinParameter
import com.copperleaf.dokka.json.models.KotlinReceiverType
import com.copperleaf.dokka.json.models.KotlinReturnType
import com.copperleaf.json.common.CommentComponent
import org.jetbrains.dokka.DocumentationNode
import org.jetbrains.dokka.NodeKind

val DocumentationNode.isMethod: Boolean get() = this.kind == NodeKind.Function

fun DocumentationNode.toMethod(): KotlinMethod {
    assert(this.isMethod) { "node must be a Function" }
    val modifiers = this.modifiers
    val parameters = this.parameters
    val receiverType = this.receiverType
    val returnType = this.returnType
    return KotlinMethod(
        this,
        this.simpleName,
        this.qualifiedName,
        this.contentText,
        this.contentTags,
        modifiers,
        parameters,
        receiverType,
        returnType,
        this.methodSignature(
            modifiers,
            parameters,
            receiverType,
            returnType
        )
    )
}

fun DocumentationNode.methodSignature(
    modifiers: List<String>,
    parameters: List<KotlinParameter>,
    receiverType: KotlinReceiverType?,
    returnType: KotlinReturnType
): List<CommentComponent> {
    val list = mutableListOf<CommentComponent>()

    list.addAll(modifiers.toModifierListSignature())
    list.add(CommentComponent("keyword", "fun "))

    list.addAll(this.toTypeParameterDeclarationSignature())

    if (receiverType != null) {
        list.addAll(receiverType.signature)
        list.add(CommentComponent("punctuation", "."))
    }

    list.add(CommentComponent("name", this.simpleName))
    list.addAll(parameters.toParameterListSignature())

    if (returnType.name != "Unit") {
        list.add(CommentComponent("punctuation", ": "))
        list.addAll(returnType.signature)
    }

    return list
}

val DocumentationNode.returnType: KotlinReturnType
    get() {
        val it = this.detail(NodeKind.Type)
        return KotlinReturnType(
            it,
            it.simpleName,
            it.qualifiedName,
            it.contentText("Return", null),
            this.contentTags,
            it.simpleType,
            it.qualifiedType,
            it.asType().toTypeSignature()
        )
    }

val DocumentationNode.receiverType: KotlinReceiverType?
    get() {
        val it = this.detailOrNull(NodeKind.Receiver)
        return if (it == null)
            null
        else
            KotlinReceiverType(
                it,
                it.simpleName,
                it.qualifiedName,
                it.contentText("Receiver", null),
                this.contentTags,
                it.simpleType,
                it.qualifiedType,
                it.asType().toTypeSignature()
            )
    }

