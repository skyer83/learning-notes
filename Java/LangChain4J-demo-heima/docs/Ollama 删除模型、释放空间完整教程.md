# Ollama 删除模型、释放空间完整教程

## 一、常规命令删除模型（首选）

### 1. 先查看本机所有已装模型

打开终端 / CMD/PowerShell：

```bash
ollama list
```

会列出**模型全名（带：版本）、大小**，例：

```tex
qwen2.5:7b       xxxxxx  5.2GB
llama3:8b        xxxxxx  4.7GB
```

> 删除必须复制**完整名称（包含：后面标签）**

### 2. 删除单个无用模型

```bash
ollama rm qwen2.5:7b

# 若输出 deleted 'qwen2.5:7b' 即表示删除成功、立刻释放磁盘空间
```

### 3. 一次性删多个模型（空格分隔）

```bash
ollama rm qwen2.5:7b llama3:8b gemma2:2b
```

### 4. 一键删除本机全部模型（谨慎）

**Windows/Linux/Mac 通用**

```bash
ollama list | awk 'NR>1 {print $1}' | xargs ollama rm
```

> NR>1 跳过表头，避免误删报错

## 二、手动彻底删除（命令无效时）

### 各系统模型默认存储路径

- **Windows**：`C:\Users\你的用户名\.ollama\models`
- **Mac/Linux**：`~/.ollama/models`（Linux 系统安装：`/usr/share/ollama/.ollama/models`）

直接进入文件夹，删除不需要的模型目录即可。

## 三、后续避免 C 盘爆满：修改模型存储盘

把后续下载模型存 D/E 盘，不再占用系统盘：

### Windows

1. 此电脑→属性→高级系统设置→环境变量→**新建系统变量**

   变量名：`OLLAMA_MODELS` （全大写）

   变量值：`D:\AI\Ollama_Models`（提前建好文件夹）

2. 重启终端 /ollama 服务，之后 pull 的模型自动存 D 盘

### Mac/Linux

```bash
# zsh(mac)
echo 'export OLLAMA_MODELS=/mnt/d/ollama_models' >> ~/.zshrc
source ~/.zshrc
```

## 四、彻底卸载 Ollama 全软件 + 所有模型

1. Windows：控制面板卸载 Ollama 软件 → 手动删除`C:\Users\xxx\.ollama`整个文件夹
2. Linux：

```bash
sudo systemctl stop ollama
sudo rm -rf ~/.ollama /usr/share/ollama
```

3. Mac：卸载 App，删除`~/.ollama`文件夹