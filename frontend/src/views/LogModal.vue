<template>
    <div>
        <Timeline style="height: 400px; overflow-y: scroll;padding-left: 20px">
            <TimelineItem v-for="(item, index) in Array.prototype.reverse.call(msgList)" :key="index + item">
                <p class="time">{{item.title}}</p>
                <p class="content">{{item.desc}}</p>
            </TimelineItem>
        </Timeline>
    </div>
</template>

<script>
    export default {
        name: "log-modal",
        props: [],
        data: () => {
            return {
                msgList: [
                    {title: "2020-09-16更新", desc: "增加上传轮廓时，选择文件或者文本时，可支持二进制轮廓，但是必须已'0x'开头"},
                    {title: "2020-09-22更新", desc: "增加上传轮廓添加读取数据库功能"},
                    {title: "2020-11-04更新", desc: "增加折线显示"},
                    {title: "2020-11-06更新", desc: "增加轮廓编辑功能"},
                    {title: "2020-11-06更新", desc: "优化操作模式"},
                    {title: "2020-12-11更新", desc: "添加错误信息通知"},
                    {title: "2020-12-15更新", desc: "优化界面显示，添加右键菜单"},
                    {title: "2020-12-17更新", desc: "添加意见反馈功能"},
                    {title: "2021-01-08更新", desc: "添加测距功能"},
                    {title: "2021-06-08更新", desc: "v2.0更新，新增了少量功能，优化部分细节"},
                    {title: "2021-06-10更新", desc: "打海量点和轮廓支持Google S2索引，直接将原有经纬度点改成索引ID即可"},
                    {title: "2022-04-29更新", desc: "打海量点和轮廓支持WKT数据，直接将原有经纬度点改成WKT即可"},
                    {title: "2022-05-10更新", desc: "打海量点和轮廓经纬度分隔符可为任意字符（小数点除外）"},
                    {title: "2023-03-16更新", desc: "添加分享功能，但是有效时间只能30分钟"},
                ],
            }
        },
        methods: {},
    }
</script>

<style scoped>

</style>
