# 常驻服务说明

Ollama 本身就是一个**模型服务引擎**，启动后所有已加载的模型都会以常驻服务的形式在后台运行，无需为每个模型单独启动进程。

### 🚀 启动常驻服务

```bash
ollama serve
```

执行后 Ollama 会在后台监听 `http://localhost:11434`，通过 API 随时响应请求。模型采用**按需加载 + 自动驻留**机制：首次调用时自动加载到显存/内存，空闲超时后自动卸载，再次调用时重新加载。

### ⏱️ 控制模型常驻时间

默认空闲 **5 分钟** 后自动卸载。如需调整：

```bash
# 设置为 30 分钟（环境变量方式）
OLLAMA_KEEP_ALIVE=30m ollama serve

# 永久常驻（不自动卸载）
OLLAMA_KEEP_ALIVE=-1 ollama serve
```

也可以在单次请求中指定：
```bash
curl http://localhost:11434/api/chat -d '{
  "model": "qwen2",
  "messages": [{"role": "user", "content": "你好"}],
  "keep_alive": "2h"
}'
```

### 🔧 生产环境推荐：Systemd 托管

手动 `ollama serve` 关闭终端就会停止，生产环境建议用 systemd：

```ini
# /etc/systemd/system/ollama.service
[Unit]
Description=Ollama Service
After=network.target

[Service]
ExecStart=/usr/local/bin/ollama serve
Environment="OLLAMA_KEEP_ALIVE=-1"
Environment="OLLAMA_HOST=0.0.0.0:11434"
Restart=always
User=ollama

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now ollama
```

### 💡 关键概念澄清

| 误区                       | 正确理解                                 |
| :------------------------- | :--------------------------------------- |
| 需要为每个模型启动一个服务 | ✅ 一个 `ollama serve` 管理所有模型       |
| `ollama run` 是启动后端    | ✅ `run` 只是交互式客户端，后端靠 `serve` |
| 模型必须提前加载才能用     | ✅ 首次 API 调用会自动拉取并加载          |
| 桌面版需要手动 serve       | ✅ macOS/Windows 安装后自动以后台服务运行 |

> **总结**：只需一条 `ollama serve` 即可让所有模型以常驻服务形式可用。生产环境配合 systemd + `OLLAMA_KEEP_ALIVE=-1` 实现真正的永久常驻。

# 常驻时间说明

设置 `OLLAMA_KEEP_ALIVE=30m` 的具体操作取决于你的运行环境。以下是三种最常见场景的操作方法：

### 1. 临时生效（当前终端会话）
适合测试或一次性使用，关闭终端后失效：

```bash
# Linux / macOS
OLLAMA_KEEP_ALIVE=30m ollama serve

# Windows PowerShell
$env:OLLAMA_KEEP_ALIVE="30m"; ollama serve

# Windows CMD
set OLLAMA_KEEP_ALIVE=30m && ollama serve
```

### 2. 永久生效（推荐生产环境）

#### Linux (Systemd)
编辑服务文件：
```bash
sudo systemctl edit ollama
```
添加以下内容：
```ini
[Service]
Environment="OLLAMA_KEEP_ALIVE=30m"
```
重载并重启：
```bash
sudo systemctl daemon-reload
sudo systemctl restart ollama
```

#### macOS (桌面版)
通过 `launchctl` 为 GUI 应用注入环境变量：
```bash
launchctl setenv OLLAMA_KEEP_ALIVE "30m"
```
然后**退出并重新打开 Ollama 应用**使其生效。

> 💡 如需持久化（重启后仍生效），将 `launchctl setenv OLLAMA_KEEP_ALIVE "30m"` 添加到 `~/.zshrc` 或 `~/.bash_profile` 中。

#### Windows (桌面版)
1.  按 `Win + R`，输入 `sysdm.cpl`，回车
2.  切换到 **"高级"** 选项卡 → 点击 **"环境变量"**
3.  在 **"用户变量"** 或 **"系统变量"** 中新建：
    -   变量名：`OLLAMA_KEEP_ALIVE`
    -   变量值：`30m`
4.  确定保存后，**重启 Ollama 托盘程序**

#### Docker
```bash
docker run -e OLLAMA_KEEP_ALIVE=30m -p 11434:11434 ollama/ollama
```
或在 `docker-compose.yml` 中：
```yaml
environment:
  - OLLAMA_KEEP_ALIVE=30m
```

### ⏱️ 支持的时间格式

| 写法    | 含义               |
| :------ | :----------------- |
| `30m`   | 30 分钟            |
| `2h`    | 2 小时             |
| `1h30m` | 1 小时 30 分钟     |
| `-1`    | 永久常驻，永不卸载 |
| `0`     | 每次请求后立即卸载 |

### ✅ 验证是否生效

```bat
:: CMD 中使用双引号 + 转义
:: curl http://localhost:11434/api/show -d '{"model": "你的模型名"}'
curl http://localhost:11434/api/show -d "{\"model\": \"qwen3.5:0.8b\"}"
```
> ⚠️ CMD 中 JSON 的每个内部双引号都必须用 `\"` 转义，外层用 `"` 包裹整个 `-d` 参数。

```powershell
# PowerShell 原生支持单引号包裹 JSON，无需转义
curl.exe http://localhost:11434/api/show -d '{\"model\": \"qwen3.5:0.8b\"}'
```

> ⚠️ 注意：这里外层用的是**单引号**，但内部手动写了 `\"`。PowerShell 单引号内不解析转义，`\"` 会被原样传给 curl.exe，而 curl.exe 能正确识别 `\"` 为 JSON 双引号。**这是最推荐的写法**

或者在交互模式中执行 `/show info`，观察模型加载后的空闲计时行为是否符合预期。也可以直接检查进程环境变量：

```bash
# Linux
cat /proc/$(pgrep ollama)/environ | tr '\0' '\n' | grep KEEP_ALIVE
```

> ⚠️ **注意**：修改环境变量后必须**重启 Ollama 服务**才能生效，仅修改配置不会热更新到已运行的进程。