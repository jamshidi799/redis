package handon.redis.service

import java.time.Duration

interface StringOperationService {

    fun set(key: String, value: String, expiration: Duration?)

    fun get(key: String) : String?

    /**
     * Atomically sets key to value and returns the old value stored at key.
     * Returns an error when key exists but does not hold a string value.
     */
    fun getSet(key: String, value: String): String?

    /**
     * Returns the values of all specified keys.
     * For every key that does not hold a string value or does not exist, the special value nil is returned
     */
    fun multipleGet(keys: List<String>): Map<String, String?>
}