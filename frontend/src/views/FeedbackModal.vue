<template>
    <div>
        <Form :model="formItem" ref="feedbackForm" :rules="ruleValidate" :label-width="50">
            <FormItem label="评分" prop="rate">
                <Rate show-text v-model="formItem.rate" :count="8">
                    <span style="vertical-align: top">{{rateLevel[formItem.rate - 1]}}</span>
                </Rate>
            </FormItem>
            <FormItem label="邮箱" prop="email">
                <Input v-model="formItem.email" style="width: 400px"/>
            </FormItem>
            <br>
            <FormItem label="建议" prop="suggest">
                <Input v-model="formItem.suggest" type="textarea" style="width: 480px" :rows="8"
                       placeholder="输入您的宝贵意见和建议"></Input>
            </FormItem>
        </Form>
    </div>
</template>

<script>
    export default {
        name: "feedback-modal",
        data() {
            return {
                formItem: {
                    rate: 4,
                    email: '',
                    suggest: '',
                },
                ruleValidate: {
                    email: [
                        {type: 'email', message: '邮箱格式错误', trigger: 'blur'}
                    ],
                },
                rateLevel: ['倔强青铜', '秩序白银', '荣耀黄金', '尊贵铂金', '永恒钻石', '至尊星耀', '最强王者', '荣耀王者']
            }
        },
        methods: {}
    }
</script>

<style scoped>
</style>