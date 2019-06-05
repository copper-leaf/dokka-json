package com.copperleaf.javadoc.json.models

import com.copperleaf.json.common.ModuleDoc
import com.copperleaf.json.common.fromDocList

/**
 * The result of executing Javadoc and transforming the results to JSON.
 */
class JavaRootDoc(
    val packages: List<JavaPackage>,
    val classes: List<JavaClass>
) : ModuleDoc {

    override val nodes = listOf(
        fromDocList(::packages),
        fromDocList(::classes)
    )

}
