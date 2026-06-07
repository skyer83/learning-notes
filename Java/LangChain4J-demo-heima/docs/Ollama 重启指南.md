# Ollama 重启指南

Ollama 重启分 3 个系统，分「后台服务重启 / 前台手动启停」

## 一、Linux（systemd 系统服务，最常用）

```bash
# 一键重启服务（推荐）
sudo systemctl restart ollama

# 分步启停
sudo systemctl stop ollama
sudo systemctl start ollama

# 查看运行状态
sudo systemctl status ollama
```

## 二、macOS

### 方式 1：图形最简（推荐）

顶部菜单栏羊驼图标 → Quit Ollama，再从应用点开 Ollama 即可重启。

### 方式 2：终端命令

```bash
# 杀掉全部ollama进程
killall ollama
# 前台启动服务（新开终端常驻）
ollama serve
```

brew 安装专用：

```bash
launchctl stop homebrew.mxcl.ollama
launchctl start homebrew.mxcl.ollama
```

## 三、Windows

### 方式 1：图形（最快）

右下角托盘羊驼图标右键退出，开始菜单重新打开 Ollama。

### 方式 2：CMD/PowerShell 命令

```powershell
# 强制结束进程
taskkill /F /IM ollama.exe
# 前台启动服务，终端关掉后，服务也停止了
ollama serve
# 或执行：ollama list 也会自动启动 ollama，且是后端启动，关掉终端，服务仍在运行
ollama list
```

> 若手动注册成 Windows 服务：
> 

```cmd
net stop Ollama
net start Ollama
```

## 四、通用临时方案（全平台端口卡死）

```bash
# Mac/Linux杀11434端口
lsof -ti :11434 | xargs kill -9
# Windows查端口PID再杀
netstat -ano | findstr :11434
taskkill /F /PID 这里填查到的PID
```

## 补充区分

- `ollama stop`：**只停止正在运行的模型实例**，不关闭 ollama 后台服务（11434 服务还在）

- `ollama serve`：手动在当前终端启动 ollama 后台服务（独占终端，关窗口即停）
