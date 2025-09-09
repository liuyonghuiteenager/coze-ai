一, git提交类型:

    feat: 新功能
    fix: 修复 Bug
    docs: 文档修改
    style: 代码格式调整（不影响功能）
    refactor: 重构代码（非新增功能或修复 Bug）
    test: 添加或修改测试
    chore: 构建工具或依赖更新
    revert: 回滚提交

二, 部署命令

    nohup java -jar demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev > app.log 2>&1 &