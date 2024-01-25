export function objToFormData(config) {
    //对象转formdata格式
    let formData = new FormData();
    let obj = config.data;
    let arrayKey = config.arrayKey;
    for (var i in obj) {
        if (Array.isArray(obj[i])) {
            obj[i].map(item => {
                if (!arrayKey) {
                    formData.append(i, item)
                } else {
                    formData.append(i + '[]', item)
                }
            })
        } else if (Object.prototype.toString.call(obj[i]) === '[object Object]') {
            formData.append(i, JSON.stringify(obj[i]))
        } else {
            formData.append(i, obj[i])
        }
    }
    return formData;
}

export function randomSvg(color) {
    let title = readSvg(require('../assets/point.svg'))
    let newSvg = title.replace(/#(?:[0-9a-fA-F]{6})/g, color)
    return 'data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(newSvg)));
}

export function readSvg(filePath) {
    // 创建一个新的xhr对象
    let xhr = null
    if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest()
    } else {
        return null
    }
    const okStatus = document.location.protocol === 'file' ? 0 : 200
    xhr.open('GET', filePath, false)
    xhr.overrideMimeType('image/svg+xml')
    xhr.send(null)
    return xhr.status === okStatus ? xhr.responseText : null
}

export function int2HexRevers(int) {
    int = parseInt(int)
    let hex = strPad(int.toString(16))
    let hexStr = ''
    for (let i = hex.length - 1; i > 0; i -= 2) {
        hexStr += hex.substring(i - 1, i + 1)
    }
    return hexStr.toUpperCase()
}

function strPad(hex) {
    let zero = '00000000';
    let tmp = 8 - hex.length;
    return zero.substr(0, tmp) + hex;
}

export function revertColor(color) {
    return color.substring(0, 1) + color.substring(1, color.length).split("").reverse().join("")
}


export function getPathDistance(lnglatPath) {
    let distance = 0;
    for (let i = 0; i < lnglatPath.length - 1; i++) {
        distance += getDistance(lnglatPath[i][0], lnglatPath[i][1], lnglatPath[i + 1][0], lnglatPath[i + 1][1])
    }
    return distance
}

export function getDistance(lng1, lat1, lng2, lat2) {
    lat1 = lat1 || 0;
    lng1 = lng1 || 0;
    lat2 = lat2 || 0;
    lng2 = lng2 || 0;

    const rad1 = lat1 * Math.PI / 180.0;
    const rad2 = lat2 * Math.PI / 180.0;
    const a = rad1 - rad2;
    const b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
    const r = 6378137;
    return r * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)));
}
