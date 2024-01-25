import axios from "../axios"

export function post(url, datas, contentType = 'multipart/form-data') {
    return axios({
        url: url,
        method: "post",
        xhrFields: {withCredentials: true},
        crossDomain: true,
        headers: {
            'Content-Type': contentType //设置请求头请求格式form
        },
        data: datas
    })
}

export function postJson(url, datas) {
    return post(url, datas, 'application/json')
}

export function get(url) {
    return axios({
        url: url,
        method: "get",
    })
}