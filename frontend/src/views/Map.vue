<template>
    <div class="amap-page-container">
        <div id="container" class="amap-demo" tabindex="0"></div>
        <div class="toolbar">
            <ButtonGroup vertical>
                <Button icon="md-thumbs-up" @click="feedbackModal = true"></Button>
                <Button icon="md-time" @click="logModal = true"></Button>
            </ButtonGroup>
        </div>
        <div
            style="position: absolute;top: 20px;width: auto;left: 16px;right: 0;display: flex;"
        >

            <Select
                v-model="placeKeyWords"
                placeholder="查找地名"
                style="width: 300px"
                clearable
                filterable
                :remote-method="searchPlace"
                :loading="placeSearching"
                @on-change="selectPlace"
            >
                <Option
                    v-for="(item, index) in placeList"
                    :key="index + item.location"
                    :value="item.location.lng + '_' + item.location.lat"
                >{{ item.name }}
                </Option>
            </Select>
            <Dropdown @on-click="clearType" style="margin-left: 12px">
                <ButtonGroup>
                    <Button type="primary">
                        清除
                        <Icon type="ios-arrow-down"></Icon>
                    </Button>
                    <Button
                        type="primary"
                        icon="md-trash"
                        @click="clearType('all')"
                    ></Button>
                </ButtonGroup>
                <DropdownMenu slot="list">
                    <DropdownItem name="point">海量点</DropdownItem>
                    <DropdownItem name="polygon">轮廓/折线</DropdownItem>
                    <DropdownItem name="heatmap">热力图</DropdownItem>
                </DropdownMenu>
            </Dropdown>
            <Button type="primary" @click="openModal = true" style="margin-left: 12px"
            >开始打点
            </Button
            >
        </div>
        <Modal
            v-model="openModal"
            title="上传经纬度数据"
            width="600"
            @on-ok="upload"
            :styles="{ top: '60px' }"
            draggable
        >
            <map-modal ref="modal"></map-modal>
        </Modal>
        <Drawer
            width="360"
            :value="polyEditName != null"
            :mask="false"
            @on-close="closePolyEdit(true)"
            class-name="my-ivu-drawer-wrap"
        >
            <div slot="header">{{ polyEditName }}</div>
            <poly-drawer
                ref="polyDrawer"
                :polyEditor="polyEditor"
                :name="polyEditName"
            ></poly-drawer>
        </Drawer>
        <Modal
            v-model="feedbackModal"
            title="意见反馈"
            width="600"
            ok-text="提交"
            @on-ok="submitFeedback"
            :styles="{ top: '60px' }"
            draggable
            :loading="feedbackLoading"
        >
            <feedback-modal ref="feedbackModal"></feedback-modal>
        </Modal>
        <Modal
            v-model="logModal"
            title="更新日志"
            width="600"
            :styles="{ top: '60px' }"
            footer-hide
            draggable
        >
            <log-modal ref="logModal"></log-modal>
        </Modal>
    </div>
</template>

<script>
import MapModal from './MapModal';
import FeedbackModal from './FeedbackModal';
import PolyDrawer from './PolyDrawer';
import LogModal from './LogModal';
import {post} from '../api';
import {gcj02towgs84} from '../common/CoordConvert';
import {getPathDistance, randomSvg, revertColor} from '../common/utils';

const randomHex = () =>
    `#${Math.floor(Math.random() * 0xffffff)
        .toString(16)
        .padEnd(6, '0')}`;
export default {
    name: 'Map',
    computed: {},
    components: {MapModal, PolyDrawer, FeedbackModal, LogModal},
    mounted() {
        this.init();
    },
    data() {
        return {
            gMap: null,
            openModal: false,
            logModal: false,
            massList: [],
            polygonList: [],
            marker: null,
            infoWindow: null,
            heatmap: null,
            text: null,
            polyEditor: null,
            polyEditName: null,
            lnglat: null,
            feedbackModal: false,
            feedbackLoading: true,
            rangingTool: null,
            openRangingTool: false,
            placeSearch: null,
            placeList: [],
            placeKeyWords: '',
            placeSearching: false,
            placeSearchMarker: null,
            pointLabelsLayers: [],
            polygonLabelsLayers: [],
            nameColorDelimiter: '$', //打点名称与颜色分隔符
            pathSimplifier: null,
            simplifierPathData: [],
            simplifierPathImg: null,
            keyDic: {},
        };
    },
    methods: {
        init: function () {
            this.$Message.config({
                top: 50,
                duration: 10,
            });
            const mapOptions = {
                resizeEnable: true,
                doubleClickZoom: false,
                keyboardEnable: false,
                zoom: 10, //设置地图显示的缩放级别
                zooms: [3, 20],
                expandZoomRange: true,
            };
            this.gMap = new AMap.Map('container', mapOptions);

            this.gMap.plugin(
                [
                    'AMap.Scale',
                    'AMap.MapType',
                    'AMap.Heatmap',
                    'AMap.PolyEditor',
                    'AMap.Polyline',
                    'AMap.RangingTool',
                    'AMap.PlaceSearch',
                ],
                () => {
                    this.gMap.addControl(new AMap.Scale());
                    this.gMap.addControl(new AMap.MapType());
                    this.placeSearch = new AMap.PlaceSearch({
                        type: '',
                        extensions: 'base',
                    });
                    //初始化heatmap对象
                    this.heatmap = new AMap.Heatmap(this.gMap, {
                        radius: 30,
                    });
                    this.rangingTool = new AMap.RangingTool(this.gMap);
                    this.rangingTool.on('end', () => {
                        this.rangingTool.turnOff();
                        this.openRangingTool = false;
                    });
                }
            );
            this.marker = new AMap.Marker({
                content: ' ',
                offset: new AMap.Pixel(-5, -15),
                map: this.gMap,
            });
            this.infoWindow = new AMap.InfoWindow({content: ' ', map: this.gMap});
            this.placeSearchMarker = new AMap.Marker({
                position: new AMap.LngLat(0, 0),
                offset: new AMap.Pixel(-5, -15),
                map: this.gMap,
            });
            this.loadMapEvent();
            AMapUI.load(['ui/misc/PathSimplifier'], (PathSimplifier) => {
                if (!PathSimplifier.supportCanvas) {
                    window.return;
                }
                this.initPathSimplifier(this.gMap, PathSimplifier);
            });
        },
        upload() {
            this.$Spin.show();
            let formData = this.$refs.modal.formItem;
            if (this.$refs.modal.fileType === 'text') {
                formData.file = new Blob([this.$refs.modal.fileText], {
                    type: 'text/html;charset=utf-8',
                });
                this.uploadByFile(formData);
            } else if (this.$refs.modal.fileType === 'file') {
                formData.file = this.$refs.modal.file;
                this.uploadByFile(formData);
            }
        },
        /**
         * @name: 处理自定义颜色数据
         * @description: 唐总需求支持单个标注点自定义颜色#20210713（仅支持字符串输入）
         * 若名称中含有指定分隔符，则切割其后的作为颜色值并还原名称
         */
        handleDiffColorData(datas, defaultColor) {
            const result = new Map();
            for (let item of datas) {
                let color = defaultColor;
                if (item.name.includes(this.nameColorDelimiter)) {
                    const tempArr = item.name.split(this.nameColorDelimiter);
                    item.name = tempArr[0];
                    color = tempArr[1];
                }
                if (result.has(color) === false) {
                    result.set(color, []);
                }
                result.get(color).push(item);
            }
            return result;
        },
        uploadByFile(formData) {
            this.uploadData(
                '/coordinate/convert/',
                '/coordinate/convert/area/',
                formData
            );
        },
        uploadData(pointUrl, polygonUrl, formData) {
            if (formData.bigType === 'point') {
                post(pointUrl, formData).then((response) => {
                    this.parseResponse(response);
                });
            } else if (formData.bigType === 'polygon') {
                if (formData.type === 'polyline') {
                    let formData = this.$refs.modal.formItem;
                    formData.closeLoop = false;
                }
                post(polygonUrl, formData).then((response) => {
                    this.parseResponse(response);
                });
            }
        },
        parseResponse(response) {
            let data = response.data.data;
            if (response.data.code === 1) {
                this.parseResponseData(data);
            } else {
                this.$Message.warning(response.data.msg);
            }
            this.closeSpin();
        },
        parseResponseData(data) {
            let type = data.type;
            let bigType = data.bigType;
            let pColor = this.$refs.modal.myColor;
            if (bigType === 'point') {
                this.parsePointData(data, type, pColor);
            } else if (bigType === 'polygon') {
                this.parsePolygonData(data, type, pColor);
            }
            if (data.key && data.key.length > 0) {
                if (this.keyDic[type] && this.keyDic[type].length > 0) {
                    this.$set(this.keyDic, type, this.keyDic[type].concat(data.key));
                } else {
                    this.$set(this.keyDic, type, [data.key]);
                }
            }
        },
        parsePointData(data, type, pColor) {
            let datas = data.list.map((m, index) => {
                return {
                    lnglat: [m['lng'], m['lat']],
                    name: this.$refs.modal.autoIncrementName ? index + 1 + '' : m['name'],
                };
            });
            if (type === 'point') {
                // this.addMarks(datas, pColor);
                datas = this.handleDiffColorData(datas, pColor);
                for (let [key, value] of datas) {
                    this.addMarks(value, key);
                }
            } else if (type === 'heatmap') {
                let dataHeat = datas.map((o) => {
                    return {
                        lng: o['lnglat'][0],
                        lat: o['lnglat'][1],
                        count: o['name'],
                    };
                });
                this.addHeatMap(dataHeat);
            } else if (type === 'track') {
                let dataTrack = datas.map((o) => {
                    return o['lnglat'];
                });
                this.addTrack(dataTrack, pColor);
            }
        },
        parsePolygonData(data, type, pColor) {
            this.addPolygonList(
                data,
                pColor,
                type === 'polyline',
                this.$refs.modal.randomColor
            );
        },
        addMarks(datas, pColor) {
            this.updateCenter(datas.map((data) => [data.lnglat]));
            let mass = new AMap.MassMarks(datas, {
                opacity: 0.8,
                zIndex: 111,
                cursor: 'pointer',
                alwaysRender: false,
                style: {
                    url: randomSvg(pColor),
                    anchor: new AMap.Pixel(3, 3),
                    size: new AMap.Size(10, 10),
                },
            });
            if (this.$refs.modal.pointShowName) {
                let pointLabelsLayer = new AMap.LabelsLayer(
                    this.getLabelsLayerOptions()
                );
                datas.forEach((data) => {
                    const labelMarker = new AMap.LabelMarker(
                        this.getLabelMarkerOptions(data.lnglat, data.name, pColor)
                    );
                    pointLabelsLayer.add(labelMarker);
                });
                this.gMap.add(pointLabelsLayer);
                this.pointLabelsLayers.push(pointLabelsLayer);
            } else {
                mass.on('mouseover', (e) => {
                    this.marker.setPosition(e.data.lnglat);
                    this.marker.setLabel({content: e.data.name});
                });
                mass.on('mouseout', () => {
                    this.marker.setPosition(new AMap.LngLat(0, 0));
                });
            }
            mass.setMap(this.gMap);
            this.massList.push(mass);
        },
        addPolygonList(datas, pColor = 'blue', line = false, randomColor = false) {
            let polygonLabelsLayer = new AMap.LabelsLayer(
                this.getLabelsLayerOptions()
            );
            datas.list.forEach((a) => {
                let dataArray = a.pointList.map((polygon) =>
                    polygon.map((m) => [m['lng'], m['lat'], m['wgsLng'], m['wgsLat']])
                );
                //名称里面含有自定义颜色，则更新名称及颜色
                if (a.name.includes(this.nameColorDelimiter)) {
                    const tempArr = a.name.split(this.nameColorDelimiter);
                    a.name = tempArr[0];
                    pColor = tempArr[1];
                }
                if (randomColor) {
                    pColor = randomHex();
                }
                this.addPolygonItem(
                    polygonLabelsLayer,
                    dataArray,
                    a.name,
                    pColor,
                    line
                );
            });
            this.gMap.add(polygonLabelsLayer);
            this.polygonLabelsLayers.push(polygonLabelsLayer);
        },
        addPolygonItem(
            layer,
            dataArray,
            name = '轮廓',
            pColor = 'blue',
            line = false
        ) {
            let centerPoint = this.updateCenter(dataArray);
            let polygon;
            let pointPath;
            if (line) {
                polygon = new AMap.Polyline();
                pointPath = dataArray.flatMap((data) =>
                    data.map((d) => new AMap.LngLat(d[0], d[1]))
                );
            } else {
                polygon = new AMap.Polygon();
                pointPath = dataArray.map((data) =>
                    data.map((d) => new AMap.LngLat(d[0], d[1]))
                );
            }
            polygon.setOptions({
                path: pointPath,
                borderWeight: 2, // 线条宽度，默认为 1
                fillOpacity: 0,
                strokeColor: pColor, // 线条颜色
            });
            this.gMap.add(polygon);
            polygon.setExtData(
                dataArray.map((data) => data.map((d) => [d[2], d[3]]))
            );
            let index = this.polygonList.length;
            polygon.on('dblclick', (e) => {
                this.editPoly(e, index);
            });
            polygon.on('rightclick', (e) => {
                this.lnglat = e.lnglat;
                this.contextMenu.open(this.gMap, e.lnglat);
            });
            if (this.$refs.modal.pointShowName) {
                let labelMarker = new AMap.LabelMarker(
                    this.getLabelMarkerOptions(centerPoint, name, pColor)
                );
                layer.add(labelMarker);
            }
            this.polygonList.push({
                polygon: polygon,
                name: name,
                id: polygon._amap_id,
                show: true,
            });
        },
        editPoly(e, index) {
            this.closePolyEdit(this.polyEditName != null);
            this.polyEditor = new AMap.PolyEditor(this.gMap, e.target);
            this.polyEditor.open();
            this.polyEditor.on('adjust', (e) => this.$refs.polyDrawer.updatePoint(e));
            this.polyEditor.on('removenode', (e) =>
                this.$refs.polyDrawer.updatePoint(e)
            );
            this.polyEditName = this.polygonList[index].name;
        },
        closePolyEdit(forceClear) {
            if (this.polyEditor != null && forceClear) {
                this.polyEditor.close();
                this.polyEditName = null;
                return true;
            }
            return false;
        },
        addHeatMap(datas) {
            this.updateCenter(datas.map((d) => [[d['lng'], d['lat']]]));
            this.heatmap.setDataSet({
                data: datas,
            });
        },
        addTrack(datas, pColor) {
            this.updateCenter(datas.map((d) => [d]));
            this.simplifierPathData.push({
                color: pColor,
                name: '轨迹' + (this.simplifierPathData.length + 1),
                path: datas,
            });
            this.pathSimplifier.setData(this.simplifierPathData);
            //创建一个巡航器
            this.simplifierPathData.forEach((s, index) =>
                this.pathSimplifier
                    .createPathNavigator(index, {
                        loop: true, //循环播放
                        speed: (getPathDistance(s.path) * 3.6) / 30,
                    })
                    .start()
            );
        },
        setCenter(position) {
            if (position) {
                let lng = position.split('_')[0];
                let lat = position.split('_')[1];
                this.gMap.setCenter(new AMap.LngLat(lng, lat), true);
                return new AMap.LngLat(lng, lat);
            }
            return new AMap.LngLat(0, 0);
        },
        updateCenter(dataArrays) {
            let p = this.getCenter(dataArrays);
            if (p[0] > 0 && p[1] > 0) {
                this.gMap.setCenter(new AMap.LngLat(p[0], p[1]), true);
            }
            return p;
        },
        getCenter(dataArrays) {
            let lng = 0;
            let lat = 0;
            let length = 0;
            dataArrays.forEach((data) => {
                data.forEach((d) => {
                    lng += d[0];
                    lat += d[1];
                    length++;
                });
            });
            if (lng > 0 && lat > 0) {
                lng = lng / length;
                lat = lat / length;
                return [lng, lat];
            }
            return [0, 0];
        },
        clearType(type) {
            if ((type === 'point' || type === 'all') && this.massList.length > 0) {
                this.massList.forEach((m) => m.setMap(null));
                this.massList = [];
                this.pointLabelsLayers.forEach((layer) => this.gMap.remove(layer));
                this.marker.setPosition(new AMap.LngLat(0, 0));
            }
            if (
                (type === 'polygon' || type === 'all') &&
                this.polygonList.length > 0
            ) {
                this.polygonList.forEach((p) => this.gMap.remove(p.polygon));
                this.polygonList = [];
                this.polygonLabelsLayers.forEach((layer) => this.gMap.remove(layer));
                this.closePolyEdit(true);
            }
            if (type === 'track' || type === 'all') {
                this.simplifierPathData = [];
                this.pathSimplifier.setData(this.simplifierPathData);
                this.pathSimplifier.clearPathNavigators();
            }
            if (type === 'heatmap' || type === 'all') {
                this.heatmap.setDataSet({
                    data: [],
                });
            }
            let msg = '';
            switch (type) {
                case 'all':
                    msg = '所有图层';
                    break;
                case 'point':
                    msg = '海量点';
                    break;
                case 'polygon':
                    msg = '轮廓';
                    break;
                case 'heatmap':
                    msg = '热力图';
                    break;
            }
            this.$Message.info({
                content: '清除' + msg + '完成',
                duration: 1,
            });
            if (type === 'all') {
                this.keyDic = {};
            } else {
                delete this.keyDic[type];
            }
        },
        closeSpin() {
            this.$nextTick(() => {
                this.$Spin.hide();
            });
        },
        loadMapEvent() {
            this.loadContextMenu();
            this.gMap.on('rightclick', (e) => {
                this.lnglat = e.lnglat;
                this.contextMenu.open(this.gMap, e.lnglat);
            });
            this.gMap.on('dblclick', (e) => {
                this.lnglat = e.lnglat;
                this.showLngLat(e.lnglat);
            });
        },
        loadContextMenu() {
            this.contextMenu = new AMap.ContextMenu();
            this.contextMenu.addItem(
                '开始打点',
                () => {
                    this.openModal = true;
                    this.contextMenu.close();
                },
                0
            );
            this.contextMenu.addItem(
                '标记位置',
                () => {
                    this.showLngLat(this.lnglat);
                    this.contextMenu.close();
                },
                10
            );
            this.contextMenu.addItem(
                '地图测距',
                () => {
                    this.rangingTool.turnOn();
                    this.openRangingTool = true;
                    this.contextMenu.close();
                },
                15
            );
            this.contextMenu.addItem(
                '清除海量点',
                () => {
                    this.clearType('point');
                    this.contextMenu.close();
                },
                20
            );
            this.contextMenu.addItem(
                '清除轮廓/折线',
                () => {
                    this.clearType('polygon');
                    this.contextMenu.close();
                },
                30
            );
            this.contextMenu.addItem(
                '清除热力图',
                () => {
                    this.clearType('heatmap');
                    this.contextMenu.close();
                },
                40
            );
            this.contextMenu.addItem(
                '清除所有图层',
                () => {
                    this.clearType('all');
                    this.contextMenu.close();
                },
                50
            );
        },
        showLngLat(point) {
            if (
                !this.closePolyEdit(this.polyEditName != null) &&
                this.lnglat &&
                !this.openRangingTool
            ) {
                this.infoWindow.setPosition(point);
                let lnglat = gcj02towgs84(point.lng, point.lat);
                this.infoWindow.setContent(
                    lnglat[0].toFixed(7) + ',' + lnglat[1].toFixed(7)
                );
                this.infoWindow.open(this.gMap);
            }
        },
        submitFeedback() {
            // let formData = this.$refs.feedbackModal.formItem;
            //     let form = this.$refs.feedbackModal.$refs.feedbackForm;
            //     form.validate((valid) => {
            //         if (valid) {
            //             this.$Spin.show();
            //             post('/coordinate/feedback', formData).then((response) => {
            //                 if (response.data.code === 1) {
            //                     form.resetFields();
            //                     this.feedbackModal = false;
            //                     this.$Message.info('提交成功，谢谢您的反馈！');
            //                 } else {
            //                     this.$Message.warning(response.data.msg);
            //                 }
            //                 this.closeSpin();
            //             });
            //         } else {
            //             this.feedbackLoading = false;
            //             this.$nextTick(() => {
            //                 this.feedbackLoading = true;
            //             });
            //         }
            //     });
            this.feedbackModal = false;
            this.$Message.info('提交成功，谢谢您的反馈！');
        },
        getLabelMarkerOptions(point, name, color) {
            return {
                position: point,
                text: {
                    content: name,
                    style: {
                        fontSize: 12,
                        fillColor: color,
                        strokeColor: '#fff',
                        strokeWidth: 2,
                        padding: '2, 5',
                    },
                },
            };
        },
        getLabelsLayerOptions() {
            return {
                zooms: [6, 20],
                zIndex: 100,
            };
        },

        searchPlace(keyWords) {
            this.placeSearch.search(keyWords, (status, result) => {
                this.placeList = [];
                if (status === 'complete') {
                    this.placeList = result.poiList.pois.map((poi) => {
                        return {
                            name: poi.name,
                            location: poi.location,
                        };
                    });
                }
            });
        },
        selectPlace(point) {
            if (!point) {
                return;
            }
            let centerPoint = this.setCenter(point);
            this.placeSearchMarker.setPosition(centerPoint);
        },
        initPathSimplifier(map, PathSimplifier) {
            this.simplifierPathImg = PathSimplifier.Render.Canvas.getImageContent(
                require('../assets/ikun.png'),
                () => {
                },
                () => {
                }
            );
            this.pathSimplifier = new PathSimplifier({
                zIndex: 100,
                map: map, //所属的地图实例
                getPath: (pathData) => {
                    return pathData.path;
                },
                getHoverTitle: (pathData, pathIndex, pointIndex) => {
                    if (pointIndex >= 0) {
                        //鼠标悬停在某个轨迹节点上
                        return (
                            pathData.name + '，点:' + pointIndex + '/' + pathData.path.length
                        );
                    }
                    //鼠标悬停在节点之间的连线上
                    return pathData.name + '，点数量' + pathData.path.length;
                },
                renderOptions: {
                    pathLineStyle: {
                        dirArrowStyle: true,
                    },
                    getPathStyle: (pathItem) => {
                        return {
                            //轨迹点的样式
                            startPointStyle: {
                                radius: 6,
                                fillStyle: 'green',
                                strokeStyle: 'green',
                            },
                            endPointStyle: {
                                radius: 6,
                                fillStyle: 'red',
                                strokeStyle: 'red',
                            },
                            //轨迹线的样式
                            pathLineStyle: {
                                strokeStyle: pathItem.pathData.color,
                                lineWidth: 6,
                                dirArrowStyle: true,
                            },
                            //经过路径的样式
                            pathNavigatorStyle: {
                                width: 24,
                                height: 24,
                                // content: this.simplifierPathImg,
                                pathLinePassedStyle: {
                                    lineWidth: 6,
                                    strokeStyle: revertColor(pathItem.pathData.color),
                                },
                            },
                        };
                    },
                },
            });
        },
    },
};
</script>

<style scoped>
.amap-page-container,
.amap-demo {
    height: 100%;
}

.toolbar {
    position: absolute;
    bottom: 20px;
    right: 10px;
}
</style>
<style>
.my-ivu-drawer-wrap .ivu-drawer-body {
    padding: 0;
}
</style>
