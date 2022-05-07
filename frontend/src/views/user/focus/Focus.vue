<template>
  <div style="background:#ECECEC; padding:30px;margin-top: 30px;width: 100%">
    <a-row :gutter="55">
      <a-col v-if="collectList.length === 0" :span="24">
        <div style="color: rgba(0,0,0,.85);font-size: 24px;line-height: 1.8;text-align: center;height: 250px;">
          <p style="margin-top: 100px">暂无关注记录</p>
        </div>
      </a-col>
      <a-col :span="8" v-for="(item, index) in collectList" :key="index">
        <a-card hoverable :bordered="false">
          <a-card-meta :title="item.collectUsername" :description="item.createDate">
            <a-avatar
              slot="avatar"
              :src="'static/avatar/' + item.collectAvatar"
            />
          </a-card-meta>
          <template slot="actions" class="ant-card-actions">
            <a-icon key="delete" type="delete" @click="collectPostCheck(1, item.collectUserId)"/>
          </template>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'Focus',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      loading: false,
      collectList: []
    }
  },
  mounted () {
    this.getCollectList()
  },
  methods: {
    collectPostCheck (deleteFlag, collectUserId) {
      this.$post(`/cos/focus-info`, {userId: this.currentUser.userId, collectUserId, deleteFlag}).then((r) => {
        this.getCollectList()
        this.$message.success(deleteFlag === 0 ? '关注成功！' : '取消关注成功！')
      })
    },
    getCollectList () {
      this.$get(`/cos/focus-info/list/${this.currentUser.userId}`).then((r) => {
        this.collectList = r.data.data
      })
    }
  }
}
</script>

<style scoped>

</style>
