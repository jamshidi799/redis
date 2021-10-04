package handon.redis.service

interface ListOperationService {

    /**
     * Sets the list element at index to element
     * An error is returned for out of range indexes.
     */
    fun listSet(key: String, index: Int, element: String): String

    /**
     * Returns the element at index index in the list stored at key
     *  The index is zero-based, so 0 means the first element, 1 the second element and so on.
     *  Negative indices can be used to designate elements starting at the tail of the list.
     *  Here, -1 means the last element, -2 means the penultimate and so forth.
     */
    fun listIndex(key: String, index: Int): String?

    /**
     * Insert all the specified values at the tail of the list stored at key
     */
    fun rightPush(key: String, vararg elements: String): Int

    /**
     * Insert all the specified values at the first of the list stored at key
     */
    fun leftPush(key: String, vararg elements: String): Int

    /**
     * Removes and returns the first elements of the list stored at key.
     */
    fun leftPop(key: String): String?

    /**
     * Removes and returns the first elements of the list stored at key.
     */
    fun leftPop(key: String, count: Int): List<String>
}