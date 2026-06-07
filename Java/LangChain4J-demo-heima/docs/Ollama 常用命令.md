# Ollama 常用命令

### 1. 模型运行与交互（最常用）
| 命令                           | 说明                                           | 示例                              |
| :----------------------------- | :--------------------------------------------- | :-------------------------------- |
| `ollama run <模型名>`          | 运行模型并进入交互式对话（首次运行会自动下载） | `ollama run llama3.1`             |
| `ollama run <模型名> "提示词"` | 非交互式单次提问                               | `ollama run qwen2 "解释量子计算"` |
| `/bye` 或 `/exit`              | 在交互对话中退出                               | -                                 |
| `/show info`                   | 在对话中查看当前模型信息                       | -                                 |

### 2. 模型管理
| 命令                        | 说明                              | 示例                         |
| :-------------------------- | :-------------------------------- | :--------------------------- |
| `ollama list` / `ollama ls` | 列出本地已安装的所有模型          | `ollama list`                |
| `ollama pull <模型名>`      | 从仓库拉取/更新模型（不自动运行） | `ollama pull gemma:7b`       |
| `ollama rm <模型名>`        | 删除本地模型                      | `ollama rm llama3:latest`    |
| `ollama show <模型名>`      | 查看模型详细信息（参数、大小等）  | `ollama show mistral`        |
| `ollama cp <源> <目标>`     | 复制/重命名模型                   | `ollama cp llama3 my-llama3` |

### 3. 自定义模型
| 命令                             | 说明                          | 示例                                    |
| :------------------------------- | :---------------------------- | :-------------------------------------- |
| `ollama create <名称> -f <文件>` | 通过 Modelfile 创建自定义模型 | `ollama create my-model -f ./Modelfile` |

### 4. 服务与系统
| 命令                 | 说明                                   | 示例               |
| :------------------- | :------------------------------------- | :----------------- |
| `ollama serve`       | 手动启动 Ollama 服务（默认端口 11434） | `ollama serve`     |
| `ollama --version`   | 查看 Ollama 版本                       | `ollama --version` |
| `ollama help [命令]` | 查看帮助文档                           | `ollama help run`  |

### 💡 实用技巧
- **模型命名格式**：`模型名:标签`，如 `qwen2:7b-chat-q4_0`，省略标签默认为 `latest`。
- **健康检查**：服务启动后可通过 `curl http://localhost:11434/health` 验证。
- **停止服务**：Ollama 没有内置 stop 命令，Linux/macOS 可用 `pkill ollama`，Windows 可在任务管理器结束进程。
- **环境变量配置**：可通过设置 `OLLAMA_MODELS` 更改模型存储路径，`OLLAMA_HOST` 更改监听地址。

> ⚠️ **注意**：大多数桌面版安装后 Ollama 会以后台服务自动运行，通常无需手动执行 `ollama serve`，仅在服务器部署或需要自定义端口时才需手动启动。