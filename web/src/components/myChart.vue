<template>
  <div :id="id" :option="option"></div>
</template>

<script>
import echarts from 'echarts';

export default {
  props:{
    id:String,
    option:Object
  },
  data() {
    return {
      
    }
  },
  methods: {
    setChart () {
      let option = this.option;
      let myChart = echarts.init(document.getElementById(this.id));

      myChart.clear();
      // myChart.resize(
      //   {
      //     width:document.getElementById(this.id).offsetWidth*0.8,
      //     height:document.getElementById(this.id).offsetHidth* 0.8
      //   }
      // ),
      // window.onresize = function() {
      //   myChart.resize();
      // };
      myChart.setOption(option);
      window.addEventListener("resize", function () {
          myChart.resize();
      });
    }
  },
  mounted() {
    this.setChart ()
  },
}
</script>
