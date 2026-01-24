export const Paths = {
    default: {
        home: '/',
    },

    anonymous: {
        home: '/',

        login: '/login',
        register: '/register',
    },

    client: {
        detailedUser: '/users/details/:login',

        createAllocations: '/allocations/create',

        listResources: '/resources',
    },

    manager: {
        detailedUser: '/users/details/:login',

        listAllocations: '/allocations',
        createAllocations: '/allocations/create',

        listResources: '/resources',
        createResource: '/resources/create',
    },

    administrator: {
        listUsers: '/users',
        createUser: '/users/create',
        detailedUser: '/users/details/:login',

        listResources: '/resources',
        createResource: '/resources/create',

        listAllocations: '/allocations',
        createAllocations: '/allocations/create'
    }
}