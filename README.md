# Kona

Kona 是一个使用现代 Android 开发技术重构的应用程序。

支持 Konachan, Yande.re, Danbooru, etc.

<img src="docs/Screenshot_20250529_135452.png" width="50%" alt="Screenshot 1">
<img src="docs/Screenshot_20250529_135510.png" width="50%" alt="Screenshot 2">
<img src="docs/Screenshot_20250529_135535.png" width="50%" alt="Screenshot 3">

## 技术栈

- **UI 框架**: Jetpack Compose
- **架构模式**: MVI (Model-View-Intent)
- **依赖注入**: Hilt
- **协程与 Flow**: 用于异步操作
- **Material Design 3**: 用于现代化 UI 组件

## 项目结构

项目采用模块化架构，主要包含以下组件：

- `bizMain`: 主要业务模块，包含核心功能
- 通用工具和共享组件

## 特性

- 使用 Jetpack Compose 构建的现代化 UI
- 清晰的架构实现
- 使用 Kotlin Flow 的响应式编程
- 基于 MVI 模式的高效状态管理

## 开始使用

1. 克隆仓库
2. 在 Android Studio 中打开项目
3. 构建并运行应用

## 环境要求

- Android Studio Arctic Fox 或更新版本
- Kotlin 1.8.0 或更新版本
- Android SDK 21 或更新版本

# TODO
- [ ] 将 libDatabase 重命名为 bizDatabase，因为该模块涉及到业务逻辑。 