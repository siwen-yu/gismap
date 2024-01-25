import axios from "axios";
import qs from 'qs'
import {objToFormData} from "../common/utils";
import {Message, Spin} from "view-design";

let config = {
    baseURL: 'http://localhost:8088/', 		      // 后台服务地址
    timeout: 60000, 		      // 请求超时时间1分钟
    responseType: "json",
    withCredentials: true,
}

const instance = axios.create(config)

instance.interceptors.request.use((config) => {
    if (config.headers["Content-Type"] === "application/x-www-form-urlencoded") {
        /*数据转换: axios post方式默认是json格式提交数据，如果使用application/x-www-form-urlencoded数据格式提交，要用qs.stringify()进行转换,个人建议不在拦截器中全局配置，因为不够灵活，还有一点是，如果
      设置了重新请求的配置，那么重新请求时，请求体中的config里面的传参就会被再次进行qs.stringify()转 换，会使得参数丢失，造成请求失败。*/
        config.data = qs.stringify(config.data)
    } else if (config.headers["Content-Type"] === "multipart/form-data") {
        /*数据转换: axios post方式默认是json格式提交数据，如果使用application/x-www-form-urlencoded数据格式提交，要用qs.stringify()进行转换,个人建议不在拦截器中全局配置，因为不够灵活，还有一点是，如果
      设置了重新请求的配置，那么重新请求时，请求体中的config里面的传参就会被再次进行qs.stringify()转 换，会使得参数丢失，造成请求失败。*/
        config.data = objToFormData(config)
    }
    return config;
}, (error) => {
    Message.error('请求失败：' + error)
    return Promise.reject(error)
})

instance.interceptors.response.use((res) => {
    if (res.status === 200) {
        return Promise.resolve(res)
    } else {
        Spin.hide()
        Message.error('响应失败：' + res.status)
    }
    return res;
}, (error) => {
    Spin.hide()
    if (error.isAxiosError) {
        Message.error('响应失败：请联系管理员!')
    } else {
        Message.error('网络错误：请联系管理员!')
    }
    return Promise.reject(error)
})
// 4. 导出
export default instance
