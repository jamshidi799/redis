package handon.redis.controller

import handon.redis.controller.request.ListPushRequest
import handon.redis.controller.request.ListSetRequest
import handon.redis.service.ListOperationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/list")
class ListController(private val listOperationService: ListOperationService) {

    @PostMapping("/lSet")
    fun listSet(@RequestBody body: ListSetRequest): String {
        return listOperationService.listSet(body.key, body.index, body.element)
    }

    @GetMapping("/lIndex")
    fun listIndex(@RequestParam("key") key: String, @RequestParam("index") index: Int): String? {
        return listOperationService.listIndex(key, index)
    }

    @PostMapping("/rPush/{key}")
    fun rightPush(@PathVariable key: String, @RequestBody body: ListPushRequest): Int {
        return listOperationService.rightPush(key, *body.elements.toTypedArray())
    }

    @PostMapping("/lPush/{key}")
    fun leftPush(@PathVariable key: String, @RequestBody body: ListPushRequest): Int {
        return listOperationService.leftPush(key, *body.elements.toTypedArray())
    }

    @DeleteMapping("/rPush/{key}")
    fun leftPop(@PathVariable key: String): String? {
        return listOperationService.leftPop(key)
    }

    @DeleteMapping("/rPush/{key}")
    fun leftPopWithCount(@PathVariable key: String, @RequestParam("count") count: Int): List<String> {
        return listOperationService.leftPop(key, count)
    }

}