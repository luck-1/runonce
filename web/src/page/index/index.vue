<template>
  <div class="home-index">
    <div class="flex-content">
      <el-col :span="14">
        <el-card class="content-card" :body-style="{ padding: '0 10px',height: 'calc(100% - 45px)' }">
          <div slot="header" class="flex-header">
            <span>待办事项</span>
          </div>
          <div v-if="todoTasks.length===0" style="margin-top: 10px;text-align:center">暂无数据</div>

          <div v-else style="height: calc(100% - 42px);overflow: auto;">
            <div class="list-row" v-for="(item,index) in todoTasks" :key="index" @click="clickTodo(item)">
              <p><span>{{index+1}}、</span>{{item.content}}</p>
            </div>
          </div>

          <el-pagination
            class="todo-page"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card class="content-card" :body-style="{ padding: '0',height: '100%' }">
          <div class="top-right">
            <div>
              <p>总办理项</p>
              <div style="background-color: #ff6b5d;">{{eventStatistics.allDeal}}个</div>
            </div>
            <div>
              <p>更新中办理项</p>
              <div style="background-color: #f4b205;">{{eventStatistics.stayDeal}}个</div>
            </div>
            <div>
              <p>已完成办理项</p>
              <div style="background-color: #37ccc2;">{{eventStatistics.accomplishDeal}}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </div>
    <div class="flex-content">
      <el-col :span="14">
        <el-card class="content-card" :body-style="{ padding: '0 10px',height: 'calc(100% - 45px)' }">
          <div slot="header" class="flex-header">
            <span>市、各区县开发区的事项统计</span>
          </div>
          <myChart ref="barChart" style="width:100%;height:100%;" :id="eventbar" :option="option1"></myChart>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card class="content-card" :body-style="{ padding: '0 10px',height: 'calc(100% - 45px)' }">
          <div slot="header" class="flex-header">
            <span>事项级别统计</span>
          </div>
          <myChart ref="pieChart" style="width:110%;height:100%;" :id="eventpie" :option="option2"></myChart>
        </el-card>
      </el-col>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex'
import { todoTask } from '@/api/service'
import myChart from "@/components/myChart";
import echarts from 'echarts';
export default {
  data () {
    return {
      // 待办事项
      currentPage: 1,
      pageSize: 10,
      total: 0,
    	todoTasks:[],
      eventbar:"eventbar",
      option1:{
        grid: {
          left: '2%',
          right: '1%',
          bottom: '3%',
          top:'12%',
          containLabel: true
        },
        legend: {
          data: ['总计事项', '已完成事项'],
          align: 'left'
        },
        tooltip:{},
        xAxis: {},
        yAxis: {
          type: 'value',
          axisLine:{
            symbol:['none','arrow'],
            symbolSize:[6,6],
          },
          axisLabel:{
            showMaxLabel:false,
          },
          splitLine:{
            show:false,
          },
        },
        series: []
      },
      eventpie:"eventpie",
      option2:{
        tooltip:{},
        legend:{},
        series:[{
          type: 'pie',
          radius : ['40%', '78%'],
          center: ['30%', '50%'],
          label: {
            normal: {
              show: false,
            },
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      },
      eventStatistics:{},
      eventSum:{
        provincialLevel:0,
        cityLevel:0,
        countyLevel:0,
        countyPrivateLevel:0,
      },
    }
  },
  computed: {
    ...mapState([
      'todoTask'
    ])
  },
  components:{
    myChart
  },
  mounted() {
    this.searchToDoTask();
    this.getEventStatistics();
  },
  created(){
    this.findEventLevel();
    this.findEveryAreaCount();
  },
  methods: {
    ...mapMutations([
      'SET_TODOTASK'
    ]),
    // 待办事项分页查询
    handleSizeChange (val) {
      this.pageSize = val
      this.searchToDoTask()
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.searchToDoTask()
    },
    async searchToDoTask () {
      let res = await todoTask.selectTodo({
        currentPage: this.currentPage,
        pageSize: this.pageSize
      })
      if(res.code === 0) {
        this.todoTasks = res.obj.list
        this.total = res.obj.total
      }
    },
    // 点击待办事项
    async clickTodo (row) {
      let res = await todoTask.clickTodo(row.id)
      if(res.code === 0) {
        if(res.obj) {
          this.SET_TODOTASK(res.obj)
          switch (row.type) {
            case 1:
              this.$router.push('/itemManagement')
              break;
            case 2:
              this.$router.push('/itemManagement')
              break;
            case 3:
              this.$router.push('/publicity')
              break;
            default:
          }
        }
      }
    },
    // 办理项统计
    async getEventStatistics (){
      let res = await todoTask.getEventStatistics();
      if(res.code == 0){
        this.eventStatistics = res.obj;
      }else{
        this.$message.error(res.msg);
      }
    },
    // 事项级别统计
    async findEventLevel (){
      let res = await todoTask.findEventLevel();
      if(res.code == 0){
        this.eventSum = res.obj;
        this.option2.tooltip = {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)"
        };
        this.option2.legend = {
          orient: 'vertical',
          right: 60,
          top: 20,
          bottom: 20,
          data: ["省级", "市级", "县（区）级", "县（区）级个性化事项"],
        };
        this.option2.series = [{
          type: 'pie',
          radius : ['40%', '78%'],
          center: ['30%', '50%'],
          label: {
            normal: {
              show: false,
            },
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          data: [{name: "省级", value:this.eventSum.provincialLevel},{name: "市级", value:this.eventSum.cityLevel},{name: "县（区）级", value: this.eventSum.countyLevel},{name: "县（区）级个性化事项", value: this.eventSum.countyPrivateLevel}],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }];
        this.$refs.pieChart.setChart();
      } else{
        this.$message.error(res.msg);
      }
    },
     // 各区县开发区的事项统计
    async findEveryAreaCount (){
      let res = await todoTask.findEveryAreaCount();
      if(res.code == 0){
        let  cityEventCount = res.obj;
        let cityName = [];
        let allEvent = [];
        let finishEvent = [];
        for(let i = 0;i < cityEventCount.length;i++){
          if((i+1)%2 === 0){
            cityName.push("\n"+cityEventCount[i].cityName);
          }else{
            cityName.push(cityEventCount[i].cityName);
          }
          allEvent.push(cityEventCount[i].allEventCount);
          finishEvent.push(cityEventCount[i].finishEventCount);
        }
        this.option1.tooltip = {
          trigger: 'item',
          formatter: "{b} : {c}"
        };
        this.option1.xAxis = {
          type: 'category',
          data: cityName,
          axisLine:{
            symbol:['none','arrow'],
            symbolSize:[6,6],
          },
          axisLabel: {
            interval:0,
            rotate:60
          },
        };
        this.option1.series = [{
          type: 'bar',
          name: '总计事项',
          data:  allEvent,
        }, {
          type: 'bar',
          name: '已完成事项',
          data:  finishEvent,
        }];
        this.$refs.barChart.setChart();
      } else{
        this.$message.error(res.msg);
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.home-index {
  padding: 0 10px;
  height: calc(100vh - 100px);
  .flex-content {
    display: flex;
    width: 100%;
    height: calc(50% - 8px);
    &:nth-child(1) {
      margin-bottom:16px;
    }
    &>div:not(:last-child){
      margin-right: 1rem;
    }
    & /deep/ .el-card__header {
      padding: 10px 20px;
    }
    .flex-header {
      font-size: 1rem;
      font-weight: bold;
      color:#303133;
    }
  }
}
.content-card {
  position:relative;
  height:100%;
}
.list-row {
  display: flex;
  width: 100%;
  flex-direction: row;
  padding: 0px 10px;
  margin: 12px 0;
  line-height: 36px;
  border-radius: 5px;
  color: #303133;
  background: #eee;
  cursor: pointer;
  &>p {
    flex: 1;
    height: 36px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    span{
      margin-right: 10px;
    }
  }
  &:hover {
    color: #fff;
    background: rgba(45, 140, 240, 0.8);
  }
}
.todo-page {
  position: absolute;
  bottom: 0;
  left: 0;
  margin: 10px 0;
  width: 100%;
  text-align: center;
}
.top-right {
  display: flex;
  width:100%;
  height:100%;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  &>div {
    flex: 1;
    margin: 10px;
    height:100%;
    text-align: center;
    p{
      margin: 1.5rem 0;
      font-size: 1rem;
    }
    div {
      display: flex;
      align-items: center;
      justify-content: center;
      width:100%;
      height:60%;
      vertical-align: bottom;
      border-radius: 10px;
      font-size: 2rem;
      color: #fff;
    }
  }
}
.event-bar {
  display:flex;
  width:100%;
  height:90%;
}
</style>
