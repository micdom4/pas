export const Paths = {
    default: {
        home: '/',

        listUsers: '/users',
        createUser: '/users/create',
        detailedUser: '/users/details/:login',

        listResources: '/resources',
        createResource: '/resources/create',

        listAllocations: '/allocations',
        createAllocations: '/allocations/create'
    },

    anonymous: {
        home: '/',

        login: '/login',
        register: '/register',
    }
}