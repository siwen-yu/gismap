<template>
    <div class="map-model-class">
        <Form :model="formItem" :label-width="90" inline>
            <FormItem label="数据类型">
                <RadioGroup v-model="fileType">
                    <Radio label="text">字符串</Radio>
                    <Radio label="file">文件</Radio>
                </RadioGroup>
            </FormItem>
            <FormItem label="数据内容">
                <Input v-if="fileType==='text'" v-model="fileText" type="textarea"
                       placeholder="经纬度/16进制轮廓/S2索引/WKT"
                       :rows="3"
                       style="width: 450px"/>
                <Upload v-else-if="fileType==='file'" :action=actionUrl :before-upload="handleUpload"
                        style="display: inline">
                    <Input search v-model="filename" enter-button="上传" disabled placeholder="上传文件"/>
                </Upload>
            </FormItem>
            <FormItem label="打点类型">
                <RadioGroup v-model="formItem.type">
                    <Radio label="point">海量点</Radio>
                    <Radio label="polygon">轮廓</Radio>
                    <!--                    <Radio label="clusterer">点聚集</Radio>-->
                    <Radio label="polyline">折线</Radio>
                    <Radio label="heatmap">热力图</Radio>
                    <Radio label="track">轨迹</Radio>
                </RadioGroup>
            </FormItem>
            <br>
            <FormItem label="附加配置">
                <ColorPicker v-model="myColor" size="small" style="vertical-align: middle;"/>
                <Checkbox
                    v-if="formItem.type === 'point' || formItem.type === 'polygon' || formItem.type === 'polyline' || formItem.type === 'track'"
                    style="margin-left: 24px;vertical-align: middle" v-model="randomColor">随机
                </Checkbox>
                <Checkbox
                    v-if="formItem.type === 'point' || formItem.type === 'polygon' || formItem.type === 'polyline' || formItem.type === 'track'"
                    style="margin-left: 24px;vertical-align: middle"
                    v-model="autoIncrementName">自动编号
                </Checkbox>
                <Checkbox
                    v-if="formItem.type === 'point' || formItem.type === 'polygon' || formItem.type === 'polyline'"
                    style="margin-left: 24px;vertical-align: middle" v-model="pointShowName">显示所有名字
                </Checkbox>
            </FormItem>
            <br>
            <FormItem label="数据示例">
                <template v-if="formItem.type === 'point' || formItem.type === 'track'">
                    <Tooltip :max-width="400" transfer placement="right-start"
                             content="一行代表一个点，经度在前，纬度在后，中间用任意字符分隔，其后接名字(可选);若需自定义标注点颜色，则用'$'符分隔(仅支持字符串输入)">
                        96.0288,32.9266|红色点标注$#FF0000
                        <br>
                        96.1288,32.8266|黄色点标注$#FFFF00
                    </Tooltip>
                </template>
                <template v-else-if="formItem.type === 'polygon' || formItem.type === 'polyline'">
                    <Tooltip :max-width="400" transfer placement="right-start"
                             content="名称(可选)和经纬度序列用竖线分隔，每个点经度在前，纬度在后，多个点任意字符隔开;若需自定义标注折线颜色，将自定义颜色放在名称尾部，用'$'符分隔(仅支持字符串输入)">
                        名称1$#FF0000|105.56,37.1645;105.5874,37.1630;105.4874,37.2630
                        <br>
                        $#FFFF00|POLYGON((95.5 37.1,95.6 37.2,95.6 37.1,95.5 37.1))
                    </Tooltip>
                </template>
                <template v-else-if="formItem.type === 'heatmap'">
                    <Tooltip :max-width="400" transfer placement="right-start"
                             content="一行代表一个点，经度在前，纬度在后，中间用任意字符隔开，竖线后接个数">
                        96.0288,32.9266|13
                        <br>
                        96.1288,32.8266|21
                    </Tooltip>
                </template>
            </FormItem>
            <br>
            <FormItem label="坐标系">
                <Select v-model="formItem.coordinate" filterable style="width: 240px">
                    <Option v-for="item in coordinateList" :value="item.value" :key="item.value" :label="item.label">
                        <span>{{ item.label }}</span>
                        <div style="float:right;color:#515a6e;">{{ item.value }}</div>
                    </Option>
                </Select>
            </FormItem>
            <FormItem label="格式" :label-width="60">
                <Select v-model="formItem.format" filterable style="width: 120px">
                    <Option value="double" label="浮点型"></Option>
                    <Option value="int" label="整型"></Option>
                </Select>
            </FormItem>
        </Form>
    </div>
</template>

<script>

export default {
    name: "map-modal",
    computed: {},
    props: [],
    data: () => {
        return {
            actionUrl: "/",
            fileType: "text",
            fileText: "",
            filename: "",
            file: new Blob(),
            myColor: '#ff0000',
            formItem: {
                type: 'point',
                bigType: 'point',
                coordinate: 'WGS84',
                format: 'double',
            },
            pointShowName: false,
            randomColor: false,
            autoIncrementName: false,
            polygonHtml: "",
            coordinateList: [
                {
                    label: "GPS地心坐标系",
                    value: "WGS84"
                },
                {
                    label: "火星坐标系(腾讯/高德)",
                    value: "GCJ02"
                },
                {
                    label: "百度坐标系(百度)",
                    value: "BD09"
                }
            ],
        }
    },
    watch: {
        'formItem.type'(newVal) {
            if (newVal === "point" || newVal === "heatmap" || newVal === "track") {
                this.formItem.bigType = 'point'
            } else if (newVal === "polygon" || newVal === "polyline") {
                this.formItem.bigType = 'polygon'
            }
        }
    },
    methods: {
        handleUpload(file) {
            this.file = file
            this.filename = file.name
            return false;
        },
    },
}
</script>

<style>
.map-model-class .ivu-form-item {
    margin-bottom: 18px;
}
</style>
