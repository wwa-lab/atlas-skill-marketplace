# Atlas Marketplace 产品需求文档（PRD）

**文档版本：** v0.2 Draft
**更新时间：** 2026-08-14
**产品名称：** Atlas Marketplace
**当前原型：** `atlas_marketplace_v2_6_mvp_plus_day2.html`
**目标读者：** 产品、设计、前端、后端、DevOps、Skill Owner、内部平台团队

---

## 0. v0.2 变更摘要

本版本新增 **One-click Update** 产品要求：

- 用户点击 Update 后无需手工操作目录；
- 系统先下载并校验最新版本；
- 将当前 Skill Rename 为带版本和时间戳的 Backup；
- 将最新版本安装到原 Skill 路径；
- 安装失败时自动回滚；
- 允许在备份保留期内手动回滚；
- 引入本地执行组件和版本化 Distribution Artifact 的架构要求。

---

## 1. 文档目的

本文档定义 Atlas Marketplace 的产品定位、目标用户、MVP 范围、Day 2 能力、核心业务流程、信息架构、数据模型、权限、非功能要求、成功指标及交付计划。

本文档以当前已确认的产品方向和 V2.6 原型为基础，作为后续设计评审、技术方案设计、开发拆分和验收的统一依据。

---

## 2. 产品摘要

### 2.1 一句话定位

> **Atlas Marketplace 是一个集中式的内部 AI Skill 注册、发现和管理平台，Skill 源代码继续保存在各自的分布式 GitHub Repository 中。**

英文定位：

> **A centralized marketplace for discovering and managing internal AI Skills across distributed repositories.**

### 2.2 产品核心价值

Atlas Marketplace 不托管 Skill 源代码，也不在 MVP 中提供在线执行或 Workflow 编排。它重点解决以下问题：

1. 内部 Skill 分散在不同团队和个人的 GitHub Repository 中，难以发现。
2. 用户不知道某个 Skill 解决什么问题、属于哪个 SDLC Phase、如何安装和使用。
3. Skill 缺少统一的 Owner、版本、兼容性、文档和更新状态。
4. 用户安装后缺少 Update、Uninstall 和安装状态管理。
5. Skill Owner 缺少基础的使用反馈和生命周期管理能力。
6. 随着 Skill 数量增长，需要更好的推荐、集合、评分和运营能力。

### 2.3 产品边界

Atlas Marketplace 是：

- 内部 AI Skill 的统一入口；
- 分布式 GitHub Repository 上方的集中 Registry；
- Skill 安装包或版本化分发 Artifact 的统一分发入口，但不集中托管源代码；
- Skill 的发现、理解、安装、一键更新、卸载和反馈平台；
- 按 SDLC Phase 组织 Skill 的产品化 Marketplace。

Atlas Marketplace 不是：

- Skill 源代码托管平台；
- GitHub 的替代品；
- 在线 Skill Runtime；
- Agent 或 Workflow 编排平台；
- 面向外部用户的公共应用商店；
- 强制所有团队迁移到同一个 Monorepo 的平台。

---

## 3. 背景与问题

### 3.1 当前状态

内部不同团队和开发者已经创建了多个 AI Skills。这些 Skill：

- 分散在不同的 GitHub Repository；
- 使用不同的目录结构和文档习惯；
- 可能带有部门名称、内部系统名称和组织标识；
- 主要服务于 GitHub Copilot、OpenCode 或类似编码助手；
- 覆盖 Planning、Discovery、Build、Testing 等不同 SDLC Phase；
- 也包含跨阶段使用的 Common Skills。

目前用户通常依靠口头传播、聊天消息、内部文档或直接询问作者来发现和使用 Skill。随着 Skill 数量增加，这种方式不可持续。

### 3.2 用户痛点

#### Skill 使用者

- 不知道公司内部已经有哪些 Skill。
- 不知道应该搜索 Skill 名称，还是按任务或 SDLC Phase 寻找。
- 点进 Repository 后仍然不清楚如何上手。
- 无法快速判断 Skill 是否值得使用。
- 不知道 Skill 是否还在维护。
- 安装后难以统一查看、更新或卸载。

#### Skill Owner

- Skill 很难被内部用户发现。
- 需要重复回答“怎么安装”“怎么用”“支持什么工具”等问题。
- 缺少统一的版本展示和发布入口。
- 缺少评分、反馈和使用情况。
- 无法及时发现 Skill 已过期、文档不足或需要更新。

#### 平台管理员

- 无法建立内部 Skill 的统一目录和质量标准。
- 无法明确 Skill Owner 和责任边界。
- 无法查看整体覆盖了哪些 SDLC Phase。
- 无法识别重复、过期或长期无人维护的 Skill。
- 缺少 Marketplace 级别的 adoption 数据。

---

## 4. 产品愿景与设计原则

### 4.1 产品愿景

让内部用户在几分钟内完成以下闭环：

> **发现合适的 Skill → 判断是否可信 → 理解如何使用 → 安装 → 更新或卸载 → 提交反馈**

同时让 Skill Owner 保持对自己 Repository 的完全控制。

### 4.2 设计原则

#### P1. 简单、自然、易推广

用户进入平台后不需要培训即可理解主要操作。首页重点突出搜索、Featured Skills 和 SDLC 浏览，不使用复杂 Dashboard 作为首屏。

#### P2. 分布式 Repository，集中式 Registry

Skill 继续保存在 Owner 自己的 GitHub Repository 中。Marketplace 只保存和索引元数据，不复制和集中托管源代码。

#### P3. 按 SDLC Phase 建立清晰上下文

每个 Skill 必须至少指定一个 Primary SDLC Phase：

1. Planning
2. Estimation
3. Discovery
4. Build
5. Testing
6. Deployment
7. Maintenance

Common Skills 可以标记为跨阶段使用，但仍应提供推荐使用阶段或使用场景。

#### P4. Skill Detail 必须帮助用户做决定

用户应能在一个页面中明确知道：

- Skill 是做什么的；
- 适用于哪个 Phase；
- 谁负责维护；
- 如何安装；
- 如何使用；
- 支持哪些工具；
- 最新版本是什么；
- 最近是否更新；
- 质量和用户反馈如何。

#### P5. 不在当前阶段做在线执行

MVP 和 Day 2 仍然聚焦 Marketplace 管理能力，不引入在线 Skill Runtime、Agent 编排或 Workflow Engine。

#### P6. 安装与更新体验以“一键、安全、可回滚”为目标

用户在 Marketplace 中点击 **Update** 后，应完成一次确认即可更新到最新版本。更新过程对用户表现为单一操作，底层必须包含备份、安装、校验和失败回滚。

为实现浏览器发起的本地文件操作，Atlas Marketplace 需要配套至少一种受控执行组件：

- Atlas Local Installer；
- Atlas CLI；
- IDE / Host Extension；
- 企业托管的本地辅助程序。

不支持本地执行组件的环境可以提供命令作为 fallback，但“一键更新”是目标体验，而不是仅展示命令。

#### P7. 高级但克制的视觉语言

视觉参考成熟的 Developer Marketplace 和高端 SaaS 产品：

- Product website 风格首页；
- 大 Hero、清晰的信息层级；
- Featured Skills；
- 高质量内容卡片；
- 浅色与深色主题；
- 避免强烈“AI 模板感”、机器人图标和过度发光效果。

---

## 5. 目标与非目标

### 5.1 MVP 目标

1. 为内部 Skill 提供一个统一、可搜索的 Marketplace。
2. 让用户按 SDLC Phase、关键词、Owner、Tag 和 Category 发现 Skill。
3. 让用户在 Skill Detail 页面快速理解 Skill 的价值和上手方式。
4. 支持从分布式 GitHub Repository 注册 Skill。
5. 支持版本展示和基础版本管理。
6. 支持 Install、One-click Update、Uninstall 的完整管理闭环，其中更新必须自动备份当前版本并支持失败回滚。
7. 提供 My Skills 页面统一查看已安装 Skill。
8. 提供基础评分和反馈。
9. 支持 Owner 和管理员的基础管理操作。

### 5.2 Day 2 目标

1. 提供多维度质量评分和健康状态。
2. 提供 Leaderboard、Trending 和 Most Installed。
3. 提供 Curated Collections。
4. 提供 Save / Favorite。
5. 提供个性化或场景化推荐。
6. 提供 Owner Insights 和 adoption analytics。
7. 提供过期、版本不同步和反馈待处理提醒。
8. 提供更成熟的审核、认证和治理能力。

### 5.3 非目标

以下能力不属于 MVP，也不属于当前 Day 2 的必做范围：

- 在线执行 Skill；
- Agent Runtime；
- Workflow / Playbook 编排；
- Prompt Playground；
- 在 Marketplace 中直接编辑完整 Skill 源代码；
- 将所有 Skill 迁移到统一 Repository；
- 面向外部客户的公开 Marketplace；
- 商业支付、订阅或结算能力。

---

## 6. 目标用户与角色

### 6.1 Skill Consumer

主要任务：

- 发现 Skill；
- 按 SDLC Phase 浏览；
- 查看 Skill Detail；
- 安装、更新、卸载；
- 保存 Skill；
- 提交评分和反馈。

### 6.2 Skill Owner / Maintainer

主要任务：

- 注册 Skill；
- 维护元数据；
- 发布新版本；
- 查看 Repository 同步状态；
- 查看评分、反馈和使用趋势；
- 回应问题；
- 标记 Skill Deprecated 或 Archived。

### 6.3 Marketplace Admin

主要任务：

- 审核和认证 Skill；
- 管理分类、Tag 和 SDLC Phase；
- 管理 Owner 和权限；
- 下架不合规或过期 Skill；
- 管理 Featured、Collections 和 Leaderboard 规则；
- 查看 Marketplace 整体 adoption 和健康状态。

---

## 7. 信息架构

### 7.1 顶部导航

#### MVP

- Featured
- Browse by SDLC
- My Skills
- Register Skill

#### Day 2

- Leaderboard
- Collections
- Insights

### 7.2 首页结构

1. Global Navigation
2. Hero
3. Search
4. SDLC Phase Filter
5. Marketplace Statistics
6. Featured Skills
7. Browse by SDLC
8. Day 2：Leaderboard
9. Day 2：Curated Collections
10. Day 2：Owner Insights
11. Why Atlas Marketplace
12. Footer

### 7.3 Skill Detail

- Header
- Primary SDLC Phase
- Summary
- Owner
- Verified / Health Status
- Install Target
- Overview
- Quick Start
- Example Input / Output
- Compatibility
- Version History
- Changelog
- Rating / Feedback
- Repository and Issue Links

---

## 8. MVP 功能需求

## 8.1 Discover 首页

### FR-DIS-001：Hero 与产品定位

首页首屏必须清楚表达 Atlas Marketplace 的作用，并提供：

- 主标题；
- 简短说明；
- Explore Skills CTA；
- Register Skill CTA；
- 全局搜索入口。

**验收标准：**

- 用户不滚动页面即可理解平台是内部 Skill Marketplace。
- 用户可从首屏进入搜索、浏览或注册流程。

### FR-DIS-002：全局搜索

搜索范围包括：

- Skill Name；
- Description；
- Use Case；
- Owner；
- Tag；
- Category；
- Primary SDLC Phase；
- Repository Name。

**验收标准：**

- 输入关键词后 300ms 内更新结果或显示 loading 状态。
- 无结果时显示明确空状态。
- 搜索结果可与 Phase Filter 同时生效。

### FR-DIS-003：按 SDLC Phase 浏览

平台必须支持以下 Phase：

| 顺序 | Phase |
|---|---|
| 1 | Planning |
| 2 | Estimation |
| 3 | Discovery |
| 4 | Build |
| 5 | Testing |
| 6 | Deployment |
| 7 | Maintenance |

每个 Skill 必须有一个 Primary Phase，并允许后续增加 Secondary Phases。

**验收标准：**

- 首页提供 All Phases 和 7 个 Phase Filter。
- 每张 Skill Card 清楚显示 Primary Phase。
- Browse by SDLC 区域按 Phase 分组显示 Skill。
- 点击 Phase 后仅展示相关 Skill。

### FR-DIS-004：Featured Skills

管理员可配置 Featured Skills。

Featured Skill 应优先满足：

- 文档完整；
- Owner 清晰；
- 最近有维护；
- 使用量或评分较高；
- 对内部推广具有代表性。

**验收标准：**

- 首页最多展示 6 个 Featured Skills。
- Featured 顺序可由管理员配置。
- 未配置时可按默认质量分排序。

### FR-DIS-005：Skill Card

每张 Skill Card 至少展示：

- Name；
- One-line Description；
- Owner；
- Verified 状态；
- Primary Phase；
- Category；
- Tags；
- Latest Version；
- Quality Score；
- Rating；
- Install Count；
- Save；
- Install 或 View Detail 入口。

---

## 8.2 Skill Detail

### FR-DET-001：核心信息

Skill Detail 必须展示：

- Name；
- Description；
- Owner / Maintainer；
- Primary SDLC Phase；
- Category / Tags；
- Latest Version；
- Last Updated；
- Repository；
- Compatibility；
- Install Count；
- Rating；
- Quality / Health Status。

### FR-DET-002：How to Use / Quick Start

必须提供：

1. 适用场景；
2. 安装步骤；
3. 如何在目标工具中调用；
4. 示例输入；
5. 示例输出或输出说明；
6. 已知限制。

**验收标准：**

- 用户无需打开 Repository README 即可完成第一次使用。
- 至少包含一个可复制的使用示例。

### FR-DET-003：兼容性

至少支持声明以下目标：

- GitHub Copilot；
- OpenCode；
- Future / Custom Host。

兼容性由 Skill Metadata 声明。

### FR-DET-004：Repository 与 Issue

Skill Detail 提供：

- Open Repository；
- View Metadata；
- Contact Owner；
- Report Issue。

权限仍由 GitHub 或内部身份系统控制。

---

## 8.3 Register Skill

### FR-REG-001：以 Repository 注册，而不是上传代码

入口名称为：

> **Register Skill**

注册流程：

1. 输入 GitHub Repository URL；
2. 输入或选择 Metadata Path；
3. 平台读取 Metadata；
4. 校验必填字段；
5. 选择 Primary SDLC Phase；
6. Preview；
7. Publish。

### FR-REG-002：Metadata Validation

必须校验：

- Name；
- Slug；
- Description；
- Owner；
- Repository；
- Version；
- Primary Phase；
- Compatibility；
- Quick Start；
- Install / Update / Uninstall Instructions。

校验结果分为：

- Error：无法发布；
- Warning：允许发布但提示改进；
- Passed：可发布。

### FR-REG-003：Preview

发布前必须显示 Marketplace Preview，包括：

- Skill Card Preview；
- Skill Detail Header Preview；
- Phase、Tags 和 Compatibility；
- Validation Result。

### FR-REG-004：Repository Sync

MVP 支持以下任一同步方式：

- 手动 Refresh；
- GitHub Webhook；
- 定时同步。

具体实现由技术方案决定。

---

## 8.4 Version Management

### FR-VER-001：版本展示

每个 Skill 展示：

- Latest Version；
- Current Published Version；
- Version History；
- Release Date；
- Changelog；
- Compatibility；
- Deprecated Status。

### FR-VER-002：版本同步

当 Repository 中 Metadata Version 高于 Marketplace Published Version 时：

- Owner 页面显示 Update Available；
- Owner 可 Review 并 Publish；
- Day 2 可增加自动提醒。

### FR-VER-003：版本状态

支持：

- Draft；
- Published；
- Deprecated；
- Archived。

---

## 8.5 Install、Update 与 Uninstall

### FR-INS-001：安装目标选择

用户安装前选择目标环境：

- GitHub Copilot；
- OpenCode；
- 未来扩展目标。

### FR-INS-002：MVP 安装方式

MVP 优先提供：

- 可复制安装命令；
- 安装路径说明；
- 前置依赖检查说明；
- 安装成功确认；
- 安装版本记录。

不要求浏览器直接写入用户本地文件系统。

### FR-INS-003：One-click Update

当 Installed Version 低于 Latest Version 时：

- My Skills 和 Skill Detail 显示 **Update Available**；
- 用户可先查看 Changelog、Compatibility 和目标版本；
- 用户点击 **Update** 并确认后，系统自动完成备份、替换、校验和状态更新；
- 用户不需要手工进入目录、重命名文件夹或再次执行安装命令；
- 更新后记录新的 Installed Version、更新时间、Backup Path 和 Update Result。

从用户角度，Update 是一次点击操作。从系统角度，必须执行以下安全流程：

1. 检查当前安装路径、版本、目标环境、目录权限和磁盘空间；
2. 获取最新版本的可信安装包或版本化 Distribution Artifact；
3. 将新版本下载到临时 Staging 目录；
4. 校验版本、Metadata、Checksum / Signature、目录结构和兼容性；
5. 将当前 Skill 目录原子重命名为备份目录，例如：
   `<skill-name>.backup-<old-version>-<timestamp>`；
6. 将已校验的新版本切换或安装到原 Skill 路径；
7. 执行安装后校验；
8. 更新 Marketplace 的安装记录；
9. 按策略保留或清理旧版本备份。

为了缩短不可用时间，推荐先完成下载与校验，再执行旧目录 Rename 和新目录切换。

### FR-INS-003A：自动回滚

若在旧版本 Rename 之后发生下载、写入、校验或启动失败，系统必须：

- 删除或隔离不完整的新版本；
- 将 Backup 目录恢复为原 Skill 目录；
- 将安装状态恢复为原版本；
- 向用户显示清晰失败原因；
- 保留 Update Job 和 Audit Log。

### FR-INS-003B：手动回滚

在备份保留期内，用户可选择 **Rollback to Previous Version**。

回滚后：

- 当前失败或不满意的新版本被重命名或移入隔离目录；
- 上一个 Backup 恢复到正式 Skill 路径；
- My Skills 显示恢复后的版本；
- 系统记录回滚原因和结果。

### FR-INS-003C：分发来源

用户从 Atlas Marketplace 发起更新。实际最新版本可以来自：

- Marketplace 管理的版本化 Distribution Artifact；
- GitHub Release Artifact；
- 由 Registry 解析并签名的 Repository Package URL。

无论采用哪种方式，Skill 源代码仍可保留在分布式 Repository 中。Marketplace 可以托管或缓存安装 Artifact，但不要求集中托管 Repository。

### FR-INS-004：Uninstall

用户可在 My Skills 或 Skill Detail 中执行 Uninstall。

卸载流程：

1. 选择环境；
2. 显示将移除的 Skill 和版本；
3. 显示影响说明；
4. 用户确认；
5. 提供卸载命令或调用安装辅助工具；
6. 更新安装状态。

### FR-INS-005：本地执行组件

One-click Install / Update / Uninstall 需要本地执行组件。MVP 应至少选定并支持一种实现：

- Atlas Local Installer；
- Atlas CLI；
- IDE Extension；
- Enterprise Device Management Integration。

Web 页面负责发起操作、展示进度和结果；本地执行组件负责目录 Rename、文件写入、版本切换、校验和回滚。

若某个环境暂不支持本地执行组件，可以提供命令式 fallback，但该环境不得标记为“支持一键更新”。

---

## 8.6 My Skills

### FR-MYS-001：已安装列表

展示：

- Skill Name；
- Owner；
- Primary Phase；
- Installed Version；
- Latest Version；
- Status；
- Environment；
- Last Used；
- Update；
- Uninstall。

### FR-MYS-002：状态

支持：

- Installed；
- Enabled；
- Disabled；
- Update Available；
- Install Incomplete；
- Uninstalled。

### FR-MYS-003：多环境

同一个 Skill 可安装到多个目标环境，并分别记录版本和状态。

---

## 8.7 基础评分与反馈

### FR-FBK-001：基础评分

MVP 支持：

- 1–5 星评分；
- Useful / Not Useful；
- 简短文本反馈。

### FR-FBK-002：问题类型

可选择：

- Output inaccurate；
- Documentation unclear；
- Installation failed；
- Compatibility issue；
- Missing use case；
- Feature request；
- Other。

### FR-FBK-003：关联 Owner

反馈必须关联：

- Skill；
- Version；
- Environment；
- Owner；
- 提交时间；
- 用户身份。

---

## 8.8 基础 Owner 管理

### FR-OWN-001：Manage Published Skills

Owner 可查看：

- 自己维护的 Skills；
- Published Version；
- Repository Sync Status；
- Install Count；
- Rating；
- Feedback Count；
- Last Updated；
- Manage 入口。

### FR-OWN-002：Skill Lifecycle

Owner 可：

- 更新元数据；
- 发布新版本；
- 标记 Deprecated；
- Archive；
- 转移 Owner；
- 查看反馈。

---

## 9. Day 2 功能需求

Day 2 能力应在 MVP adoption 和数据质量达到一定水平后启用。

## 9.1 多维度质量评分

质量分不应只由用户星级决定。建议包含：

| 维度 | 示例指标 |
|---|---|
| Documentation | Quick Start、示例、README 完整度 |
| Freshness | 最近更新时间、版本同步状态 |
| Usage | 安装量、活跃用户、重复使用 |
| Reliability | 安装成功率、问题数量 |
| Feedback | 评分、Useful Rate、负面反馈 |
| Ownership | Owner 是否有效、响应时间 |
| Compatibility | 已验证目标环境数量 |

最终分数的具体权重由 Product、Engineering 和 Governance 共同定义。

## 9.2 Leaderboard

支持：

- Top Quality；
- Most Installed；
- Highest Rated；
- Rising This Month；
- Recently Updated；
- Editor’s Choice。

应防止仅靠安装量造成不公平排名。

## 9.3 Curated Collections

Collection 可以按以下方式组织：

- Job-to-be-done；
- Technology；
- Team；
- Initiative；
- Common Use Case；
- End-to-end Delivery Scenario。

示例：

- Legacy Modernization；
- New Feature Delivery；
- Engineering Productivity；
- AS400 / IBM i；
- Testing Essentials。

Collection 不是 Workflow，不负责自动编排执行。

## 9.4 Save / Favorite

用户可保存 Skill，并在个人页面查看 Saved Skills。

后续可用于：

- 推荐；
- 订阅更新；
- Collection；
- 快速安装。

## 9.5 Recommendation

Day 2 可基于以下信号推荐：

- 用户团队；
- 已安装 Skill；
- 当前浏览 Phase；
- 收藏；
- 角色；
- 相似用户；
- Trending；
- 管理员推荐。

推荐必须可解释，例如：

> Recommended because your team uses three Build-phase API Skills.

## 9.6 Owner Insights

Owner 可查看：

- Installs；
- Active Users；
- Update Adoption；
- Uninstall Rate；
- Rating Trend；
- Feedback Breakdown；
- Top Environments；
- Version Distribution；
- Search-to-Install Conversion；
- Repository Sync Status。

## 9.7 Lifecycle Alerts

提醒类型包括：

- 超过 N 天未更新；
- Repository 有新版本；
- Metadata 校验失败；
- Owner 无效；
- 负面反馈增加；
- 安装失败率升高；
- 兼容环境发生变化；
- Deprecated 版本仍有大量用户。

## 9.8 Certification / Verification

Day 2 可提供：

- Verified Owner；
- Official；
- Security Reviewed；
- Documentation Complete；
- Production Ready；
- Deprecated。

认证规则需有明确审核流程，不能仅由 Owner 自行声明。

---

## 10. 核心用户流程

## 10.1 用户发现并安装 Skill

1. 用户进入首页；
2. 通过搜索或 Phase Filter 浏览；
3. 查看 Featured 或 Phase 下的 Skill；
4. 打开 Skill Detail；
5. 查看 Owner、Phase、Quality、Quick Start 和 Compatibility；
6. 选择 GitHub Copilot 或 OpenCode；
7. 获取安装命令；
8. 完成安装；
9. Marketplace 记录安装状态；
10. Skill 出现在 My Skills。

## 10.2 用户一键更新 Skill

1. My Skills 显示 Update Available；
2. 用户查看目标版本、Changelog 和 Compatibility；
3. 用户点击 Update 并确认目标环境；
4. 本地执行组件检查路径、权限和磁盘空间；
5. 系统下载并校验最新版本到 Staging 目录；
6. 当前 Skill 目录被 Rename 为带版本和时间戳的 Backup；
7. 最新版本安装或原子切换到原 Skill 路径；
8. 系统执行安装后校验；
9. 成功时，Marketplace 更新 Installed Version，并保留 Backup 信息；
10. 失败时，系统自动恢复原 Backup，并向用户显示失败原因；
11. 用户可在备份保留期内手动 Rollback。

## 10.3 用户卸载 Skill

1. 用户在 My Skills 点击 Uninstall；
2. 选择环境；
3. 确认影响；
4. 执行卸载；
5. Marketplace 将状态更新为 Uninstalled。

## 10.4 Owner 注册 Skill

1. Owner 点击 Register Skill；
2. 输入 Repository；
3. 指定 Metadata Path；
4. 平台读取 Metadata；
5. Owner 选择 Primary Phase；
6. 查看 Validation；
7. Preview；
8. Publish；
9. Skill 出现在 Marketplace。

## 10.5 Owner 发布新版本

1. Repository Version 更新；
2. Marketplace 检测到变更；
3. Owner Review；
4. 确认 Changelog 和 Compatibility；
5. Publish；
6. 已安装旧版本的用户看到 Update Available。

---

## 11. 数据模型

## 11.1 Skill

| 字段 | 说明 |
|---|---|
| skill_id | 平台唯一标识 |
| slug | URL 与命令使用的唯一名称 |
| name | Skill 名称 |
| short_description | 卡片摘要 |
| long_description | Detail 页面说明 |
| owner_id | Owner / Team |
| repository_url | GitHub Repository |
| metadata_path | Metadata 文件路径 |
| primary_phase | Primary SDLC Phase |
| secondary_phases | 可选 Secondary Phases |
| category | 主分类 |
| tags | 标签 |
| status | Draft / Published / Deprecated / Archived |
| verified_status | 未认证 / Verified / Official |
| latest_version | 最新版本 |
| compatibility | 支持环境 |
| quick_start | 使用步骤 |
| example_inputs | 示例输入 |
| example_outputs | 示例输出 |
| limitations | 已知限制 |
| created_at | 创建时间 |
| updated_at | 更新时间 |

## 11.2 Skill Version

| 字段 | 说明 |
|---|---|
| version_id | 唯一标识 |
| skill_id | 所属 Skill |
| version | 语义化版本 |
| changelog | 更新说明 |
| compatibility | 当前版本兼容性 |
| install_spec | 安装说明 |
| update_spec | 更新说明 |
| uninstall_spec | 卸载说明 |
| published_at | 发布时间 |
| deprecated_at | 废弃时间 |

## 11.3 Installation

| 字段 | 说明 |
|---|---|
| installation_id | 唯一标识 |
| user_id | 用户 |
| skill_id | Skill |
| environment | GitHub Copilot / OpenCode / Other |
| installed_version | 已安装版本 |
| install_path | 当前 Skill 本地安装路径 |
| backup_path | 最近一次更新产生的备份路径 |
| previous_version | 可回滚的上一版本 |
| update_job_id | 最近一次更新任务 |
| update_status | Pending / Downloading / Backing Up / Installing / Validating / Completed / Rolled Back / Failed |
| status | Installed / Disabled / Update Available / Uninstalled |
| installed_at | 安装时间 |
| updated_at | 更新时间 |
| last_used_at | 最近使用时间，可选 |

## 11.4 Feedback

| 字段 | 说明 |
|---|---|
| feedback_id | 唯一标识 |
| user_id | 提交用户 |
| skill_id | Skill |
| version | 使用版本 |
| environment | 使用环境 |
| rating | 1–5 |
| useful | Yes / No |
| category | 问题类型 |
| comment | 文本 |
| status | Open / Reviewed / Resolved |
| created_at | 提交时间 |

## 11.5 Collection（Day 2）

| 字段 | 说明 |
|---|---|
| collection_id | 唯一标识 |
| name | Collection 名称 |
| description | 描述 |
| owner | Collection Owner |
| skill_ids | Skill 列表 |
| visibility | Internal / Restricted |
| featured | 是否 Featured |
| order | 排序 |

---

## 12. Metadata 建议格式

下面是建议的概念模型，最终字段名由技术方案确认。

```yaml
name: AS400 Code Explainer
slug: as400-code-explainer
description: Explain RPGLE and CL code and generate technical documentation.

owner:
  team: Atlas Engineering
  contact: atlas-engineering@internal.example

repository:
  url: https://github.example.com/atlas/rpgle-explainer
  metadataPath: .github/skills/as400-code-explainer/SKILL.md

version: 1.2.0

sdlc:
  primaryPhase: Discovery
  secondaryPhases:
    - Planning
    - Maintenance

category: AS400 / IBM
tags:
  - RPGLE
  - CL
  - Documentation

compatibility:
  - host: github-copilot
    status: supported
  - host: opencode
    status: supported

quickStart:
  - Install the skill
  - Open a relevant repository
  - Ask the assistant to explain the program

install:
  github-copilot:
    command: "<generated-or-declared-command>"
  opencode:
    command: "<generated-or-declared-command>"

update:
  command: "<generated-or-declared-command>"

uninstall:
  command: "<generated-or-declared-command>"
```

---

## 13. 权限模型

| 操作 | Consumer | Owner | Admin |
|---|---:|---:|---:|
| 浏览 Skill | ✓ | ✓ | ✓ |
| 安装 / 更新 / 卸载 | ✓ | ✓ | ✓ |
| 评分 / 反馈 | ✓ | ✓ | ✓ |
| 注册 Skill | 可配置 | ✓ | ✓ |
| 编辑自己维护的 Skill |  | ✓ | ✓ |
| 发布新版本 |  | ✓ | ✓ |
| 管理 Featured |  |  | ✓ |
| 管理分类和 Phase |  |  | ✓ |
| 下架 Skill |  | 部分 | ✓ |
| 查看全局 Insights |  | 部分 | ✓ |

身份认证应复用公司现有 SSO。

Repository 权限仍由 GitHub 管理，Marketplace 不绕过原有访问控制。

---

## 14. 技术架构原则

建议逻辑架构：

```text
Browser
   |
Atlas Marketplace UI
   |
Registry API
   |------ Skill Metadata Store
   |------ Version Store
   |------ Installation Records
   |------ Feedback / Rating Store
   |------ Analytics Events
   |
GitHub Integration
   |------ Repository Metadata Reader
   |------ Webhook / Scheduled Sync
   |
Installer Orchestration
   |------ Distribution Artifact Resolver
   |------ Update Job / Progress API
   |------ Backup and Rollback Records
   |
Local Execution Component
   |------ Atlas Local Installer / CLI / IDE Extension
   |------ GitHub Copilot Adapter
   |------ OpenCode Adapter
   |------ Atomic Rename / Install / Validate / Rollback
```

关键原则：

1. Marketplace 不复制 Skill 源代码。
2. Registry 中只保存展示、索引和管理所需元数据。
3. 安装逻辑通过 Adapter 适配不同 Host。
4. 更新必须采用 Staging、Backup Rename、安装后校验和自动回滚机制。
5. Distribution Artifact 必须可追溯到明确的 Repository、Commit / Tag 和 Version。
6. GitHub 权限是源代码访问的最终权限边界。
7. Analytics 不应采集敏感源代码或 Prompt 内容。
8. 所有安装、更新、回滚、发布和管理操作必须保留 Audit Log。

---

## 15. 非功能需求

## 15.1 性能

- 首页首次可交互时间目标：内部网络环境下 ≤ 3 秒。
- 搜索响应目标：P95 ≤ 500ms。
- Skill Detail 加载目标：P95 ≤ 1 秒，不含 GitHub 外部请求。
- Repository Sync 应异步执行，不阻塞主页面。

## 15.2 可用性

- 支持主流桌面浏览器；
- 响应式布局；
- 浅色与深色主题；
- 键盘可操作；
- 关键交互有 Loading、Success、Error 状态；
- 空状态和错误状态提供下一步操作。

## 15.3 安全

- 公司 SSO；
- Role-based Access Control；
- Repository Token 最小权限；
- Secret 不写入前端；
- Audit Log；
- 不存储用户私有代码内容；
- 安装命令需防止命令注入；
- 安装包必须校验 Checksum 或 Signature；
- 必须防止 Zip Slip / Path Traversal；
- Rename、写入和删除操作必须限制在已授权 Skill 根目录；
- Update Job 需要幂等和并发锁，避免重复更新破坏目录；
- Metadata 内容需清理危险 HTML。

## 15.4 可访问性

目标达到 WCAG 2.1 AA 的主要要求：

- 对比度；
- 键盘导航；
- Focus 状态；
- 表单 Label；
- 屏幕阅读器语义；
- 不依赖颜色表达唯一信息。

## 15.5 可维护性

- Phase、Category、Tag 可配置；
- Host Adapter 可扩展；
- Quality Score 权重可配置；
- UI 组件统一；
- Metadata Schema 可版本化。

---

## 16. 成功指标

以下为建议指标，最终目标值需结合当前 Skill 数量和内部用户规模确认。

### 16.1 MVP 指标

| 指标 | 建议目标 |
|---|---|
| 已知内部 Skill 注册率 | 上线后 8 周达到 80% |
| 月活跃用户 | 目标用户群的 30% 以上 |
| 搜索到 Detail 转化率 | ≥ 40% |
| Detail 到 Install 启动率 | ≥ 20% |
| Install 成功率 | ≥ 90% |
| Update 成功率 | ≥ 90% |
| Uninstall 成功率 | ≥ 95% |
| 有完整 Quick Start 的 Skill | ≥ 90% |
| 有明确 Owner 的 Skill | 100% |
| 用户首次找到并开始安装的中位时间 | ≤ 2 分钟 |

### 16.2 Day 2 指标

| 指标 | 说明 |
|---|---|
| Saved Skills 使用率 | 收藏功能 adoption |
| Collection 到 Install 转化率 | Collection 是否有效 |
| Owner Feedback Response Time | Owner 响应效率 |
| Stale Skill Rate | 过期 Skill 比例 |
| Update Adoption | 新版本发布后的更新率 |
| Recommendation CTR | 推荐点击率 |
| Repeat Usage | Skill 重复使用情况 |
| Uninstall Rate | 识别低价值或安装问题 |

---

## 17. MVP 验收标准

MVP 可以上线的最低标准：

- [ ] 用户可以通过搜索找到 Skill。
- [ ] 用户可以按 7 个 SDLC Phase 浏览 Skill。
- [ ] 每张 Skill Card 显示 Primary Phase。
- [ ] Skill Detail 信息完整，包括 Quick Start。
- [ ] Owner 可以从不同 GitHub Repository 注册 Skill。
- [ ] Metadata Validation 可识别缺失字段。
- [ ] 平台支持至少 GitHub Copilot 和 OpenCode 两个安装目标的配置。
- [ ] 用户可以完成 Install、One-click Update 和 Uninstall。
- [ ] One-click Update 会先下载并校验最新版本，再将当前 Skill Rename 为 Backup。
- [ ] 新版本安装失败时，系统会自动恢复 Backup。
- [ ] My Skills 可以展示 Installed Version、Latest Version、Update Status 和 Update Available。
- [ ] 用户可以在备份保留期内回滚到上一版本。
- [ ] 用户可以提交基础评分和反馈。
- [ ] Owner 可以管理自己发布的 Skill。
- [ ] Admin 可以下架 Skill 和管理 Featured。
- [ ] 支持公司 SSO 和基础角色权限。
- [ ] 支持浅色和深色主题。
- [ ] 关键管理操作有 Audit Log。
- [ ] 不需要将 Skill 源代码迁移到统一 Repository。

---

## 18. 交付阶段

## Phase 0：设计与技术验证

- 确认 Metadata Schema；
- 确认 GitHub Repository 读取方式；
- 验证 Copilot / OpenCode 安装、目录 Rename、更新、自动回滚和卸载方式；
- 确定 Atlas Local Installer、CLI 或 IDE Extension 的 MVP 实现；
- 确认身份和权限集成；
- 完成 UX / UI 定稿；
- 确认 MVP 数据范围。

## Phase 1：MVP Pilot

- Discover；
- Search；
- SDLC Browse；
- Skill Detail；
- Register Skill；
- Version；
- Install / One-click Update / Rollback / Uninstall；
- Local Installer / CLI / IDE Extension 的最小可用实现；
- My Skills；
- Basic Feedback；
- Owner Management；
- Admin Basics。

建议先选择 10–20 个高质量 Skill 进行 Pilot。

## Phase 2：MVP General Availability

- 扩大 Skill 覆盖；
- 完善质量和文档；
- 优化搜索；
- 加强监控；
- 建立 Owner 运营机制；
- 发布内部推广材料。

## Phase 3：Day 2

- Quality Score；
- Leaderboard；
- Collections；
- Save / Favorite；
- Recommendation；
- Owner Insights；
- Lifecycle Alerts；
- Certification。

---

## 19. 风险与应对

### 风险 1：Skill Metadata 不统一

**影响：** 注册失败、详情页质量不一致。
**应对：** 提供模板、Validation、Preview 和迁移指南。

### 风险 2：浏览器无法直接安全修改本地 Skill 目录

**影响：** 无法仅依赖 Web 页面完成目录 Rename、文件写入和回滚。
**应对：** MVP 配套 Atlas Local Installer、CLI 或 IDE Extension；Web 页面负责发起和展示进度，本地组件负责受控执行。

### 风险 3：更新中断导致 Skill 不可用

**影响：** 当前 Skill 已被 Rename，但新版本未成功安装。
**应对：** 先下载和校验到 Staging；使用原子 Rename；实现自动回滚、幂等 Update Job、目录锁和 Audit Log。

### 风险 4：Marketplace 变成静态目录

**影响：** 用户浏览但不安装。
**应对：** 强化 Quick Start、Install CTA、My Skills 和 Update / Uninstall。

### 风险 5：Owner 不维护

**影响：** 过期 Skill 增多。
**应对：** Owner 必填、Freshness、Lifecycle Alert、Deprecated 和 Archive。

### 风险 6：评分失真

**影响：** 少量评分导致误导。
**应对：** 显示样本数量，多维质量分与用户评分分开。

### 风险 7：内部权限与敏感信息

**影响：** 用户看到无权限 Repository 或内部敏感描述。
**应对：** 继承 GitHub 权限、支持 Restricted Visibility、Metadata 审核和内容规范。

### 风险 8：范围持续膨胀

**影响：** MVP 延迟。
**应对：** 明确不做在线执行、Workflow Engine 和 Agent Runtime；Day 2 也聚焦 Marketplace 运营与治理。

---

## 20. 待确认问题
1. 公司内部 GitHub 是 GitHub Enterprise Cloud、Server，还是其他部署方式？
2. GitHub Copilot Skill 的最终目录结构和安装路径是否已经统一？
3. OpenCode 的安装、更新和卸载目录结构是否稳定？
4. MVP 的本地执行组件采用 Atlas Local Installer、CLI、IDE Extension，还是组合方案？
5. Web 页面如何唤起本地执行组件：Custom URI、Local Service、Browser Extension，还是 IDE Deep Link？
6. “最新版本”由 Git Tag、GitHub Release 还是 Metadata Version 决定？
7. Distribution Artifact 由 Marketplace 托管、缓存，还是直接使用 GitHub Release？
8. Backup 的默认命名规则和保留时间是多少？
9. 一个 Skill 最多保留几个 Backup？
10. Update 完成后是否保留旧 Backup 供手动回滚？
11. 更新失败时是否需要自动重试？最大重试次数是多少？
12. 本地执行组件如何上报安装路径、更新进度和最终状态？
13. 谁可以 Register Skill：所有员工、指定团队，还是需要审批？
14. Publish 是否需要 Admin Review？
15. Primary Phase 是否允许由管理员修改？
16. Common Skill 是否必须指定一个 Primary Phase？
17. Rating 是否匿名？
18. Feedback 是否自动创建 GitHub Issue？
19. Repository 为 Private 时，Marketplace 如何处理无权限用户？
20. Quality Score 的初始权重由哪个团队负责？
21. 是否需要支持多语言 Skill 文档？
22. 是否需要对 Metadata Schema 做组织级标准化和版本管理？
23. Analytics 能否获得真实使用数据，还是只能记录 Marketplace 行为和安装数据？
---

## 21. 当前产品决策记录

已确认：

- 产品名称使用 **Atlas Marketplace**。
- 产品以内部使用为主。
- Skill 保留在分布式 GitHub Repository。
- Marketplace 作为集中 Registry。
- 不采用统一 Monorepo。
- 核心操作为 Discover、Register、Install、One-click Update、Rollback、Uninstall。
- One-click Update 会将当前 Skill Rename 为 Backup，再将已校验的最新版本安装到原路径。
- 更新失败必须自动恢复上一版本。
- 为实现本地目录操作，产品需要 Atlas Local Installer、CLI 或 IDE Extension。
- 必须有 My Skills。
- 必须明确 Skill 所属 SDLC Phase。
- SDLC Phases 为 Planning、Estimation、Discovery、Build、Testing、Deployment、Maintenance。
- UI 采用产品官网式 Marketplace 布局。
- 视觉追求高级、自然、科技感，但避免过度 AI 风格。
- 支持浅色和深色主题。
- MVP 不做在线执行。
- Day 2 重点为质量、推荐、集合、榜单、Owner Insights 和生命周期治理。
- Day 2 不引入 Workflow Engine。

---

## 22. 下一步

1. 对本 PRD 进行产品评审；
2. 回答“待确认问题”中的关键技术和治理问题；
3. 确认 MVP Priority：P0 / P1；
4. 输出 Metadata Schema v0.1；
5. 输出页面级 UX Specification；
6. 输出技术架构设计；
7. 将 MVP 拆分为 Epic、User Story 和 Acceptance Criteria；
8. 确认 Pilot Skill 列表和 Owner；
9. 确认开发计划与上线窗口。
