package handon.redis.controller

import handon.redis.controller.request.MGetRequest
import handon.redis.controller.request.SetRequest
import handon.redis.service.StringOperationService
import org.springframework.web.bind.annotation.*

// TODO: write test and documention
@RestController
@RequestMapping("/string")
class StringController(private val stringService: StringOperationService){

    @PostMapping("/set")
    fun set(@RequestBody body: SetRequest) {
        stringService.set(body.key, body.value, body.expiration)
    }


    @GetMapping("/get/{key}")
    fun get(@PathVariable key: String): Any? {
        return stringService.get(key)
    }

    @PostMapping("/get-set")
    fun getSet(@RequestBody body: SetRequest): Any? {
        return stringService.getSet(body.key, body.value)
    }


    @GetMapping("/m-get")
    fun mGet(@RequestBody body: MGetRequest): Any? {
        return stringService.multipleGet(body.keys)
    }
}