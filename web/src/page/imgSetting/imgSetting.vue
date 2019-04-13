<template>
    <div class="imgSettingWrap">
      <el-button-group class="btn-group">
        <el-button type="danger" @click="save" style="border-radius:3px">保存</el-button>
        <el-button type="primary" @click="$emit('close')" style="margin-left:5px;border-radius:3px">关闭</el-button>
      </el-button-group>
      <div class="imageEditorApp">
        <tui-image-editor ref="tuiImageEditor"
          :include-ui="useDefaultUI"
          :options="options"
          :imgUrl="imgUrl"
          :imgName="imgName"
          :saveBtn="saveBtn"
          :dealPic="dealPic"
          @successSubmit="successSubmit"
          @addText="onAddText"
          @objectMoved="onObjectMoved">
        </tui-image-editor>
      </div>
    </div>
</template>
<script>
// To use the default UI, the svg files for the icons is required.
// import 'tui-image-editor/dist/svg/icon-a.svg';
// import 'tui-image-editor/dist/svg/icon-b.svg';
// import 'tui-image-editor/dist/svg/icon-c.svg';
// import 'tui-image-editor/dist/svg/icon-d.svg';

// Load Style Code
import 'tui-image-editor/dist/tui-image-editor.css';
import 'tui-color-picker/dist/tui-color-picker.css';

import ImageEditor from './ImageEditor';

export default {
    props:{
        imgName:{
            type: String
        },
        imgUrl:{
            type: String
        },
        dealPic: {
            type: Boolean
        }
    },
    components: {
        'tui-image-editor': ImageEditor
    },
    data() {
        return {
            useDefaultUI: true,
            saveBtn: false,
            options: {
                includeUI: {
                    initMenu: 'filter'
                },
                cssMaxWidth: 700,
                cssMaxHeight: 500
            }
        };
    },
    methods: {
        save(){
            this.saveBtn = true;
        },
        onAddText(res) {
            // console.group('addText');
            // console.log('Client Position : ', res.clientPosition);
            // console.log('Origin Position : ', res.originPosition);
            // console.groupEnd();
        },
        onObjectMoved(res) {
            // console.group('objectMoved');
            // console.log('Left : ', res.left);
            // console.log('Top : ', res.top);
            // console.groupEnd();
        },
        successSubmit(imgUrl){
            this.saveBtn = false;
            this.$emit('successSubmit', imgUrl);
        }
    }
};
</script>
<style scoped>
.imageEditorApp {
  width: 100%;
  height: 872px;
}
.imgSettingWrap{
  position: relative;
}
.btn-group{
  position: absolute;
  top: 18px;
  right: 3%;
  z-index: 10;
}
</style>
