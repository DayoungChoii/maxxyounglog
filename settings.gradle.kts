rootProject.name = "ordertoy"

include("order-domain:rds")
include("order-domain:redis")
include("order-app:api")
include("order-app:batch")
include("order-app:crm")
include("order-internal:async")
