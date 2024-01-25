module.exports = {
    configureWebpack: {
        externals: {
            AMap: 'AMap',
            Loca: 'Loca',
            AMapUI: 'AMapUI',
        },
    },
    publicPath: '',
    css: {
        extract: true, // 是否使用css分离插件 ExtractTextPlugin
        sourceMap: false, // 开启 CSS source maps?
        loaderOptions: {
            less: {
                javascriptEnabled: true, //less 配置
            },
        }, // css预设器配置项
    },
};
