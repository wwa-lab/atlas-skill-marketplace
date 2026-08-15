# Atlas Marketplace 产品需求文档（PRD）

**文档版本：** v0.3 Draft
**更新时间：** 2026-08-15
**产品名称：** Atlas Marketplace
**首个试点：** IBM iSeries RPGLE Program Analysis Skill
**目标读者：** 产品、设计、工程、平台运营、AI SME、IBM iSeries SME、试点用户

---

## 0. v0.3 变更摘要

v0.3 将 v0.2 的广义 Marketplace MVP 收敛为一个可被真实验证的强场景：

> 负责评审和估算改动的 TL，不询问同事、不阅读额外操作文档，在 Atlas
> 中找到并安装 RPGLE 分析 Skill，在 VS Code GitHub Copilot Chat 中明确
> 调用它，并获得包含程序高层信息、调用关系和依赖全景的有效报告。

本版本确认：

- 首要问题是用户不知道 Skill 是否存在、在哪里，而不是单纯不会复制目录；
- 首批只验证 Windows、VS Code GitHub Copilot Chat 和 IBM iSeries/RPGLE；
- Discover 以平台主分类和普通关键词搜索为核心，不做语义搜索；
- 安装支持个人级和项目级，项目级在对应项目中优先；
- 更新由用户确认，技术失败自动回滚，质量退化由用户手动回滚；
- 认证绑定具体版本，新版本默认未认证但仍允许在警告后安装；
- AI SME 自助发布并维护内容，平台只提供机制和运营支持；
- 反馈、更新、回滚必须形成可追踪闭环；
- 首批试点以 10 名真实首次用户现场观察，而不是演示数据验收。

v0.3 是当前产品范围与验收的主来源。v0.2 保留为历史基线；V2.6 HTML
原型仅作为视觉与交互参考，不能证明某项能力属于 MVP 或已经实现。

## 1. 产品定位与边界

### 1.1 一句话定位

> Atlas Marketplace 是公司内部集中发现、评估、安装和管理 AI Skills 的
> Marketplace；Skill 源代码继续由 Owner 保存在分布式 GitHub Enterprise
> Repository 中。

### 1.2 核心价值

对 Skill Consumer：

- 不依赖同事口头传播即可发现适合工作场景的 Skill；
- 通过示例输出、使用场景、Owner、版本和认证状态判断是否值得安装；
- 从网页发起安全的本地安装、更新和回滚；
- 获得可复制的调用方式并验证 Skill 在目标 Host 中可用；
- 将问题反馈给 Owner 并查看处理状态。

对 Skill Owner / AI SME：

- 以现有 GitHub Repository 为内容源，无需迁移到平台 Monorepo；
- 自助注册、发布版本、同步 Metadata、查看反馈；
- 通过频繁发布让使用者及时获得调优后的版本。

### 1.3 Atlas 是什么

- 集中式 Skill Registry 和产品化浏览入口；
- GitHub Repository 与公司用户之间的权限感知层；
- 安装、更新、回滚、反馈和审计的协调层；
- 版本化 Distribution Artifact 的受控分发入口。

### 1.4 Atlas 不是什么

- Skill 源代码托管或在线编辑平台；
- GitHub 的替代品或权限绕过工具；
- 在线 Skill Runtime、Agent Runtime 或 Workflow 编排平台；
- Prompt Playground；
- 面向互联网的公开商店或商业交易平台；
- 强制所有 Skill 进入同一个 Repository 的平台。

## 2. 试点问题、假设与目标

### 2.1 当前问题

Developer、TL 或 SME 处理 IBM iSeries 工作时，通常通过口头询问同事寻找
可用 Skill。他不仅不知道如何把 GitHub 中的 Skill 复制到本地 Copilot
目录，往往连 Skill 是否存在、位于哪个 Repository 都不知道。

对负责 RPGLE 改动评审和估算的 TL，最严重的失败是分析报告漏掉需要修改
的依赖程序，进而造成范围和工作量估算错误。

### 2.2 产品假设

如果 Atlas 能让首次使用者在一个入口中：

1. 通过 `IBM iSeries` 分类或相关关键词找到 RPGLE 分析 Skill；
2. 用示例报告和版本认证信息建立基本信任；
3. 一键安装到 Windows 上的目标范围；
4. 复制明确的调用命令并在 VS Code GitHub Copilot Chat 中获得有效报告；

那么一个强场景就足以验证 Marketplace 的核心价值。

### 2.3 试点目标

- 验证用户能自行发现、安装并首次调用 Skill；
- 验证网页到可信本地组件的安装闭环；
- 验证每周约两次 Skill 更新所需的发现、确认、回滚闭环；
- 验证认证、警告和反馈机制是否足以支持风险知情使用；
- 为后续扩展到其他技术分类和 Host 建立可复用协议，而不是预先实现全部平台。

## 3. 用户与责任

### 3.1 Primary Consumer：负责评审和估算的 TL

目标：快速理解 RPGLE 程序做什么、调用哪些程序、改动影响哪些依赖。
核心任务：发现 → 评估 → 安装 → 调用 → 阅读报告 → 更新/回滚 → 反馈。

### 3.2 其他 Consumer

Developer、其他 TL 和 SME 可使用相同能力。公司所有员工均可登录和使用
Atlas；10 人只是首批推广和观察对象，不是系统内的特殊租户或许可范围。

### 3.3 Skill Owner / AI SME

- 负责 Skill 内容、Prompt/说明、Metadata 和调优质量；
- 预计每周发布约两次；
- 点击 Publish 并处理用户反馈；
- 对发布内容负责，平台不替代其内容审核职责。

### 3.4 IBM iSeries SME

- 用标准 RPGLE 程序集运行特定 Skill Version；
- 核对依赖识别结果；
- 对该版本作出认证、未通过或暂停认证结论。

### 3.5 Atlas Marketplace 平台团队

- 提供注册、发布、版本、安装、更新、回滚、权限、反馈和运营能力；
- 维护主分类与同义词；
- 不维护 Owner 的 Skill 内容；
- 在 Owner 长期不活跃等治理事件中介入。

## 4. MVP Pilot 范围

### 4.1 必须交付

- 公司 SSO 登录和 GitHub Repository 权限感知；
- Discover、关键词搜索、主分类筛选、Skill Card 和 Skill Detail；
- 一个可用的 IBM iSeries/RPGLE 分析 Skill；
- Windows 本地组件的引导安装和原任务自动续装；
- 个人级与项目级安装，版本选择和 Host 调用指引；
- My Skills、Update Available、用户确认更新、自动/手动回滚和按范围卸载；
- GitHub Repository 注册、Owner 确认、Metadata 校验、自助发布和同步；
- 版本认证显示、警告和累计高风险反馈暂停规则；
- 版本化反馈、Owner 回复和状态追踪；
- 必要的审计和不包含源码/Prompt 的产品行为分析。

### 4.2 明确不在试点内

- macOS、Linux 或其他 IDE/Host 的可用性承诺；
- Copilot CLI 作为试点主 Host；
- 自然语言语义搜索；
- 后台静默自动更新 Skill 或本地组件；
- 多份历史备份、任意历史版本的一键回滚；
- 在线执行 Skill；
- 上传程序源码、Prompt 或工作材料到 Atlas；
- Leaderboard、Trending、Most Installed、Collections、Favorites、Owner
  Insights 等 Day 2 运营能力。

Day 2 入口若出现在 UI 中，只能显示 `Coming Soon`，不得使用静态数字或
假数据冒充已运行能力。

## 5. Discover 与搜索

### 5.1 分类体系

- **FR-DIS-001**：平台 MUST 维护稳定的主分类；试点主分类为
  `IBM iSeries`。
- **FR-DIS-002**：平台 MUST 维护同义词，将 `AS400`、`IBM i`、
  `iSeries` 等搜索词映射到 `IBM iSeries`。
- **FR-DIS-003**：Owner MAY 添加自由标签，但不得创建或改写主分类。
- **FR-DIS-004**：Skill MAY 继续记录 SDLC Phase，但试点发现入口不得
  依赖用户先知道某个 Phase。

### 5.2 搜索与排序

- **FR-DIS-005**：用户 MUST 能按主分类筛选，并用普通关键词跨名称、
  描述、使用场景、标签和同义词搜索。
- **FR-DIS-006**：试点 MUST NOT 宣称支持语义或自然语言搜索。
- **FR-DIS-007**：默认结果首先按用户评分排序；没有评分的新 Skill
  先按当前版本是否已认证，再按搜索相关性排序。
- **FR-DIS-008**：无结果时 MUST 推荐相邻分类或相关 Skill，并提供清除
  筛选的路径。

### 5.3 Skill Card

Skill Card MUST 至少展示：

- 名称、短描述、Owner/Primary Maintainer；
- 主分类、自由标签和具体使用场景；
- 最新发布版本与该版本认证状态；
- 一个可快速预览的精选示例输出；
- 当前用户的访问/安装资格；
- 评分；无评分时显示明确的 `Not yet rated`，不得显示虚构分数。

用户判断价值时，示例输出是第一信号，具体使用场景是第二信号。

## 6. Skill Detail 与 RPGLE 示例报告

- **FR-DET-001**：Detail MUST 展示完整描述、适用场景、限制、Owner、
  Repository、版本、Changelog、兼容性、安装范围和调用方式。
- **FR-DET-002**：RPGLE Skill MUST 展示一份 Owner 精选的程序分析报告，
  而不是笼统营销文案或并排的输入/输出截图。
- **FR-DET-003**：精选报告 MUST 包含有数据支持的程序高层信息、程序用途、
  调用关系、依赖清单和程序全景图。
- **FR-DET-004**：认证详情 MUST 展示认证 SME、测试日期、Skill Version
  和认证结论。
- **FR-DET-005**：没有 Repository 权限时，普通可见 Skill 的名称和描述
  MAY 被展示，但 MUST 标记 `No access`，禁止安装并提供 `Contact Owner`。
- **FR-DET-006**：Restricted/Hidden Skill 对无 Repository 权限的用户
  MUST 完全隐藏。

## 7. 安装与首次调用

### 7.1 安装前提

- 试点 OS 为 Windows；目标 Host 为 VS Code GitHub Copilot Chat。
- 用户有权限在受管 Windows 设备上安装 Atlas 本地组件，不要求临时管理员
  或 IT 审批。
- Atlas 只能分发可追溯到 Repository、Git Tag、Revision 和完整性校验值的
  Artifact。

### 7.2 安装流程

- **FR-INS-001**：每次安装 MUST 让用户选择 `Personal` 或 `Project`；
  默认选择 `Personal`。
- **FR-INS-002**：用户 MUST 可安装默认 Latest，也可选择仍被允许安装的
  较旧 Published Version。
- **FR-INS-003**：点击安装但没有本地组件时，Atlas MUST 引导安装本地
  组件，并在组件就绪后自动继续原 Skill 安装，不要求用户重新寻找 Skill。
- **FR-INS-004**：安装界面只需显示 Loading 和明确的最终成功/失败结果，
  不要求逐步技术进度。
- **FR-INS-005**：首次安装失败时 MUST 立即显示可理解的原因和手动 Retry；
  不自动重试或自动创建支持工单。
- **FR-INS-006**：同一用户可在个人和项目范围安装同一 Skill 的不同版本；
  在该项目中 Project 版本 MUST 优先于 Personal 版本。
- **FR-INS-007**：安装操作 MUST 可重复安全执行、并发受控、中断可恢复，
  且不得写出获授权的规范化 Skill Root。

### 7.3 调用引导

- **FR-USE-001**：安装成功后，Atlas MUST 展示可复制的精确调用命令。
- **FR-USE-002**：用户必须明确输入 Skill 名称调用，不能以“可能被系统
  自动采用”作为试点成功证据。
- **FR-USE-003**：有效首次输出 MUST 是一次可识别的 RPGLE 程序分析报告，
  包含程序高层信息及依赖/调用关系，而不是仅返回帮助文字或错误。
- **FR-USE-004**：最终命令语法和 VS Code Copilot Chat 的 Skill 加载能力
  必须在技术验证中以官方支持行为和实机测试确认。

## 8. My Skills、更新与回滚

### 8.1 My Skills

- **FR-MYS-001**：同一 Skill 合并成一张卡片；展开后分别展示 Personal
  和各 Project 安装的版本、状态和可执行操作。
- **FR-MYS-002**：每个范围独立显示 Installed Version、Latest Eligible
  Version、认证状态和 Update Available。
- **FR-MYS-003**：项目级和个人级安装 MUST 分别更新，用户每次选择目标范围。

### 8.2 用户确认更新

- **FR-UPD-001**：Atlas 发现更高的 Eligible Version 后，在 Atlas 内显示
  `Update Available`；试点不要求 Windows 或 VS Code 通知。
- **FR-UPD-002**：更新不得静默发生。确认前 MUST 展示 Changelog、目标版本、
  认证状态和 Host/环境兼容性。
- **FR-UPD-003**：用户确认后，本地组件 MUST 先保留当前版本，再下载、
  校验、替换和验证目标版本。
- **FR-UPD-004**：只保留紧邻的上一个本地安装版本作为可回滚备份。

### 8.3 回滚

- **FR-RBK-001**：下载、完整性校验、文件替换或 VS Code/Copilot 加载失败时，
  MUST 自动恢复旧版本。
- **FR-RBK-002**：自动回滚成功后 MUST 明确显示：更新失败、旧版本已恢复、
  失败原因。
- **FR-RBK-003**：输出质量退化不属于自动回滚判断；用户 MUST 能手动回滚
  到紧邻的上一个本地版本。
- **FR-RBK-004**：手动回滚后，该安装范围 MUST 忽略刚被回滚的问题版本，
  直到 Owner 发布版本号更高的新版本。
- **FR-RBK-005**：更新与回滚 MUST 使用锁、幂等标识和可审计状态，防止并发、
  重试或进程中断破坏当前可用安装。

### 8.4 本地组件自更新

- **FR-CLI-001**：本地组件 MAY 检测自身新版本，但 MUST 由用户确认更新；
  试点不做静默升级或 IT 集中推送。

### 8.5 卸载

- **FR-UNI-001**：用户 MUST 能从 My Skills 卸载一个明确选择的 Personal
  或 Project 安装范围，而不影响其他范围。
- **FR-UNI-002**：卸载 MUST 在执行前显示 Skill、版本、Host 和范围，并要求
  用户确认；操作只能删除受管安装内容，不得删除 Project 或其他用户文件。
- **FR-UNI-003**：卸载失败或中断时 MUST 形成明确、可重试的状态，不得把
  部分残留误报为成功。

## 9. Owner 注册、发布与同步

- **FR-OWN-001**：注册者 MUST 对目标 GitHub Repository 有 Write/Maintain
  权限。
- **FR-OWN-002**：注册者手工填写一个 Owner；Owner 可为个人或团队，但
  MUST 指定一名具名 Primary Maintainer。
- **FR-OWN-003**：被填写的 Owner MUST 确认，首个版本才可发布。
- **FR-OWN-004**：Repository Metadata 是 Skill 信息主来源；Atlas 表单只
  补充缺失内容，不形成竞争性的完整副本。
- **FR-OWN-005**：Git Tag 是 Published Version 的权威版本标识；Metadata
  中若也声明版本，MUST 与 Tag 一致，否则校验失败。
- **FR-OWN-006**：Metadata 校验通过、权限和 Owner 确认满足后，AI SME 点击
  Publish 即立即可见，不需要平台人工审批。
- **FR-OWN-007**：GitHub Webhook MUST 自动发现变化，同时保留手动 Refresh。
- **FR-OWN-008**：Repository 删除或不可访问时，页面保留并标记
  `Repository Unavailable`，暂停新安装；已有本地安装不被远程删除。
- **FR-OWN-009**：Owner 转移 MUST 由现 Owner 与新 Owner 双方确认。
- **FR-OWN-010**：Owner 不活跃时 Skill 仍可展示和安装，并显示警告；连续
  一年不活跃后，平台管理员必须介入转移 Owner 或 Delist。

## 10. 认证、风险警告与反馈

### 10.1 版本认证

- **FR-CER-001**：IBM iSeries SME 使用平台认可的标准 RPGLE 程序集运行
  具体 Skill Version，并核对依赖结果。
- **FR-CER-002**：认证只属于被测试的版本；任何新版本发布时自动为
  `Uncertified`，通过重新测试后才能恢复 `Certified`。
- **FR-CER-003**：未认证版本仍可作为 Latest 正常安装，但安装前和 Detail
  MUST 显示明显警告，由用户知情承担风险。
- **FR-CER-004**：同一版本生命周期内累计达到 10 条独立的
  `Missed dependency` 高风险反馈时，系统 MUST 自动暂停该版本认证。
- **FR-CER-005**：单条反馈或未达到阈值的多条反馈不自动暂停认证。
- **FR-CER-006**：暂停认证或高风险反馈逾期未处理时，仍允许新安装，但
  MUST 显示更高风险警告。

### 10.2 反馈闭环

- **FR-FBK-001**：用户可在 Skill Detail 或 My Skills 直接提交反馈。
- **FR-FBK-002**：反馈 MUST 自动关联 Skill、Skill Version 和安装范围，
  并记录文字描述；用户 MAY 提供程序或 Repository 标识。
- **FR-FBK-003**：反馈不得上传或要求用户粘贴程序源码、Prompt 或 Secret。
- **FR-FBK-004**：高风险漏依赖反馈的初次响应 SLA 为 2 个工作日。
- **FR-FBK-005**：提交者 MUST 能查看 `Open`、`Reviewed`、`Resolved` 状态和
  AI SME 回复。
- **FR-FBK-006**：修复发布后不单独定向通知提交者；他与其他用户一样看到
  正常 `Update Available`。

## 11. 权限、可见性与隐私

- **FR-AUT-001**：公司所有员工可通过公司身份登录 Atlas。
- **FR-AUT-002**：普通内部 Skill 可在 Catalog 展示名称和描述；没有底层
  Repository 权限的用户不得获取 Artifact 或安装。
- **FR-AUT-003**：`Restricted` / `Hidden` Skill MUST 直接继承 GitHub
  Repository 访问权限，无权限用户不得在搜索、推荐或直接 URL 中看到 Metadata。
- **FR-AUT-004**：前端显示不是授权证据；Registry API 和 Artifact 获取路径
  MUST 在每次敏感操作重新检查身份、Repository 权限和版本资格。
- **FR-AUT-005**：Atlas MAY 收集搜索词、浏览、安装、更新、回滚和反馈行为，
  但 MUST NOT 收集源代码、Prompt、Secret 或不受控本地路径内容。
- **FR-AUT-006**：权限、发布、安装、更新、回滚、认证暂停和 Owner 转移等
  敏感动作 MUST 留下可追踪审计事件。

## 12. 核心实体与状态

### 12.1 核心实体

- **Skill**：Catalog 身份、Repository、Owner、Primary Maintainer、分类、标签、
  可见性、描述和精选示例报告。
- **Skill Version**：Git Tag、Source Revision、Changelog、兼容性、发布状态、
  认证状态和 Distribution Artifact。
- **Certification**：版本、SME、标准程序集、测试日期、结果和暂停原因。
- **Installation**：用户、Host、范围、Project 标识、Installed Version、状态和
  被抑制的问题版本。
- **Lifecycle Job**：安装、更新、自动/手动回滚或卸载的幂等任务与结果。
- **Backup**：紧邻的前一个本地版本及其完整性和恢复状态。
- **Feedback**：版本化用户报告、安装范围、风险类型、Owner 回复和状态。
- **Repository Sync**：Webhook/Refresh 触发的 Revision、校验和同步结果。
- **Audit Event**：Actor、Action、Target、Outcome 和安全过滤后的上下文。

### 12.2 必须区分的版本概念

`Git Tag Version`、`Published Version`、`Latest Eligible Version`、
`Installed Version`、`Target Version` 和 `Previous Version` 不得压缩为一个
含义模糊的 `version` 字段。

### 12.3 关键状态

- Publication：`Draft`、`Published`、`Deprecated`、`Archived`
- Certification：`Uncertified`、`Certified`、`Suspended`、`Failed`
- Installation：`Installed`、`Update Available`、`Install Incomplete`、
  `Repository Unavailable`、`Uninstalled`
- Feedback：`Open`、`Reviewed`、`Resolved`

状态转换必须显式校验，不得通过缺失字段或 UI 文案推断。

## 13. 关键流程

### 13.1 Consumer 首次成功

```text
Login -> Search/IBM iSeries -> Review example + certification
-> Choose version and scope -> Install local component if needed
-> Resume Skill install -> Copy invocation command
-> Invoke in VS Code Copilot Chat -> Receive valid RPGLE report
```

### 13.2 Skill 更新

```text
Webhook/manual sync -> New published version -> Update Available
-> Review changelog/certification/compatibility -> Choose scope -> Confirm
-> Preserve previous -> Download + validate + replace + load check
-> Success OR automatic restore with explicit reason
```

### 13.3 Owner 发布

```text
Register repository -> Verify Write/Maintain -> Enter Owner + Maintainer
-> Owner confirms -> Read metadata + Git Tag -> Validate
-> AI SME clicks Publish -> Visible immediately -> New version uncertified
```

### 13.4 高风险反馈

```text
Submit missed-dependency feedback -> Auto-associate version/scope
-> Open -> Owner initial response within 2 business days
-> Reviewed/Resolved -> New version published -> Normal Update Available
```

## 14. 非功能与安全要求

- 所有外部 Metadata、Webhook、Artifact Manifest 和本地请求都必须使用版本化
  Schema 校验并对渲染内容做安全处理。
- 本地组件不得执行 Metadata 提供的任意 Shell Command；所有路径必须在允许的
  Host Root 内规范化，并防御 Path Traversal、Zip Slip 和 Symlink Escape。
- Artifact 必须不可变，并可追溯到 Repository、Git Tag、Revision 和 Checksum
  或 Signature。
- 安装、更新、回滚和卸载必须幂等、互斥、可审计、可中断恢复。
- 错误信息面向用户可理解，但不得泄露 Token、Secret、源码或敏感绝对路径。
- 关键 Consumer 路径应支持键盘操作和清晰的警告语义，不仅依靠颜色表达状态。
- 在选择具体技术栈前，不预设 Web、API、数据库或本地组件框架。

## 15. 成功指标与试点验收

### 15.1 北极星试点指标

在不询问同事、不查看额外操作文档的情况下，10 名首次使用 Atlas 的目标用户
中，至少 7 名在 10 分钟内：

1. 找到 RPGLE 分析 Skill；
2. 成功安装到选择的范围；
3. 在 VS Code GitHub Copilot Chat 中明确调用该 Skill；
4. 获得一次包含程序高层信息和依赖/调用关系的有效输出。

### 15.2 验收方法

- 以 10 名真实首次用户的现场观察为准；
- 观察者只能记录，不得口头提示或提供额外安装文档；
- 计时从用户进入已登录的 Atlas Discover 页面开始；
- 失败必须分类为发现、理解、权限、本地组件、安装、调用或输出无效；
- 首批 10 人不获得特殊系统权限或专用产品流程。

### 15.3 质量与安全验收

- 定义范围内的下载、校验、替换和 Host 加载故障均能恢复旧版本；
- 未授权用户不能取得 Restricted/Hidden Metadata 或 Artifact；
- 安装与更新不能写出授权 Host Root；
- 埋点和反馈记录不包含源代码或 Prompt；
- 新版本不会继承旧版本认证；
- 每条高风险反馈可追踪 Version、Scope、状态、回复和 SLA 时间。

## 16. 交付阶段

### Phase 0：技术与风险验证

- 验证 VS Code GitHub Copilot Chat 的 Skill 安装目录、发现机制和明确调用语法；
- 选择 Windows 本地组件形态及浏览器到本地组件的安全协议；
- 明确 GitHub Enterprise/SSO/Repository 权限集成；
- 明确 Artifact 构建、托管、签名/校验和保留策略；
- 用标准 RPGLE 程序建立认证基线和有效输出判定。

### Phase 1：RPGLE Pilot

- 完成 Discover → Detail → Install → Invoke 主路径；
- 完成 Publish → Sync → Update → Rollback；
- 完成版本认证、警告和反馈闭环；
- 运行 10 人现场观察并记录结果。

### Phase 2：试点修正与推广判断

- 优先修复阻止 7/10 指标的问题；
- 评估是否扩展更多 Skill、技术分类、Host 或 OS；
- 未通过试点前，不以 Day 2 运营功能替代核心闭环问题。

## 17. 已知风险与刻意保留的权衡

### R-001：高风险 Skill 仍可安装

漏依赖是本场景最严重的错误，但未认证、认证暂停或高风险反馈逾期版本仍允许
安装。这是已确认的“警告后用户自行承担风险”策略，不代表风险已被消除。
试点必须观察用户是否看懂警告，后续可依据事故和行为数据重新评审门槛。

### R-002：10 条暂停阈值可能过迟

认证仅在同一版本生命周期累计 10 条独立漏依赖反馈后自动暂停，可能让低使用量
但严重错误的版本长期保持认证。必须保证“独立反馈”的反滥用和去重规则可审计。

### R-003：Host 能力尚待实证

Copilot CLI 已有明确 `/SKILL-NAME` 文档，不等于 VS Code GitHub Copilot Chat
采用相同语法。试点不能在实机验证前承诺具体命令。

### R-004：一键安装扩大本地信任边界

浏览器不能直接安全修改本地目录；协议、用户同意、路径白名单、Artifact 完整性、
并发和中断恢复必须在实现前形成 ADR 与版本化 Contract。

### R-005：频繁更新增加质量与回滚压力

Owner 预计每周发布两次。版本特定认证、新版本未认证、用户确认更新、单份备份和
问题版本抑制共同降低风险，但无法替代 Skill 内容质量责任。

### R-006：Repository Metadata 与 Git Tag 漂移

Git Tag 是版本权威；Metadata 是描述主来源。如果 Metadata 内含版本且不一致，
必须阻止发布，而不是猜测哪个值正确。

## 18. 技术决策待办

这些问题不改变已确认的用户行为，但在实现计划前必须通过研究、Contract 或 ADR
解决：

1. VS Code GitHub Copilot Chat 对 Agent Skill 的受支持目录、刷新方式和明确调用语法；
2. Windows 本地组件采用 CLI、后台服务、VS Code Extension 或组合；
3. 浏览器发现、唤起、授权并恢复本地安装任务的协议；
4. GitHub Enterprise 类型、SSO 映射和逐请求 Repository 权限校验方式；
5. Distribution Artifact 的构建主体、托管、Checksum/Signature 与撤回机制；
6. 认证标准程序、依赖真值集、独立反馈去重和有效输出判定规则；
7. 普通可见与 Restricted/Hidden Metadata 的缓存和搜索索引隔离；
8. 本地组件自更新的签名、兼容性和失败恢复策略。

## 19. 决策记录摘要

| 主题 | v0.3 决策 |
|---|---|
| 首个场景 | IBM iSeries RPGLE 程序分析 |
| Primary Persona | 负责评审和估算改动的 TL |
| 最严重错误 | 漏掉程序依赖 |
| 首要价值 | 自助发现并安装，不再询问同事 |
| Pilot OS / Host | Windows / VS Code GitHub Copilot Chat |
| 搜索 | 主分类 + 普通关键词；平台维护同义词 |
| 排序 | 有评分按评分；无评分按认证与相关性 |
| 卡片第一信号 | 精选示例报告 |
| 安装范围 | Personal 默认；支持 Project，Project 优先 |
| 更新 | Atlas 内提示，用户查看信息后确认 |
| 回滚 | 技术失败自动；质量问题手动；只保留上一版 |
| 认证 | 版本特定；新版本默认未认证但仍可安装 |
| 认证暂停 | 同版本生命周期累计 10 条独立漏依赖反馈 |
| 发布 | Metadata 校验 + Owner 确认后 AI SME 自助发布 |
| 版本权威 | Git Tag；Metadata 若声明版本必须一致 |
| 同步 | GitHub Webhook + 手动 Refresh |
| 反馈 SLA | 高风险反馈 2 个工作日内初次响应 |
| 可见性 | 普通内部 Metadata 可见；Restricted/Hidden 继承 Repo 权限 |
| Pilot 成功 | 10 人中至少 7 人在 10 分钟内完成并获得有效输出 |
