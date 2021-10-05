package handon.redis.service

import handon.redis.repository.inMemory.InMemoryListRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class ListOperationServiceImplTest {

    private val repository = InMemoryListRepository()
    private val underTest = ListOperationServiceImpl(repository)

    @Test
    fun listSet() {
        val key = "key"
        val index = 0
        val value = "old"
        val newValue = "new"

        underTest.rightPush(key, value)
        underTest.listSet(key, index, newValue)
        assertEquals(newValue, underTest.listIndex(key, index))
    }

    @Test
    fun listIndex() {
        val key = "key"
        val index = 0
        val value = "old"

        underTest.rightPush(key, value)
        assertEquals(value, underTest.listIndex(key, index))
    }

    @Test
    fun listIndexWithNegativeIndedx() {
        val key = "key"
        val index = -1
        val values = listOf("1", "2", "3", "4")
        underTest.rightPush(key, *values.toTypedArray())

        val expected = "4"

        assertEquals(expected, underTest.listIndex(key, index))
    }

    @Test
    fun listIndexWithGreaterThanListSizeIndex() {
        val key = "key"
        val index = 10
        val values = listOf("1", "2", "3", "4")
        underTest.rightPush(key, *values.toTypedArray())

        assertThrows(IndexOutOfBoundsException::class.java) { underTest.listIndex(key, index) }
    }

    @Test
    fun listIndexWithVeryLargeNegativeIndedx() {
        val key = "key"
        val index = -10
        val values = listOf("1", "2", "3", "4")
        underTest.rightPush(key, *values.toTypedArray())

        assertThrows(IndexOutOfBoundsException::class.java) { underTest.listIndex(key, index) }

    }

    @Test
    fun rightPushOverall() {
        val key = "key"
        val values = listOf("1", "2", "3", "4")
        var index = 2

        assertEquals(values.size, underTest.rightPush(key, *values.toTypedArray()))
        var expected = "3"

        assertEquals(expected, underTest.listIndex(key, index))

        assertEquals(values.size * 2, underTest.rightPush(key, *values.toTypedArray()))

        index = 5
        expected = "2"
        assertEquals(expected, underTest.listIndex(key, index))

    }

    @Test
    fun leftPushOverall() {
        val key = "key"
        val values = listOf("1", "2", "3", "4")
        var index = 2

        assertEquals(values.size, underTest.leftPush(key, *values.toTypedArray()))
        var expected = "3"

        assertEquals(expected, underTest.listIndex(key, index))

        assertEquals(values.size * 2, underTest.leftPush(key, *values.toTypedArray()))

        index = 5
        expected = "2"
        assertEquals(expected, underTest.listIndex(key, index))
    }

    @Test
    fun leftPop() {
        val key = "key"
        val values = listOf("1", "2", "3", "4")
        underTest.rightPush(key, *values.toTypedArray())

        val expected = "1"
        assertEquals(expected, underTest.leftPop(key))
    }

    @Test
    fun leftMultiPop() {
        val key = "key"
        val values = listOf("1", "2", "3", "4")
        val count = 2
        underTest.rightPush(key, *values.toTypedArray())

        val expected = listOf("1", "2")
        assertEquals(expected, underTest.leftPop(key, count))
    }
}