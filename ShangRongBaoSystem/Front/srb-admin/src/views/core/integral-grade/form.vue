<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form label-width="120px">
      <el-form-item label="借款额度">
        <el-input-number v-model="integralGrade.borrowAmount" :min="0" />
      </el-form-item>
      <el-form-item label="积分区间开始">
        <el-input-number v-model="integralGrade.integralStart" :min="0" />
      </el-form-item>
      <el-form-item label="积分区间结束">
        <el-input-number v-model="integralGrade.integralEnd" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="saveOrUpdate()"
        >
          保存
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import integralGradeApi from '@/api/core/integral-grade'

export default {
  data() {
    return {
      integralGrade: {},
      saveBtnDisabled: false, //是否禁用保存按钮，防止表单重复提交
    }
  },
  created() {
    if (this.$route.params.id && this.$route.params.id > 0)
      this.getById(this.$route.params.id)
  },
  methods: {
    saveOrUpdate() {
      this.saveBtnDisabled = true
      if (this.integralGrade.id && this.integralGrade.id > 0) this.updateData()
      else this.saveData()
    },
    saveData() {
      integralGradeApi.save(this.integralGrade).then((response) => {
        this.$message.success(response.message)
        this.$router.push('/core/integral-grade/list')
      })
    },
    updateData() {
      integralGradeApi.update(this.integralGrade).then((response) => {
        this.$message.success(response.message)
        this.$router.push('/core/integral-grade/list')
      })
    },
    getById(id) {
      integralGradeApi.getById(id).then((response) => {
        this.integralGrade = response.data.record
      })
    },
  },
}
</script>
