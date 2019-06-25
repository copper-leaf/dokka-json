package com.copperleaf.kodiak.groovy.groovy

/**
 * This is the `TestGroovyClass` comment text. It contains <code>code snippets</code>, <b>bold text tags</b>, and
 * also **bold markdown things**.
 */
class TestGroovyClass2 {

    /**
     * This is a field comment
     */
    public String stringField = ""

    /**
     * This is a method which returns a string
     *
     * @return the stringField value
     */
    String methodReturningString() {
        return stringField
    }


    /**
     * This is the comment text
     *
     * @param a1 the a1 to process
     * @param a2 the a2 to process
     * @param a3 the a3 to process
     * @return the value to return
     */
    String methodWithParametersReturningString(String a1, int a2, Object a3) {
        return stringField
    }


}