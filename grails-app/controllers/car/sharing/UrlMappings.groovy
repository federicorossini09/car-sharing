package car.sharing

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'login', action: 'auth')
        "/home"(controller: 'home')
        get "/registration"(controller: 'registration', action: 'index')
        post "/registration"(controller: 'registration', action: 'registration')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
