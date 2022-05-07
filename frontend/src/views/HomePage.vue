<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']" style="background-color: #f0f2f5; border: none">
    <a-row v-if="newsList.length > 0" style="width: 65%;margin: 0 auto;margin-bottom: 15px">
      <a-col :span="22">
        <a-alert
          banner
          :message="newsContent"
          type="info"
        />
      </a-col>
      <a-col :span="2">
        <a-button type="primary" style="margin-top: 2px;margin-left: 10px" @click="newsNext">下一页</a-button>
      </a-col>
    </a-row>
    <a-row :gutter="8" class="head-info">
      <a-card class="head-info-card" style="width: 65%;margin: 0 auto">
        <a-col :span="12">
          <div class="head-info-avatar">
            <img alt="头像" :src="avatar">
          </div>
          <div class="head-info-count">
            <div class="head-info-welcome">
              {{welcomeMessage}}
            </div>
            <div class="head-info-desc">
              <p>{{user.roleName ? user.roleName : '暂无角色'}}</p>
            </div>
            <div class="head-info-time">上次登录时间：{{user.lastLoginTime ? user.lastLoginTime : '第一次访问系统'}}</div>
          </div>
        </a-col>
        <a-col :span="12">
          <div>
            <a-row class="more-info">
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4">
                <a-button type="primary" @click="add">
                  发帖子
                </a-button>
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-card>
    </a-row>
    <a-row :gutter="8" class="count-info">
      <a-card class="head-info-card" style="width: 65%;margin: 0 auto">
        <a-row>
          <a-col :span="24">
            <a-input-search placeholder="搜索贴子" v-show="!postDetailShow" style="width: 200px;margin-top: 10px;float: right" @search="onSearch" />
          </a-col>
        </a-row>
        <a-tabs :activeKey="tabKey" tab-position="top" @change="tabChange" v-show="!postDetailShow">
          <a-tab-pane v-for="item in tagList" :key="item.id" :tab="item.name">
            <a-skeleton active v-if="loading" />
            <div v-if="!loading" style="padding: 25px 80px">
              <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="postList">
                <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                  <template slot="actions">
                    <span key="message">
                      <a-icon type="message" style="margin-right: 8px" />
                      <span v-if="item.collect === 0">{{ item.reply }}</span>
                      <span v-else>{{ item.reply / item.collect }}</span> 回复
                    </span>
                    <span key="star">
                      <a-icon type="star" style="margin-right: 8px" />
                      {{ item.collect }} 收藏
                    </span>
                    <span key="to-top">
                      <a-icon type="to-top" style="margin-right: 8px" />
                      {{ timeFormat(item.createDate) }}
                    </span>
                  </template>
                  <a-list-item-meta :description="item.content.slice(0, 100) + '...'">
                    <a slot="title" @click="postReplyDetail(item)">{{ item.title }}</a>
                    <a-avatar shape="square" slot="avatar" :src="'static/avatar/' + item.avatar" />
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </div>
          </a-tab-pane>
        </a-tabs>
        <div v-if="postDetailShow && postDetail !== null" style="margin: 18px">
          <div style="margin-bottom: 10px">
            <a-breadcrumb>
              <a-breadcrumb-item><a @click="postDetailShow = false">返回</a></a-breadcrumb-item>
              <a-breadcrumb-item>{{ tabName }}</a-breadcrumb-item>
            </a-breadcrumb>
          </div>
          <p style="font-size: 22px;color: black;font-weight: 500;line-height: 150%;margin: 25px 50px;margin-top: 50px">
            {{ postDetail.title }}
            <a-icon type="form" style="cursor: pointer" v-if="user.userId === postDetail.userId" @click="edit(postDetail)"/>
          </p>
          <div style="margin: 25px 50px;font-size: 13px">
            <a-icon v-if="collectUser === 0" type="heart" style="margin-right: 10px;cursor: pointer" @click="collectUserCheck(0)"/>
            <a-icon v-if="collectUser > 0" type="heart" style="margin-right: 10px;color: red;cursor: pointer" @click="collectUserCheck(1)"/>
            {{ postDetail.username }} 关注
            <a-divider type="vertical" />
            <a-icon type="eye" style="margin-right: 10px;margin-left: 40px" />
            {{ postDetail.pageviews }} 访问
            <a-divider type="vertical" />
            <a-icon type="message" style="margin-right: 10px" />
            <span v-if="postDetail.collect === 0">{{ postDetail.reply }}</span>
            <span v-else>{{ postDetail.reply / postDetail.collect }}</span> 回复
            <a-divider type="vertical" />
            <a-icon v-if="collectPost === 0" type="star" style="margin-right: 10px;cursor: pointer" @click="collectPostCheck(0)"/>
            <a-icon v-if="collectPost > 0" type="star" style="margin-right: 10px;color: red;cursor: pointer" @click="collectPostCheck(1)"/>
            {{ postDetail.collect }} 收藏
            <a-divider type="vertical" />
            {{ timeFormat(postDetail.createDate) }}
          </div>
          <div style="margin: 25px 50px;font-size: 15px;line-height: 1.6;word-break: break-word;letter-spacing: 1px;text-indent: 30px">
            {{ postDetail.content }}
          </div>
          <div style="margin: 25px 50px;height: 100px">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
            >
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </div>
          <div style="margin: 25px 50px;">
            <a-list
              class="comment-list"
              :pagination="pagination"
              :header="`${replyList.length} 回复`"
              item-layout="horizontal"
              :data-source="replyList"
            >
              <a-list-item slot="renderItem" slot-scope="item, index">
                <a-comment :author="item.username" shape="square" :avatar="'static/avatar/' + item.avatar">
                  <template slot="actions">
                    <span @click="replyUserAdd(item)">回复</span>
                  </template>
                  <p slot="content" style="white-space: pre-line;">
                    {{ item.content }}
                  </p>
                  <a-tooltip slot="datetime" :title="item.sendCreate">
                    <span>{{ timeFormat(item.sendCreate) }}</span>
                  </a-tooltip>
                </a-comment>
              </a-list-item>
            </a-list>
            <div style="margin-bottom: 200px;margin-top: 50px">
              <a-textarea
                v-model="replyContent"
                placeholder="Controlled autosize"
                :rows="5"
              />
              <a-button type="primary" style="float: right;margin-top: 15px" @click="commit">
                提交
              </a-button>
            </div>
          </div>
        </div>
      </a-card>
    </a-row>
    <post-add
      v-if="postAdd.visiable"
      @close="handlepostAddClose"
      @success="handlepostAddSuccess"
      :postAddVisiable="postAdd.visiable"
      :tagList="tagListData">
    </post-add>
    <post-edit
      ref="postEdit"
      @close="handlepostEditClose"
      @success="handlepostEditSuccess"
      :postEditVisiable="postEdit.visiable"
      :tagList="tagListData">
    </post-edit>
  </div>
</template>
<script>
import HeadInfo from '@/views/common/HeadInfo'
import PostAdd from './admin/post/PostAdd'
import PostEdit from './admin/post/PostEdit'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'HomePage',
  components: {HeadInfo, PostAdd, PostEdit},
  data () {
    return {
      newsPage: 0,
      newsContent: '',
      newsList: [],
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      tagList: [],
      tagListData: [],
      postList: [],
      replyList: [],
      postDetail: null,
      tabName: '',
      tabKey: '',
      postDetailShow: false,
      pagination: {
        pageSize: 20
      },
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      replyContent: '',
      replyUser: null,
      collectPost: 0,
      collectUser: 0,
      postAdd: {
        visiable: false
      },
      postEdit: {
        visiable: false
      }
    }
  },
  watch: {
    replyContent: function (value) {
      if (value === '') {
        this.replyUser = null
      }
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    recommendList () {
      this.loading = true
      this.$get(`/cos/post-info/recommend/${this.user.userId}`).then((r) => {
        this.postList = r.data.data
        setTimeout(() => {
          this.loading = false
        }, 500)
      })
    },
    collectUserCheck (deleteFlag) {
      this.$post(`/cos/focus-info`, {userId: this.user.userId, collectUserId: this.postDetail.userId, deleteFlag}).then((r) => {
        this.postReplyDetail(this.postDetail)
        this.$message.success(deleteFlag === 0 ? '关注成功！' : '取消关注成功！')
      })
    },
    collectPostCheck (deleteFlag) {
      this.$post(`/cos/collect-info`, {userId: this.user.userId, postId: this.postDetail.id, deleteFlag}).then((r) => {
        this.postReplyDetail(this.postDetail)
        this.$message.success(deleteFlag === 0 ? '收藏贴子成功！' : '取消收藏成功！')
      })
    },
    collectByUser (postId) {
      this.$get(`/cos/post-info/collcet`, {userId: this.user.userId, postId}).then((r) => {
        this.collectPost = r.data.collect
        this.collectUser = r.data.focus
      })
    },
    commit () {
      if (this.replyContent !== '') {
        let data = {userId: this.user.userId, content: this.replyContent, postId: this.postDetail.id, replyUserId: this.replyUser}
        this.$post(`/cos/reply-info`, data).then((r) => {
          if (r.data.code === 500) {
            this.$message.error(r.data.msg)
          } else {
            this.postReplyDetail(this.postDetail)
            this.replyContent = ''
          }
        })
      } else {
        this.$message.error('请填写评论！')
      }
    },
    replyUserAdd (reply) {
      this.replyUser = reply.userId
      this.replyContent = this.replyContent + '@' + reply.username
    },
    postReplyDetail (post) {
      this.postInfoDetail(post.id)
      this.collectByUser(post.id)
      this.replyUser = []
      this.fileList = []
      this.$get(`/cos/reply-info/list/${post.id}`).then((r) => {
        this.replyList = r.data.data
        this.postDetailShow = true
      })
    },
    postInfoDetail (postId) {
      this.$get(`/cos/post-info/${postId}`).then((r) => {
        this.postDetail = r.data
        this.imagesInit(this.postDetail.images)
      })
    },
    tabChange (key) {
      this.tabName = this.tagList.find(o => o.id === key).name
      this.tabKey = key
      if (key !== 9999 && key !== -1) {
        this.getPostList(key)
        if (this.tagList[this.tagList.length - 1].id === 9999) {
          this.tagList.pop()
        }
      }
      if (key === -1) {
        this.recommendList()
      }
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    handleCancel () {
      this.previewVisible = false
    },
    getPostList (tagId) {
      this.loading = true
      this.$get(`/cos/post-info/tag/${tagId}`).then((r) => {
        this.postList = r.data.data
        setTimeout(() => {
          this.loading = false
        }, 500)
      })
    },
    getTagList () {
      this.$get('/cos/tag-info/list').then((r) => {
        this.tagList = [{id: -1, name: '推荐'}]
        this.tagList.push.apply(this.tagList, r.data.data)
        console.log(this.tagList)
        if (this.tagList.length !== 0) {
          this.tabChange(this.tagList[0].id)
        }
        let tagListData = []
        r.data.data.forEach(item => {
          tagListData.push({label: item.name, value: item.id})
        })
        this.tagListData = tagListData
      })
    },
    welcome () {
      const date = new Date()
      const hour = date.getHours()
      let time = hour < 6 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour <= 18 ? '下午好' : '晚上好')))
      return `${time}，${this.user.username}`
    },
    timeFormat (time) {
      var nowTime = new Date()
      var day = nowTime.getDate()
      var hours = parseInt(nowTime.getHours())
      var minutes = nowTime.getMinutes()
      // 开始分解付入的时间
      var timeday = time.substring(8, 10)
      var timehours = parseInt(time.substring(11, 13))
      var timeminutes = time.substring(14, 16)
      // eslint-disable-next-line camelcase
      var d_day = Math.abs(day - timeday)
      // eslint-disable-next-line camelcase
      var d_hours = hours - timehours
      // eslint-disable-next-line camelcase
      var d_minutes = Math.abs(minutes - timeminutes)
      // eslint-disable-next-line camelcase
      if (d_day <= 1) {
        // eslint-disable-next-line camelcase
        switch (d_day) {
          case 0:
            // eslint-disable-next-line camelcase
            if (d_hours === 0 && d_minutes > 0) {
              // eslint-disable-next-line camelcase
              return d_minutes + '分钟前'
              // eslint-disable-next-line camelcase
            } else if (d_hours === 0 && d_minutes === 0) {
              return '1分钟前'
            } else {
              // eslint-disable-next-line camelcase
              return Math.abs(d_hours) + '小时前'
            }
            // eslint-disable-next-line no-unreachable
            break
          case 1:
            // eslint-disable-next-line camelcase
            if (d_hours < 0) {
              // eslint-disable-next-line camelcase
              return (24 + d_hours) + '小时前'
            } else {
              // eslint-disable-next-line camelcase
              return d_day + '天前'
            }
            // eslint-disable-next-line no-unreachable
            break
        }
        // eslint-disable-next-line camelcase
      } else if (d_day > 1 && d_day < 10) {
        // eslint-disable-next-line camelcase
        return d_day + '天前'
      } else {
        return time
      }
    },
    add () {
      this.postAdd.visiable = true
    },
    handlepostAddClose () {
      this.postAdd.visiable = false
    },
    handlepostAddSuccess () {
      this.postAdd.visiable = false
      this.$message.success('新增贴子成功')
      this.getPostList(this.tabKey)
    },
    edit (record) {
      this.$refs.postEdit.setFormValues(record)
      this.postEdit.visiable = true
    },
    handlepostEditClose () {
      this.postEdit.visiable = false
    },
    handlepostEditSuccess () {
      this.postEdit.visiable = false
      this.$message.success('修改贴子成功')
      this.postReplyDetail(this.postDetail)
    },
    getNewList () {
      this.$get(`/cos/bulletin-info/list`).then((r) => {
        this.newsList = r.data.data
        if (this.newsList.length !== 0) {
          this.newsContent = `《${this.newsList[0].title}》 ${this.newsList[0].content}`
        }
      })
    },
    newsNext () {
      if (this.newsPage + 1 === this.newsList.length) {
        this.newsPage = 0
      } else {
        this.newsPage += 1
      }
      this.newsContent = `《${this.newsList[this.newsPage].title}》 ${this.newsList[this.newsPage].content}`
    },
    onSearch (key) {
      if (key !== '') {
        this.loading = true
        if (this.tagList[this.tagList.length - 1].id !== 9999) {
          this.tagList.push({id: 9999, name: '搜索'})
        }
        this.tabKey = 9999
        this.tabName = '搜索'
        this.$get(`/cos/post-info/list/${key}`).then((r) => {
          this.postList = r.data.data
          setTimeout(() => {
            this.loading = false
          }, 500)
        })
      }
    }
  },
  mounted () {
    this.getTagList()
    this.getNewList()
    this.welcomeMessage = this.welcome()
    this.$get(`index/${this.user.username}`).then((r) => {
      let data = r.data.data
      this.todayIp = data.todayIp
      this.todayVisitCount = data.todayVisitCount
      this.totalVisitCount = data.totalVisitCount
      let sevenVisitCount = []
      let dateArr = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenVisitCount) {
          if (o.days === time) {
            contain = true
            sevenVisitCount.push(o.count)
          }
        }
        if (!contain) {
          sevenVisitCount.push(0)
        }
        dateArr.push(time)
      }
      let sevenUserVistCount = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenUserVisitCount) {
          if (o.days === time) {
            contain = true
            sevenUserVistCount.push(o.count)
          }
        }
        if (!contain) {
          sevenUserVistCount.push(0)
        }
      }
    }).catch((r) => {
      console.error(r)
      this.$message.error('获取首页信息失败')
    })
  }
}
</script>
<style lang="less">
  .home-page {
    .head-info {
      margin-bottom: .5rem;
      .head-info-card {
        padding: .5rem;
        border-color: #f1f1f1;
        .head-info-avatar {
          display: inline-block;
          float: left;
          margin-right: 1rem;
          img {
            width: 5rem;
            border-radius: 2px;
          }
        }
        .head-info-count {
          display: inline-block;
          float: left;
          .head-info-welcome {
            font-size: 1.05rem;
            margin-bottom: .1rem;
          }
          .head-info-desc {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
            p {
              margin-bottom: 0;
            }
          }
          .head-info-time {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
          }
        }
      }
    }
    .count-info {
      .visit-count-wrapper {
        padding-left: 0 !important;
        .visit-count {
          padding: .5rem;
          border-color: #f1f1f1;
          .ant-card-body {
            padding: .5rem 1rem !important;
          }
        }
      }
      .project-wrapper {
        padding-right: 0 !important;
        .project-card {
          border: none !important;
          .ant-card-head {
            border-left: 1px solid #f1f1f1 !important;
            border-top: 1px solid #f1f1f1 !important;
            border-right: 1px solid #f1f1f1 !important;
          }
          .ant-card-body {
            padding: 0 !important;
            table {
              width: 100%;
              td {
                width: 50%;
                border: 1px solid #f1f1f1;
                padding: .6rem;
                .project-avatar-wrapper {
                  display:inline-block;
                  float:left;
                  margin-right:.7rem;
                  .project-avatar {
                    color: #42b983;
                    background-color: #d6f8b8;
                  }
                }
              }
            }
          }
          .project-detail {
            display:inline-block;
            float:left;
            text-align:left;
            width: 78%;
            .project-name {
              font-size:.9rem;
              margin-top:-2px;
              font-weight:600;
            }
            .project-desc {
              color:rgba(0, 0, 0, 0.45);
              p {
                margin-bottom:0;
                font-size:.6rem;
                white-space:normal;
              }
            }
          }
        }
      }
    }
  }
</style>
