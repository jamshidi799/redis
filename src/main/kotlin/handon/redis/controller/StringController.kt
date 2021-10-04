package handon.redis.controller

import handon.redis.controller.request.MGetRequest
import handon.redis.controller.request.SetRequest
import handon.redis.service.StringOperationServiceImpl
import org.springframework.web.bind.annotation.*

// TODO: write test and documention
@RestController
@RequestMapping("/string")
class StringController(private val stringOperationServiceImpl: StringOperationServiceImpl){

    @PostMapping("/set")
    fun set(@RequestBody body: SetRequest) {
        stringOperationServiceImpl.set(body.key, body.value, body.expiration)
    }


    @GetMapping("/get/{key}")
    fun get(@PathVariable key: String): Any? {
        return stringOperationServiceImpl.get(key)
    }

    @PostMapping("/get-set")
    fun getSet(@RequestBody body: SetRequest): Any? {
        return stringOperationServiceImpl.getSet(body.key, body.value)
    }


    @GetMapping("/m-get")
    fun mGet(@RequestBody body: MGetRequest): Any? {
        return stringOperationServiceImpl.multipleGet(body.keys)
    }
}