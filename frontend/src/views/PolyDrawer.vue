<template>
    <div style="height: 100%;overflow: hidden">
        <div style="height: 340px;padding: 4px 12px 0 12px">
            <Divider orientation="left" size="small">点序列</Divider>
            <Input :value="pointStr" readonly type="textarea" placeholder="点序列" :rows="3"/>
            <Divider orientation="left" size="small">16进制</Divider>
            <Input :value="pointHex" readonly type="textarea" placeholder="16进制" :rows="3"/>
            <Divider orientation="left" size="small">WKT</Divider>
            <Input :value="pointWKT" readonly type="textarea" placeholder="wkt标准格式" :rows="3"/>
        </div>
        <div style="height: calc(100% - 340px);overflow: auto;">
            <List border size="small" v-for="(polygonList, j) in list" :key="j" :header="'轮廓' + (j + 1)">
                  <ListItem v-for="(item, i) in polygonList" :key="j + '_' + i">
                    <span style="margin-right: 48px;user-select:none;">{{i+1}}</span>
                    <span style="margin-right: 48px;">{{item[0].toFixed(7)}}</span>
                    <span>{{item[1].toFixed(7)}}</span>
                  </ListItem>
            </List>
        </div>
    </div>
</template>

<script>
    import {gcj02towgs84} from "../common/CoordConvert";
    import {int2HexRevers} from "../common/utils";

    export default {
        name: "poly-drawer",
        components: {},
        props: {
            polyEditor: {
                type: Object
            },
            name: {
                type: String
            },
        },
        watch: {
            polyEditor() {
                this.updatePoint()
            }
        },
        computed: {
            pointStr: function () {
                let result = this.list.map(p => p.map( a => a[0].toFixed(7) + ',' + a[1].toFixed(7)).join(";"))
                return this.name + '|' + result
            },
            pointHex: function () {
                return this.name + '|0x' + this.list.map(p => p.map(a => {
                    let hexLng = int2HexRevers(a[0].toFixed(7) * 10000000)
                    let hexLat = int2HexRevers(a[1].toFixed(7) * 10000000)
                    return hexLng + hexLat
                }).join('')).join('')
            },
            pointWKT: function () {
                let wktStr = this.list.map(p => p.map(a => a[0].toFixed(7) + ' ' + a[1].toFixed(7)).join(",")).join(")), ((")
                if(this.list.length > 1){
                  return this.name + '|MULTIPOLYGON (((' + wktStr + ')))'
                }else{
                  return this.name + '|POLYGON ((' + wktStr + '))'
                }
            }
        },
        data: () => {
            return {
                list: [],
            }
        },
        methods: {
            updatePoint: function () {
                if (this.polyEditor) {
                  this.tryLoadPoints(this.polyEditor.au) || this.tryLoadPoints(this.polyEditor.bu)
                }
            },
            tryLoadPoints(dataArray){
              if(dataArray && dataArray.length > 0){
                this.list = []
                dataArray.map(data =>
                    this.list.push(
                        data.map(o => gcj02towgs84(o.lng, o.lat))
                    )
                )
                return true
              }
              return false
            }
        }
    }
</script>

<style scoped>

</style>

<style>

</style>

