# GitHub常用Emoji

GitHub 支持完整的 **Unicode Emoji** 标准，同时还有一套自定义的 **GitHub Flavored Emoji**（通过 `:shortcode:` 语法使用）。以下是常用分类整理：

### 🔴 颜色相关（替代 font color）

| Shortcode               | Emoji | 用途          |
| :---------------------- | :---- | :------------ |
| `:red_circle:`          | 🔴     | 红色标记/错误 |
| `:green_circle:`        | 🟢     | 绿色标记/成功 |
| `:yellow_circle:`       | 🟡     | 黄色标记/警告 |
| `:blue_circle:`         | 🔵     | 蓝色标记/信息 |
| `:orange_circle:`       | 🟠     | 橙色标记/注意 |
| `:purple_circle:`       | 🟣     | 紫色标记      |
| `:black_circle:`        | ⚫     | 黑色标记      |
| `:white_circle:`        | ⚪     | 白色标记      |
| `:large_red_square:`    | 🟥     | 红色方块      |
| `:large_green_square:`  | 🟩     | 绿色方块      |
| `:large_yellow_square:` | 🟨     | 黄色方块      |
| `:large_blue_square:`   | 🟦     | 蓝色方块      |

### ⚠️ 状态与提示

| Shortcode              | Emoji | 用途      |
| :--------------------- | :---- | :-------- |
| `:warning:`            | ⚠️     | 警告      |
| `:x:`                  | ❌     | 失败/错误 |
| `:white_check_mark:`   | ✅     | 成功/完成 |
| `:heavy_check_mark:`   | ✔️     | 确认      |
| `:exclamation:`        | ❗     | 重要提示  |
| `:question:`           | ❓     | 疑问      |
| `:information_source:` | ℹ️     | 信息说明  |
| `:bulb:`               | 💡     | 提示/想法 |
| `:fire:`               | 🔥     | 热门/紧急 |
| `:star:`               | ⭐     | 推荐/重要 |
| `:rocket:`             | 🚀     | 发布/部署 |
| `:bug:`                | 🐛     | Bug       |
| `:memo:`               | 📝     | 文档/笔记 |
| `:lock:`               | 🔒     | 安全/私有 |
| `:link:`               | 🔗     | 链接      |

### 🏷️ GitHub 自定义 Emoji（非 Unicode）

这些是 GitHub 独有的 shortcode，在其他平台可能不显示：

| Shortcode       | 效果 | 说明             |
| :-------------- | :--- | :--------------- |
| `:octocat:`     | 🐙🐱   | GitHub 吉祥物    |
| `:shipit:`      | 🐿️    | Squirrel（旧版） |
| `:trollface:`   | 😏    | Trollface        |
| `:suspect:`     | 🤔    | 怀疑             |
| `:feelsgood:`   | 👍    | Doom meme        |
| `:finnadie:`    | 😵    | Rage comic       |
| `:goberserk:`   | 😡    | Rage comic       |
| `:godmode:`     | 😈    | Doom meme        |
| `:hurtrealbad:` | 😣    | Rage comic       |
| `:neckbeard:`   | 🧔    | Neckbeard        |
| `:rage1~4:`     | 😠    | Rage 系列        |

### 💡 使用技巧

1.  **自动补全**：在 Issue、PR、README 中输入 `:` 后会自动弹出 emoji 选择器
2.  **完整列表**：访问 [github.com/ikatyang/emoji-cheat-sheet](https://github.com/ikatyang/emoji-cheat-sheet) 查看所有支持的 shortcode
3.  **搜索**：在上述 cheat sheet 页面可按关键词搜索
4.  **组合使用**：可以像这样组织 README：
    ```markdown
    ## 项目状态
    - ✅ 核心功能已完成
    - 🚧 API 文档编写中
    - ❌ 移动端适配未开始
    - ⚠️ 需要 Node.js >= 18
    ```

> **注意**：Emoji 的实际渲染效果取决于用户的操作系统和浏览器，不同平台显示的样式会有差异。Shortcode（如 `:warning:`）在所有平台上都能正确解析为对应的 Unicode Emoji。