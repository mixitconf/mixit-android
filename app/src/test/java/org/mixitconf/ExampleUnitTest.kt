package org.mixitconf

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val speakers = arrayListOf<String>("id1", "id2")
        assertEquals(speakers.joinToString(","), "id1,id2")
    }



}
