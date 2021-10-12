# redis
a clone of Redis with HTTP and gRPC controller implemented in kotlin with spring.

currently just support Strings and Lists as value but i will add Hashes and Sets in future.

### how it works
store key value pairs in multiple hashmap (one for Strings and another for Lists). taking snapshot every minute and also before
shutdown and save this in cassandra. exactly after each start, load these snapshots in our hashmaps.
equivalent of each http endpoint implemented as a rpc method.

### future plan
- [x] HTTP endpoints for commands
- [x] take snapshot and load it on startup
- [x] gRPC endpoints for commands
- [ ] add Sets and Hashes
- [ ] simulate redis-cli with golang(communicate with gRPC)
- [ ] add graceful shutdown
- [ ] implement redis-benchmark
