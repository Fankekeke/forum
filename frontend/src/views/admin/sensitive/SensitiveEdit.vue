<template>
  <a-modal v-model="show" title="修改敏感词" @cancel="onClose" :width="450">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label='敏感词名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'keyName',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='敏感词状态' v-bind="formItemLayout">
            <a-switch :checked="checked" @change="onChange">
              <a-icon slot="checkedChildren" type="check" />
              <a-icon slot="unCheckedChildren" type="close" />
            </a-switch>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'sensitiveEdit',
  props: {
    sensitiveEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.sensitiveEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      checked: true
    }
  },
  methods: {
    setFormValues ({...sensitive}) {
      this.rowId = sensitive.id
      let fields = ['keyName', 'deleteFlag']
      let obj = {}
      Object.keys(sensitive).forEach((key) => {
        if (key === 'deleteFlag') {
          this.checked = sensitive[key].toString() === '0'
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = sensitive[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onChange (checked) {
      this.checked = checked
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.deleteFlag = this.checked ? 0 : 1
        if (!err) {
          this.loading = true
          this.$put('/cos/sensitive-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
