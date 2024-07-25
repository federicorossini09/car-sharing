grails.plugin.springsecurity.useBasicAuth = true

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'car.sharing.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'car.sharing.UserRole'
grails.plugin.springsecurity.authority.className = 'car.sharing.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/', access: ['permitAll']],
        [pattern: '/error', access: ['permitAll']],
        [pattern: '/index', access: ['permitAll']],
        [pattern: '/index.gsp', access: ['permitAll']],
        [pattern: '/shutdown', access: ['permitAll']],
        [pattern: '/assets/**', access: ['permitAll']],
        [pattern: '/**/js/**', access: ['permitAll']],
        [pattern: '/**/css/**', access: ['permitAll']],
        [pattern: '/**/images/**', access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],
        [pattern: '/registration', access: ['permitAll']],
        [pattern: '/registration/index', access: ['permitAll']],
        [pattern: '/h2-console*/**', access: ['permitAll']],
]

grails.plugin.springsecurity.interceptUrlMap = [
        '/h2-console*/**': ['permitAll']
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/**', filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/home'

log4j = {

    // Enable Hibernate SQL logging with param values
    trace 'org.hibernate.type'
    debug 'org.hibernate.SQL'
    //the rest of your logging config
    // ...
}

