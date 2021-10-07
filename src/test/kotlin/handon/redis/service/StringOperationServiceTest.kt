package handon.redis.service

import handon.redis.repository.inMemory.InMemoryStringRepository
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration


internal class StringOperationServiceTest {

    private val repository = InMemoryStringRepository()
    private val underTest = StringOperationServiceImpl(repository)  // todo: serviceImpl?

    @Test
    fun set() {
        val key = "1"
        val value = "test"

        underTest.set(key, value, null)

        assertEquals(value, repository.map[key]?.value)
    }

    @Test
    fun setAndGetWhenKeyExpired() {
        val key = "1"
        val value = "test"
        val expriation = Duration.ofMillis(50)
        underTest.set(key, value, expriation)
        assertEquals(value, underTest.get(key))
        Thread.sleep(50)
        assertEquals(null, underTest.get(key))
    }

    @Test
    fun get() {
        val key = "1"
        val value = "test"

        underTest.set(key, value, null)

        assertEquals(value, underTest.get(key))
    }

    @Test
    fun getIfKeyDoesNotExist() {
        val key = "1"
        val expectedValue = null

        assertEquals(expectedValue, underTest.get(key))
    }


    @Test
    fun getSet() {
        val key = "1"
        val value = "test"
        val newValue = "new_test"

        underTest.set(key, value, null)

        val returnedValue = underTest.getSet(key, newValue)

        assertEquals(value, returnedValue)
    }

    @Test
    fun getSetIfKeyNotExist() {
        val key = "1"
        val newValue = "new_test"

        val returnedValue = underTest.getSet(key, newValue)
        val expectedValue = null

        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun multipleGet() {
        val keys = listOf("1", "2", "3")
        val values = listOf(2, 4, 6)
        for (i in 1..5) underTest.set(i.toString(), (i * 2).toString(), null)

        val expected = keys.withIndex().associateBy ({it.value}, {values[it.index].toString()})

        assertEquals(expected, underTest.multipleGet(keys))
    }

    @Test
    fun multipleGetIfSomeKeyDoesNotExist() {
        val keys = listOf("1", "2", "3", "10")
        val values = listOf("2", "4", "6", null)
        for (i in 1..5) underTest.set(i.toString(), (i * 2).toString(), null)

        val expected = keys.withIndex().associateBy ({it.value}, {values[it.index]})

        assertEquals(expected, underTest.multipleGet(keys))
    }
}