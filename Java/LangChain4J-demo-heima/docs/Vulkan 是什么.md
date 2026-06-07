## Vulkan 是什么

Vulkan 是一个跨平台的 GPU 计算 API（类似 CUDA，但是开放标准）。Ollama 用它作为一种**补充/替代**方案来跑模型推理，尤其是：

- 没有 NVIDIA 显卡的机器（AMD、Intel 显卡）
- 跨平台场景（Linux 上 AMD GPU 主要靠 Vulkan 或 ROCm）

---

## 你的情况为什么需要关掉它

在你的笔记本上发生了连锁反应：

| 步骤 | 发生了什么                                                   |
| ---- | ------------------------------------------------------------ |
| 1    | `OLLAMA_VULKAN=true`（默认）启用 Vulkan 后端                 |
| 2    | Vulkan 检测到两张"显卡"：Intel UHD 630 + GTX 1060            |
| 3    | Vulkan 把 GTX 1060（Max-Q 笔记本独显）**误判为集成显卡**，丢弃 |
| 4    | 只剩 Intel UHD 630 可选，模型跑在核显上                      |
| 5    | CUDA 后端虽然正确识别了 GTX 1060，但没被选中                 |

关掉 `OLLAMA_VULKAN=false` 后，Vulkan 不再参与竞争，CUDA 成为唯一后端，GTX 1060 才能真正用上。

---

## 简单总结

`OLLAMA_VULKAN` 控制 Ollama 是否启用 **Vulkan 计算后端**。

- **`OLLAMA_VULKAN=true`**：Ollama 会使用 Vulkan（默认值，对非 NVIDIA 卡有用）
- **`OLLAMA_VULKAN=false`**：Ollama 跳过 Vulkan，只用 CUDA（适合有 NVIDIA 独显的机器）

在有 NVIDIA 独显机器的场景下，Vulkan 属于"帮倒忙"，关掉就对了。