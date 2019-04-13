<template>
    <div ref="tuiImageEditor" style="width: 100%;height: 100%;"></div>
</template>
<script>
import ImageEditor from 'tui-image-editor';
import '../../../static/icon_ps/iconfont.css';
import { aliConfig } from '@/utils/aliyunConfig';
import { base64Server } from '@/api/service';
const OSS = require('ali-oss');
const editorEvents = [
    'addText',
    'mousedown',
    'objectActivated',
    'objectMoved',
    'objectScaled',
    'redoStackChanged',
    'textEditing',
    'undoStackChanged'
];
const includeUIOptions = {
    includeUI: {
        initMenu: 'filter'
    }
};
const editorDefaultOptions = {
    cssMaxWidth: 700,
    cssMaxHeight: 500,
    usageStatistics: false
};
export default {
    name: 'TuiImageEditor',
    props: {
        includeUi: {
            type: Boolean,
            default: false
        },
        options: {
            type: Object,
            default() {
                return editorDefaultOptions;
            }
        },
        imgUrl: {
             type: String
        },
        imgName: {
             type: String
        },
        saveBtn: {
            type: Boolean
        },
        dealPic: {
            type: Boolean
        }
    },
    watch: {
        saveBtn(val){
            if(val){
                this.save();
            }
        },
        imgUrl(val){
            let imgName1 = val.split('com/')[1];
            let imgName2 = imgName1.split('?OSS')[0];
            this.getBase64fromServer(imgName2);
        },
        dealPic(val) {
        }

    },
    data() {
        return {
            editorInstance: null
        };
    },
    mounted() {
        let imgName1 = this.imgUrl.split('com/')[1];
        let imgName2 = imgName1.split('?OSS')[0];
        this.getBase64fromServer(imgName2);
    },
    destroyed() {
        editorEvents.forEach(eventName => this.editorInstance.off(eventName));
        this.editorInstance.destroy();
    },
    methods: {
        /**
             得到base64的图片
         */
        async getBase64fromServer(imgName){
            let res = await base64Server.getpicture(imgName);
            if(res.code === 0){
                this.initTui(res.obj);
            }
            return false;
        },
        /**
         * 初始化插件
         */
        initTui(base64Img){
            let options = editorDefaultOptions;
            if (this.includeUi) {
                options = Object.assign(includeUIOptions, this.options);
            }
            this.editorInstance = new ImageEditor(this.$refs.tuiImageEditor, options);
            this.addEventListener();
            this.openFileFromUrl(this.editorInstance, this.imgUrl, this.imgName, base64Img);
        },
        /**
         * 打开网络地址图片
         */
        openFileFromUrl(editorInstance, imgUrl, imgName, base64Img){
            editorInstance.loadImageFromURL(`data:image/jpeg;base64,${base64Img}`, imgName)
                .then(resp=>{
                    editorInstance.ui.activeMenuEvent();
                    editorInstance.ui.resizeEditor({ imageSize: resp });
                })
                .catch(err=>{
                    console.log('err:', err);
                })
        },
        /**
         * 保存图片
         */
        save(){
            let imgUrl = this.editorInstance.toDataURL();
            this.putToOSS(imgUrl, this.imgName);
        },
        /**
         * 上传图片
         */
        putToOSS(imgUrl , imgName){
            let file = this.dataURLtoFile(imgUrl, imgName);
            const client = new OSS(aliConfig);
            let storeAs = `${new Date().toJSON().split('T')[0]}/${file.name}`
            let results = client.put(storeAs, file).then((results) => {
                let signUrl = client.signatureUrl(`${results.name}`, { expires: 80000 });
                this.$message.success('成功');
                this.$emit('successSubmit', signUrl);
            });
        },
        /*
        * 将base64转换为文件
        */
        dataURLtoFile(dataurl, filename) {//将base64转换为文件
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while(n--){
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new File([u8arr], filename, {type:mime});
        },
        addEventListener() {
            editorEvents.forEach(eventName => {
                this.editorInstance.on(eventName, (...args) => this.$emit(eventName, ...args));
            });
        },
        getRootElement() {
            return this.$refs.tuiImageEditor;
        },
        invoke(methodName, ...args) {
            let result = null;
            if (this.editorInstance[methodName]) {
                result = this.editorInstance[methodName](...args);
            } else if (methodName.indexOf('.') > -1) {
                const func = this.getMethod(this.editorInstance, methodName);

                if (typeof func === 'function') {
                    result = func(...args);
                }
            }

            return result;
        },
        getMethod(instance, methodName) {
            const {first, rest} = this.parseDotMethodName(methodName);
            const isInstance = (instance.constructor.name !== 'Object');
            const type = typeof instance[first];
            let obj;

            if (isInstance && type === 'function') {
                obj = instance[first].bind(instance);
            } else {
                obj = instance[first];
            }

            if (rest.length > 0) {
                return this.getMethod(obj, rest);
            }

            return obj;
        },
        parseDotMethodName(methodName) {
            const firstDotIdx = methodName.indexOf('.');
            let firstMethodName = methodName;
            let restMethodName = '';

            if (firstDotIdx > -1) {
                firstMethodName = methodName.substring(0, firstDotIdx);
                restMethodName = methodName.substring(firstDotIdx + 1, methodName.length);
            }

            return {
                first: firstMethodName,
                rest: restMethodName
            };
        }
    }
};
</script>
<style>
</style>

