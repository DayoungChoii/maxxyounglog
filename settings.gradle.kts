rootProject.name = "ordertoy"

include("domain:rds")
include("domain:redis")
include("app:api")
include("app:batch")
include("internal:async")

