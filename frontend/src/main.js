import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import less from 'less';
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';
import '../my-theme/index.less';

Vue.use(less);
Vue.use(ViewUI);

Vue.config.productionTip = false;


new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');
