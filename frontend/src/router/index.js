import Vue from 'vue'
import VueRouter from 'vue-router'
import Map from '../views/Map'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'map',
        component: Map,
        meta: {
            title: 'upos-工具列表'
        }
    }
]

const router = new VueRouter({
    routes
})

router.afterEach(to => {
    if (to.meta.title) {
        document.title = to.meta.title
    }
})

export default router
